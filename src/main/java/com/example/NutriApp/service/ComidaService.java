package com.example.NutriApp.service;

import com.example.NutriApp.model.Comida;
import com.example.NutriApp.model.Usuario;
import com.example.NutriApp.repository.ComidaRepository;
import com.example.NutriApp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ComidaService {

    @Autowired
    private ComidaRepository comidaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Comida> getAllComidas() {
        return comidaRepository.findAll();
    }

    public Optional<Comida> getComidaById(Long id) {
        return comidaRepository.findById(id);
    }

    public List<Comida> getComidasByUsuario(Long usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            return comidaRepository.findByUsuario(usuario.get());
        }
        throw new IllegalArgumentException("Usuario no encontrado");
    }

    public List<Comida> getComidasByUsuarioAndFecha(Long usuarioId, LocalDate fecha) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            return comidaRepository.findByUsuarioAndFecha(usuario.get(), fecha);
        }
        throw new IllegalArgumentException("Usuario no encontrado");
    }

    public List<Comida> getComidasByUsuarioAndTipo(Long usuarioId, String tipoDeComida) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            return comidaRepository.findByUsuarioAndTipoDeComida(usuario.get(), tipoDeComida);
        }
        throw new IllegalArgumentException("Usuario no encontrado");
    }

    public Comida crearComida(Comida comida) {
        if (comida.getUsuario() == null || comida.getUsuario().getId() == null) {
            throw new IllegalArgumentException("El usuario es requerido");
        }
        if (!usuarioRepository.existsById(comida.getUsuario().getId())) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        if (comida.getTipoDeComida() == null) {
            throw new IllegalArgumentException("El tipo de comida es requerido");
        }
        if (comida.getFecha() == null) {
            throw new IllegalArgumentException("La fecha es requerida");
        }
        return comidaRepository.save(comida);
    }

    public Comida updateComida(Long id, Comida comidaDetails) {
        Optional<Comida> comida = comidaRepository.findById(id);
        if (comida.isPresent()) {
            Comida existing = comida.get();
            if (comidaDetails.getTipoDeComida() != null) {
                existing.setTipoDeComida(comidaDetails.getTipoDeComida());
            }
            if (comidaDetails.getFecha() != null) {
                existing.setFecha(comidaDetails.getFecha());
            }
            return comidaRepository.save(existing);
        }
        throw new IllegalArgumentException("Comida no encontrada");
    }

    public void deleteComida(Long id) {
        if (!comidaRepository.existsById(id)) {
            throw new IllegalArgumentException("Comida no encontrada");
        }
        comidaRepository.deleteById(id);
    }

    public boolean existeComida(Long id) {
        return comidaRepository.existsById(id);
    }
}
