package com.example.NutriApp.service;

import com.example.NutriApp.model.Alimento;
import com.example.NutriApp.repository.AlimentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlimentoServiceTest {

    @Mock
    private AlimentoRepository alimentoRepository;

    @InjectMocks
    private AlimentoService alimentoService;

    private Alimento alimento;

    @BeforeEach
    public void setUp() {
        alimento = new Alimento();
        alimento.setId(1L);
        alimento.setNombre("Pollo");
        alimento.setCaloriasPor100g(165);
        alimento.setProteinasPor100g(31.0f);
        alimento.setCarbosPor100g(0.0f);
        alimento.setGrasasPor100g(3.6f);
    }

    @Test
    public void testCrearAlimentoExitoso() {
        when(alimentoRepository.save(any(Alimento.class))).thenReturn(alimento);

        Alimento resultado = alimentoService.crearAlimento(alimento);

        assertNotNull(resultado);
        assertEquals("Pollo", resultado.getNombre());
        verify(alimentoRepository, times(1)).save(any(Alimento.class));
    }

    @Test
    public void testCrearAlimentoValoresNegativosInvalidos() {
        Alimento alimentoInvalido = new Alimento();
        alimentoInvalido.setNombre("Comida");
        alimentoInvalido.setCaloriasPor100g(-100);

        assertThrows(IllegalArgumentException.class,
            () -> alimentoService.crearAlimento(alimentoInvalido));
    }

    @Test
    public void testBuscarAlimentosPorNombre() {
        List<Alimento> alimentos = Arrays.asList(alimento);
        when(alimentoRepository.findByNombreContainingIgnoreCase("Pollo")).thenReturn(alimentos);

        List<Alimento> resultado = alimentoService.buscarAlimentos("Pollo");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testGetAlimentoById() {
        when(alimentoRepository.findById(1L)).thenReturn(Optional.of(alimento));

        Optional<Alimento> resultado = alimentoService.getAlimentoById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    public void testUpdateAlimento() {
        Alimento alimentoDetails = new Alimento();
        alimentoDetails.setNombre("Pollo");
        alimentoDetails.setCaloriasPor100g(170);
        alimentoDetails.setProteinasPor100g(32.0f);
        alimentoDetails.setCarbosPor100g(0.0f);
        alimentoDetails.setGrasasPor100g(4.0f);

        when(alimentoRepository.findById(1L)).thenReturn(Optional.of(alimento));
        when(alimentoRepository.save(any(Alimento.class))).thenReturn(alimento);

        Alimento resultado = alimentoService.updateAlimento(1L, alimentoDetails);

        assertNotNull(resultado);
        verify(alimentoRepository, times(1)).save(any(Alimento.class));
    }

    @Test
    public void testDeleteAlimento() {
        when(alimentoRepository.existsById(1L)).thenReturn(true);

        alimentoService.deleteAlimento(1L);

        verify(alimentoRepository, times(1)).deleteById(1L);
    }
}
