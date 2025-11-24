package com.example.NutriApp.controller;

import com.example.NutriApp.assembler.AlimentoAssembler;
import com.example.NutriApp.model.Alimento;
import com.example.NutriApp.dto.AlimentoDTO;
import com.example.NutriApp.service.AlimentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlimentoController.class)
public class AlimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlimentoService alimentoService;

    @MockBean
    private AlimentoAssembler alimentoAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Alimento alimento;
    private AlimentoDTO alimentoDTO;

    @BeforeEach
    void setUp() {
        // Entidad que devolvería el servicio
        alimento = new Alimento(1L, "Pollo", 165, 31.0f, 0.0f, 3.6f);
        // DTO que devolvería el assembler/controlador
        alimentoDTO = new AlimentoDTO(1L, "Pollo", 165, 31.0f, 0.0f, 3.6f);
    }

    @Test
    void testGetAlimentoById() throws Exception {
        // El servicio devuelve un Optional<Alimento>
        when(alimentoService.getAlimentoById(1L)).thenReturn(Optional.of(alimento));
        // El assembler convierte la entidad Alimento a AlimentoDTO
        when(alimentoAssembler.toModel(any(Alimento.class))).thenReturn(alimentoDTO);

        mockMvc.perform(get("/api/alimentos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.nombre").value("Pollo"));
    }

    @Test
    void testGetAlimentoById_NotFound() throws Exception {
        Long alimentoId = 99L;
        when(alimentoService.getAlimentoById(alimentoId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/alimentos/{id}", alimentoId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrearAlimento() throws Exception {
        Alimento alimentoSinId = new Alimento(null, "Huevo", 78, 6.0f, 0.6f, 5.0f);
        Alimento alimentoCreado = new Alimento(2L, "Huevo", 78, 6.0f, 0.6f, 5.0f);
        AlimentoDTO alimentoCreadoDTO = new AlimentoDTO(2L, "Huevo", 78, 6.0f, 0.6f, 5.0f);

        when(alimentoService.crearAlimento(any(Alimento.class))).thenReturn(alimentoCreado);
        when(alimentoAssembler.toModel(any(Alimento.class))).thenReturn(alimentoCreadoDTO);

        mockMvc.perform(post("/api/alimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alimentoSinId)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.nombre").value("Huevo"));
    }

    @Test
    void testDeleteAlimento() throws Exception {
        Long alimentoId = 1L;
        doNothing().when(alimentoService).deleteAlimento(alimentoId);

        mockMvc.perform(delete("/api/alimentos/{id}", alimentoId))
                .andExpect(status().isNoContent());
    }

}
