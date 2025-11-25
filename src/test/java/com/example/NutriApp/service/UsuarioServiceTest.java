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
        // --- CORRECCIÓN 1: Se inicializa el usuario con todos los campos ---
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setFullName("Juan Pérez");
        usuario.setUsername("juanperez");
        usuario.setEmail("juan@example.com");
        usuario.setPasswordHash("hashed123");
        usuario.setPeso(70.0);
        usuario.setMetaCalorias(2000);
        usuario.setMetaProteinas(150);
        usuario.setMetaCarbos(250);
        usuario.setMetaGrasas(70);
    }

    @Test
    public void testRegistrarUsuarioExitoso() {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername("nuevoUsuario");
        nuevoUsuario.setEmail("nuevo@example.com");

        when(usuarioRepository.findByUsername("nuevoUsuario")).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail("nuevo@example.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(nuevoUsuario);

        Usuario resultado = usuarioService.registrarUsuario(nuevoUsuario);

        assertNotNull(resultado);
        assertEquals("nuevoUsuario", resultado.getUsername());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void testRegistrarUsuarioDuplicado() {
        when(usuarioRepository.findByUsername("juanperez")).thenReturn(Optional.of(usuario));

        Usuario duploUsuario = new Usuario();
        duploUsuario.setUsername("juanperez");

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

    // --- CORRECCIÓN 2: Test de actualización mejorado ---
    @Test
    public void testUpdateUsuario() {
        // 1. Arrange: Preparamos los detalles de la actualización
        Usuario usuarioDetails = new Usuario();
        usuarioDetails.setFullName("Juan Updated");
        usuarioDetails.setMetaCalorias(2500);
        usuarioDetails.setPeso(72.5);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        // Hacemos que el mock de save devuelva el objeto que se le pasa
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. Act: Llamamos al método a probar
        Usuario resultado = usuarioService.updateUsuario(1L, usuarioDetails);

        // 3. Assert: Verificamos que los campos se actualizaron correctamente
        assertNotNull(resultado);
        assertEquals("Juan Updated", resultado.getFullName()); // Campo antiguo actualizado
        assertEquals(2500, resultado.getMetaCalorias());    // Campo nuevo actualizado
        assertEquals(72.5, resultado.getPeso());            // Campo nuevo actualizado
        assertEquals("juan@example.com", resultado.getEmail()); // Campo no actualizado, debe permanecer original
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void testDeleteUsuario() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.deleteUsuario(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}