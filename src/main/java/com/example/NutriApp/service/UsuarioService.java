package com.example.NutriApp.service;

import com.example.NutriApp.model.Usuario;
import com.example.NutriApp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> getUsuarioByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Optional<Usuario> getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario registrarUsuario(Usuario usuario) {
        // Verificar si el usuario ya existe
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está registrado");
        }
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> loginUsuario(String usernameOrEmail, String passwordHash) {
        Optional<Usuario> usuario = usuarioRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        
        if (usuario.isPresent() && usuario.get().getPasswordHash().equals(passwordHash)) {
            return usuario;
        }
        return Optional.empty();
    }

    public Usuario updateUsuario(Long id, Usuario usuarioDetails) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            Usuario user = usuario.get();
            if (usuarioDetails.getFullName() != null && !usuarioDetails.getFullName().isEmpty()) {
                user.setFullName(usuarioDetails.getFullName());
            }
            if (usuarioDetails.getEmail() != null && !usuarioDetails.getEmail().isEmpty()) {
                // Verificar que el email no esté registrado por otro usuario
                Optional<Usuario> existingEmail = usuarioRepository.findByEmail(usuarioDetails.getEmail());
                if (existingEmail.isPresent() && !existingEmail.get().getId().equals(id)) {
                    throw new IllegalArgumentException("El email ya está registrado");
                }
                user.setEmail(usuarioDetails.getEmail());
            }
            if (usuarioDetails.getPasswordHash() != null && !usuarioDetails.getPasswordHash().isEmpty()) {
                user.setPasswordHash(usuarioDetails.getPasswordHash());
            }
            return usuarioRepository.save(user);
        }
        throw new IllegalArgumentException("Usuario no encontrado");
    }

    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    public boolean existeUsuario(Long id) {
        return usuarioRepository.existsById(id);
    }
}
