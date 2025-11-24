package com.example.NutriApp.service;

import com.example.NutriApp.model.Alimento;
import com.example.NutriApp.model.Comida;
import com.example.NutriApp.model.ComidaAlimento;
import com.example.NutriApp.model.TipoDeComida;
import com.example.NutriApp.repository.AlimentoRepository;
import com.example.NutriApp.repository.ComidaAlimentoRepository;
import com.example.NutriApp.repository.ComidaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComidaAlimentoServiceTest {

    @Mock
    private ComidaAlimentoRepository comidaAlimentoRepository;

    @Mock
    private ComidaRepository comidaRepository;

    @Mock
    private AlimentoRepository alimentoRepository;

    @InjectMocks
    private ComidaAlimentoService comidaAlimentoService;

    private Comida comida;
    private Alimento alimento;
    private ComidaAlimento comidaAlimento;

    @BeforeEach
    void setUp() {
        comida = new Comida();
        comida.setId(1L);
        comida.setTipoDeComida(TipoDeComida.ALMUERZO);
        comida.setFecha(LocalDate.now());

        alimento = new Alimento();
        alimento.setId(1L);
        alimento.setNombre("Pollo");

        comidaAlimento = new ComidaAlimento(1L, comida, alimento, 150);
    }

    @Test
    void testGetAllComidaAlimentos() {
        when(comidaAlimentoRepository.findAll()).thenReturn(Collections.singletonList(comidaAlimento));
        List<ComidaAlimento> result = comidaAlimentoService.getAllComidaAlimentos();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(comidaAlimentoRepository, times(1)).findAll();
    }

    @Test
    void testGetComidaAlimentoById() {
        when(comidaAlimentoRepository.findById(1L)).thenReturn(Optional.of(comidaAlimento));
        Optional<ComidaAlimento> result = comidaAlimentoService.getComidaAlimentoById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetAlimentosByComida_Success() {
        when(comidaRepository.findById(1L)).thenReturn(Optional.of(comida));
        when(comidaAlimentoRepository.findByComida(comida)).thenReturn(Collections.singletonList(comidaAlimento));
        
        List<ComidaAlimento> result = comidaAlimentoService.getAlimentosByComida(1L);
        
        assertFalse(result.isEmpty());
        assertEquals(150, result.get(0).getCantidadEnGramos());
    }

    @Test
    void testGetAlimentosByComida_NotFound() {
        when(comidaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> comidaAlimentoService.getAlimentosByComida(99L));
    }

    @Test
    void testAgregarAlimentoAComida_Success() {
        when(comidaRepository.existsById(1L)).thenReturn(true);
        when(alimentoRepository.existsById(1L)).thenReturn(true);
        when(comidaAlimentoRepository.save(any(ComidaAlimento.class))).thenReturn(comidaAlimento);

        ComidaAlimento newComidaAlimento = new ComidaAlimento(null, comida, alimento, 150);
        ComidaAlimento result = comidaAlimentoService.agregarAlimentoAComida(newComidaAlimento);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(comidaAlimentoRepository, times(1)).save(any(ComidaAlimento.class));
    }
    
    @Test
    void testAgregarAlimentoAComida_ComidaNotFound() {
        ComidaAlimento newComidaAlimento = new ComidaAlimento(null, comida, alimento, 150);
        when(comidaRepository.existsById(1L)).thenReturn(false);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            comidaAlimentoService.agregarAlimentoAComida(newComidaAlimento);
        });
        
        assertEquals("Comida no encontrada", exception.getMessage());
    }

    @Test
    void testUpdateComidaAlimento_Success() {
        ComidaAlimento details = new ComidaAlimento(null, null, null, 200);
        when(comidaAlimentoRepository.findById(1L)).thenReturn(Optional.of(comidaAlimento));
        when(comidaAlimentoRepository.save(any(ComidaAlimento.class))).thenReturn(comidaAlimento);

        ComidaAlimento result = comidaAlimentoService.updateComidaAlimento(1L, details);

        assertNotNull(result);
        assertEquals(200, result.getCantidadEnGramos());
    }

    @Test
    void testUpdateComidaAlimento_NotFound() {
        ComidaAlimento details = new ComidaAlimento(null, null, null, 200);
        when(comidaAlimentoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> comidaAlimentoService.updateComidaAlimento(99L, details));
    }

    @Test
    void testDeleteComidaAlimento_Success() {
        when(comidaAlimentoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(comidaAlimentoRepository).deleteById(1L);
        
        comidaAlimentoService.deleteComidaAlimento(1L);
        
        verify(comidaAlimentoRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void testDeleteComidaAlimento_NotFound() {
        when(comidaAlimentoRepository.existsById(99L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> comidaAlimentoService.deleteComidaAlimento(99L));
    }

    @Test
    void testDeleteAlimentosDeComida() {
        when(comidaRepository.findById(1L)).thenReturn(Optional.of(comida));
        List<ComidaAlimento> listToDelete = Collections.singletonList(comidaAlimento);
        when(comidaAlimentoRepository.findByComida(comida)).thenReturn(listToDelete);

        comidaAlimentoService.deleteAlimentosDeComida(1L);

        verify(comidaAlimentoRepository, times(1)).deleteAll(listToDelete);
    }
}
