package com.example.NutriApp.service;

import com.example.NutriApp.model.Comida;
import com.example.NutriApp.model.Usuario;
import com.example.NutriApp.model.TipoDeComida;
import com.example.NutriApp.repository.ComidaRepository;
import com.example.NutriApp.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComidaServiceTest {

    @Mock
    private ComidaRepository comidaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ComidaService comidaService;

    private Usuario usuario;
    private Comida comida;

    @BeforeEach
    public void setUp() {
        // --- CORRECCIÓN: Inicializar el objeto Usuario completamente ---
        usuario = new Usuario(1L, "Juan Pérez", "juanperez", "juan@example.com", "hashed123", 70.0, 2000, 150, 250, 70, null, null);

        comida = new Comida();
        comida.setId(1L);
        comida.setFecha(LocalDate.now());
        comida.setTipoDeComida(TipoDeComida.DESAYUNO);
        comida.setUsuario(usuario);
    }

    @Test
    public void testCrearComidaExitosa() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(comidaRepository.save(any(Comida.class))).thenReturn(comida);

        Comida resultado = comidaService.crearComida(comida);

        assertNotNull(resultado);
        assertEquals(TipoDeComida.DESAYUNO, resultado.getTipoDeComida());
        verify(comidaRepository, times(1)).save(any(Comida.class));
    }

    @Test
    public void testCrearComidaUsuarioNoExiste() {
        Comida comidaInvalida = new Comida();
        // Usar el constructor completo para el usuario inválido también
        Usuario usuarioInvalido = new Usuario(999L, "Invalid", "invalid", "invalid@test.com", "pass", 0.0, 0, 0, 0, 0, null, null);
        comidaInvalida.setUsuario(usuarioInvalido);
        comidaInvalida.setTipoDeComida(TipoDeComida.DESAYUNO);
        comidaInvalida.setFecha(LocalDate.now());

        when(usuarioRepository.existsById(999L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
            () -> comidaService.crearComida(comidaInvalida));
    }

    @Test
    public void testGetComidasByUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(comidaRepository.findByUsuario(usuario)).thenReturn(Arrays.asList(comida));

        List<Comida> resultado = comidaService.getComidasByUsuario(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testGetComidasByUsuarioAndFecha() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(comidaRepository.findByUsuarioAndFecha(usuario, LocalDate.now())).thenReturn(Arrays.asList(comida));

        List<Comida> resultado = comidaService.getComidasByUsuarioAndFecha(1L, LocalDate.now());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testGetComidaById() {
        when(comidaRepository.findById(1L)).thenReturn(Optional.of(comida));

        Optional<Comida> resultado = comidaService.getComidaById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    public void testUpdateComida() {
        Comida comidaDetails = new Comida();
        comidaDetails.setTipoDeComida(TipoDeComida.ALMUERZO);

        when(comidaRepository.findById(1L)).thenReturn(Optional.of(comida));
        when(comidaRepository.save(any(Comida.class))).thenReturn(comida);

        Comida resultado = comidaService.updateComida(1L, comidaDetails);

        assertNotNull(resultado);
        verify(comidaRepository, times(1)).save(any(Comida.class));
    }

    @Test
    public void testDeleteComida() {
        when(comidaRepository.existsById(1L)).thenReturn(true);

        comidaService.deleteComida(1L);

        verify(comidaRepository, times(1)).deleteById(1L);
    }
}
