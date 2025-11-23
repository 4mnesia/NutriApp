package com.example.NutriApp.service;

import com.example.NutriApp.model.Usuario;
import com.example.NutriApp.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setFullName("Juan Pérez");
        usuario.setUsername("juanperez");
        usuario.setEmail("juan@example.com");
        usuario.setPasswordHash("hashed123");
    }

    @Test
    public void testRegistrarUsuarioExitoso() {
        when(usuarioRepository.findByUsername("nuevoUsuario")).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail("nuevo@example.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setFullName("Juan");
        nuevoUsuario.setUsername("nuevoUsuario");
        nuevoUsuario.setEmail("nuevo@example.com");
        nuevoUsuario.setPasswordHash("password123");

        Usuario resultado = usuarioService.registrarUsuario(nuevoUsuario);

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void testRegistrarUsuarioDuplicado() {
        when(usuarioRepository.findByUsername("juanperez")).thenReturn(Optional.of(usuario));

        Usuario duploUsuario = new Usuario();
        duploUsuario.setUsername("juanperez");
        duploUsuario.setEmail("nuevo@example.com");

        assertThrows(IllegalArgumentException.class, 
            () -> usuarioService.registrarUsuario(duploUsuario));
    }

    @Test
    public void testLoginExitoso() {
        when(usuarioRepository.findByUsernameOrEmail("juanperez", "juanperez"))
            .thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.loginUsuario("juanperez", "hashed123");

        assertTrue(resultado.isPresent());
        assertEquals("juanperez", resultado.get().getUsername());
    }

    @Test
    public void testLoginContraseniaIncorrecta() {
        when(usuarioRepository.findByUsernameOrEmail("juanperez", "juanperez"))
            .thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.loginUsuario("juanperez", "wrongPassword");

        assertFalse(resultado.isPresent());
    }

    @Test
    public void testGetUsuarioById() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.getUsuarioById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    public void testGetUsuarioByIdNoExiste() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.getUsuarioById(999L);

        assertFalse(resultado.isPresent());
    }

    @Test
    public void testUpdateUsuario() {
        Usuario usuarioDetails = new Usuario();
        usuarioDetails.setFullName("Juan Updated");
        usuarioDetails.setEmail("newemail@example.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmail("newemail@example.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.updateUsuario(1L, usuarioDetails);

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void testDeleteUsuario() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.deleteUsuario(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}

