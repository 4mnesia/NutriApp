package com.example.NutriApp.service;

import com.example.NutriApp.model.*;
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

    // --- NUEVO MOCK PARA EL SERVICIO DE REGISTRO DIARIO ---
    @Mock
    private RegistroDiarioService registroDiarioService;

    @InjectMocks
    private ComidaAlimentoService comidaAlimentoService;

    private Usuario usuario;
    private Comida comida;
    private Alimento alimento;
    private ComidaAlimento comidaAlimento;
    private RegistroDiario registroDiario;

    @BeforeEach
    void setUp() {
        // Configuración de los objetos de prueba
        usuario = new Usuario(1L, "Test User", "testuser", "test@test.com", "pass", 70.0, 2000, 150, 300, 80, null, null);
        comida = new Comida(1L, TipoDeComida.ALMUERZO, LocalDate.now(), usuario, null);
        alimento = new Alimento(1L, "Pollo", 165, 31f, 3.6f, 0f);
        comidaAlimento = new ComidaAlimento(1L, comida, alimento, 150);

        // Configuración del objeto RegistroDiario que se devolverá
        registroDiario = new RegistroDiario();
        registroDiario.setId(1L);
        registroDiario.setUsuario(usuario);
        registroDiario.setFecha(comida.getFecha());
        registroDiario.setTotalCalorias(1000); // Empezamos con valores iniciales
        registroDiario.setTotalProteinas(50);
        registroDiario.setTotalCarbos(100);
        registroDiario.setTotalGrasas(30);
    }

    // --- TEST MEJORADO: AHORA VERIFICA LA ACTUALIZACIÓN DEL REGISTRO DIARIO ---
    @Test
    void testAgregarAlimentoAComida() {
        // Arrange: Preparamos los mocks
        when(comidaRepository.findById(1L)).thenReturn(Optional.of(comida));
        when(alimentoRepository.findById(1L)).thenReturn(Optional.of(alimento));
        when(comidaAlimentoRepository.save(any(ComidaAlimento.class))).thenReturn(comidaAlimento);
        
        // Hacemos que el mock del servicio de registro devuelva nuestro objeto de prueba
        when(registroDiarioService.findOrCreateRegistroDiario(anyLong(), any(LocalDate.class))).thenReturn(registroDiario);

        ComidaAlimento ca = new ComidaAlimento(null, comida, alimento, 100); // Agregamos 100g de pollo

        // Act: Ejecutamos el método a probar
        ComidaAlimento resultado = comidaAlimentoService.agregarAlimentoAComida(ca);

        // Assert: Verificamos los resultados
        assertNotNull(resultado);
        
        // Verificamos que se llamó al servicio para buscar/crear el registro
        verify(registroDiarioService, times(1)).findOrCreateRegistroDiario(1L, comida.getFecha());

        // Verificamos que los totales se actualizaron correctamente
        // Calorías del pollo por 100g = 165. Total anterior = 1000. Nuevo total = 1165.
        assertEquals(1165, registroDiario.getTotalCalorias());
        // Proteínas del pollo por 100g = 31. Total anterior = 50. Nuevo total = 81.
        assertEquals(81, registroDiario.getTotalProteinas());
    }

    // --- TEST MEJORADO PARA BORRAR ---
    @Test
    void testDeleteComidaAlimento() {
        // Arrange
        when(comidaAlimentoRepository.findById(1L)).thenReturn(Optional.of(comidaAlimento)); // comidaAlimento tiene 150g
        when(registroDiarioService.findOrCreateRegistroDiario(anyLong(), any(LocalDate.class))).thenReturn(registroDiario);
        doNothing().when(comidaAlimentoRepository).delete(comidaAlimento);

        // Act
        comidaAlimentoService.deleteComidaAlimento(1L);

        // Assert
        verify(comidaAlimentoRepository, times(1)).delete(comidaAlimento);
        verify(registroDiarioService, times(1)).findOrCreateRegistroDiario(1L, comida.getFecha());

        // Verificamos que se restaron los macros correctamente
        // Calorías de 150g de pollo = 165 * 1.5 = 247.5. Total anterior = 1000. Nuevo total = 752.
        assertEquals(752, registroDiario.getTotalCalorias());
        // Proteínas de 150g de pollo = 31 * 1.5 = 46.5. Total anterior = 50. Nuevo total = 3.
        assertEquals(3, registroDiario.getTotalProteinas());
    }
}
