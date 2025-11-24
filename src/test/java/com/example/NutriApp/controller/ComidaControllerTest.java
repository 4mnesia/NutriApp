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
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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
    private Usuario usuario;
    private LocalDate fecha;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        usuario = new Usuario();
        usuario.setId(1L);
        
        fecha = LocalDate.now();

        comida = new Comida(1L, TipoDeComida.DESAYUNO, fecha, usuario, null);
        comidaDTO = new ComidaDTO(1L, TipoDeComida.DESAYUNO, fecha, 1L);
    }
    
    @Test
    void testGetAllComidas() throws Exception {
        List<Comida> comidas = Collections.singletonList(comida);
        when(comidaService.getAllComidas()).thenReturn(comidas);
        when(comidaAssembler.toCollectionModel(comidas)).thenReturn(CollectionModel.of(Collections.singletonList(comidaDTO), Collections.emptyList()));

        mockMvc.perform(get("/api/comidas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.comidaDTOList[0].id").value(1L));
    }

    @Test
    void testGetComidaById() throws Exception {
        when(comidaService.getComidaById(1L)).thenReturn(Optional.of(comida));
        when(comidaAssembler.toModel(any(Comida.class))).thenReturn(comidaDTO);

        mockMvc.perform(get("/api/comidas/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tipoDeComida").value("DESAYUNO"));
    }

    @Test
    void testGetComidaById_NotFound() throws Exception {
        when(comidaService.getComidaById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/comidas/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetComidasByUsuario() throws Exception {
        List<Comida> comidas = Collections.singletonList(comida);
        when(comidaService.getComidasByUsuario(1L)).thenReturn(comidas);
        when(comidaAssembler.toCollectionModel(comidas)).thenReturn(CollectionModel.of(Collections.singletonList(comidaDTO), Collections.emptyList()));

        mockMvc.perform(get("/api/comidas/usuario/{usuarioId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.comidaDTOList[0].usuarioId").value(1L));
    }

    @Test
    void testGetComidasByUsuarioAndFecha() throws Exception {
        List<Comida> comidas = Collections.singletonList(comida);
        when(comidaService.getComidasByUsuarioAndFecha(1L, fecha)).thenReturn(comidas);
        when(comidaAssembler.toCollectionModel(comidas)).thenReturn(CollectionModel.of(Collections.singletonList(comidaDTO), Collections.emptyList()));

        mockMvc.perform(get("/api/comidas/usuario/{usuarioId}/fecha/{fecha}", 1L, fecha.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.comidaDTOList[0].fecha").value(fecha.toString()));
    }
    
    @Test
    void testGetComidasByUsuarioAndTipo() throws Exception {
        List<Comida> comidas = Collections.singletonList(comida);
        String tipoAsString = "DESAYUNO";
        when(comidaService.getComidasByUsuarioAndTipo(1L, tipoAsString)).thenReturn(comidas);
        when(comidaAssembler.toCollectionModel(comidas)).thenReturn(CollectionModel.of(Collections.singletonList(comidaDTO), Collections.emptyList()));

        mockMvc.perform(get("/api/comidas/usuario/{usuarioId}/tipo/{tipoDeComida}", 1L, tipoAsString))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.comidaDTOList[0].tipoDeComida").value(tipoAsString));
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
    void testUpdateComida() throws Exception {
        ComidaDTO updatedDto = new ComidaDTO(1L, TipoDeComida.ALMUERZO, fecha, 1L);
        Comida updatedComida = new Comida(1L, TipoDeComida.ALMUERZO, fecha, usuario, null);

        when(comidaService.updateComida(any(Long.class), any(Comida.class))).thenReturn(updatedComida);
        when(comidaAssembler.toModel(any(Comida.class))).thenReturn(updatedDto);

        mockMvc.perform(put("/api/comidas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Comida(null, TipoDeComida.ALMUERZO, fecha, usuario, null))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoDeComida").value("ALMUERZO"));
    }

    @Test
    void testDeleteComida() throws Exception {
        doNothing().when(comidaService).deleteComida(1L);

        mockMvc.perform(delete("/api/comidas/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
