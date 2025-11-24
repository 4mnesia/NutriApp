package com.example.NutriApp.controller;

import com.example.NutriApp.assembler.ComidaAlimentoAssembler;
import com.example.NutriApp.dto.ComidaAlimentoDTO;
import com.example.NutriApp.model.*;
import com.example.NutriApp.service.ComidaAlimentoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ComidaAlimentoController.class)
public class ComidaAlimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComidaAlimentoService comidaAlimentoService;

    @MockBean
    private ComidaAlimentoAssembler comidaAlimentoAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private ComidaAlimento comidaAlimento;
    private ComidaAlimentoDTO comidaAlimentoDTO;
    private Comida comida;
    private Alimento alimento;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        Usuario usuario = new Usuario(1L, "Test User", "testuser", "test@example.com", "password", null);
        comida = new Comida(1L, TipoDeComida.ALMUERZO, LocalDate.now(), usuario, null);
        alimento = new Alimento(1L, "Pollo", 165, 31.0f, 0.0f, 3.6f);
        
        comidaAlimento = new ComidaAlimento(1L, comida, alimento, 150);
        comidaAlimentoDTO = new ComidaAlimentoDTO(1L, 1L, 1L, 150);
    }

    @Test
    void testGetAllComidaAlimentos() throws Exception {
        when(comidaAlimentoService.getAllComidaAlimentos()).thenReturn(Collections.singletonList(comidaAlimento));
        when(comidaAlimentoAssembler.toCollectionModel(any())).thenReturn(CollectionModel.of(Collections.singletonList(comidaAlimentoDTO)));

        mockMvc.perform(get("/api/comida-alimentos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.comidaAlimentoDTOList[0].id").value(1L));
    }

    @Test
    void testGetComidaAlimentoById_Success() throws Exception {
        when(comidaAlimentoService.getComidaAlimentoById(1L)).thenReturn(Optional.of(comidaAlimento));
        when(comidaAlimentoAssembler.toModel(any(ComidaAlimento.class))).thenReturn(comidaAlimentoDTO);

        mockMvc.perform(get("/api/comida-alimentos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cantidadEnGramos").value(150));
    }

    @Test
    void testGetComidaAlimentoById_NotFound() throws Exception {
        when(comidaAlimentoService.getComidaAlimentoById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/comida-alimentos/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAlimentosByComida_Success() throws Exception {
        when(comidaAlimentoService.getAlimentosByComida(1L)).thenReturn(Collections.singletonList(comidaAlimento));
        when(comidaAlimentoAssembler.toCollectionModel(any())).thenReturn(CollectionModel.of(Collections.singletonList(comidaAlimentoDTO)));

        mockMvc.perform(get("/api/comida-alimentos/comida/{comidaId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.comidaAlimentoDTOList[0].id").value(1L));
    }

    @Test
    void testAgregarAlimentoAComida_Success() throws Exception {
        when(comidaAlimentoService.agregarAlimentoAComida(any(ComidaAlimento.class))).thenReturn(comidaAlimento);
        when(comidaAlimentoAssembler.toModel(any(ComidaAlimento.class))).thenReturn(comidaAlimentoDTO);

        ComidaAlimento requestBody = new ComidaAlimento(null, comida, alimento, 150);

        mockMvc.perform(post("/api/comida-alimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.alimentoId").value(1L));
    }

    @Test
    void testUpdateComidaAlimento_Success() throws Exception {
        ComidaAlimentoDTO updatedDto = new ComidaAlimentoDTO(1L, 1L, 1L, 200);
        ComidaAlimento updatedEntity = new ComidaAlimento(1L, comida, alimento, 200);

        when(comidaAlimentoService.updateComidaAlimento(any(Long.class), any(ComidaAlimento.class))).thenReturn(updatedEntity);
        when(comidaAlimentoAssembler.toModel(any(ComidaAlimento.class))).thenReturn(updatedDto); 

        ComidaAlimento requestBody = new ComidaAlimento(null, comida, alimento, 200);

        mockMvc.perform(put("/api/comida-alimentos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidadEnGramos").value(200));
    }

    @Test
    void testDeleteComidaAlimento_Success() throws Exception {
        doNothing().when(comidaAlimentoService).deleteComidaAlimento(1L);

        mockMvc.perform(delete("/api/comida-alimentos/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteAlimentosDeComida_Success() throws Exception {
        Long comidaId = 1L;
        doNothing().when(comidaAlimentoService).deleteAlimentosDeComida(comidaId);
        
        mockMvc.perform(delete("/api/comida-alimentos/comida/{comidaId}", comidaId))
                .andExpect(status().isNoContent());
    }
}
