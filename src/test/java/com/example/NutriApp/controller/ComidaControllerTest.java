package com.example.NutriApp.controller;

import com.example.NutriApp.assembler.ComidaAssembler;
import com.example.NutriApp.dto.ComidaDTO;
import com.example.NutriApp.model.Comida;
import com.example.NutriApp.model.TipoDeComida;
import com.example.NutriApp.model.Usuario;
import com.example.NutriApp.service.ComidaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ComidaController.class)
public class ComidaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ComidaService comidaService;

    @MockBean
    private ComidaAssembler comidaAssembler;

    private Comida comida;
    private ComidaDTO comidaDTO;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        comida = new Comida(1L, TipoDeComida.ALMUERZO, LocalDate.now(), usuario, null);
        comidaDTO = new ComidaDTO(1L, TipoDeComida.ALMUERZO, LocalDate.now(), 1L);
    }

    @Test
    void testGetComidaById() throws Exception {
        when(comidaService.getComidaById(1L)).thenReturn(Optional.of(comida));
        when(comidaAssembler.toModel(any(Comida.class))).thenReturn(comidaDTO);

        mockMvc.perform(get("/api/comidas/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tipoDeComida").value("ALMUERZO"));
    }

    @Test
    void testGetComidaById_NotFound() throws Exception {
        Long comidaId = 99L;
        when(comidaService.getComidaById(comidaId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/comidas/{id}", comidaId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrearComida() throws Exception {
        when(comidaService.crearComida(any(Comida.class))).thenReturn(comida);
        when(comidaAssembler.toModel(any(Comida.class))).thenReturn(comidaDTO);

        mockMvc.perform(post("/api/comidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comida)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testDeleteComida() throws Exception {
        doNothing().when(comidaService).deleteComida(1L);

        mockMvc.perform(delete("/api/comidas/1"))
                .andExpect(status().isNoContent());
    }
}
