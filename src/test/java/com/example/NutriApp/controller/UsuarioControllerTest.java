package com.example.NutriApp.controller;

import com.example.NutriApp.assembler.UsuarioAssembler;
import com.example.NutriApp.model.Usuario;
import com.example.NutriApp.dto.UsuarioDTO;
import com.example.NutriApp.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.hateoas.CollectionModel;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioAssembler usuarioAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        // El DTO que el assembler/controlador devolverá
        usuarioDTO = new UsuarioDTO(1L, "Juan Pérez", "juanperez", "juan@example.com");
    }

    @Test
    void testGetAllUsuarios() throws Exception {
        Usuario usuario = new Usuario(1L, "Juan Pérez", "juanperez", "juan@example.com", "hashed_password", null);
        when(usuarioService.getAllUsuarios()).thenReturn(Collections.singletonList(usuario)); // El servicio devuelve la entidad
        when(usuarioAssembler.toCollectionModel(any())).thenReturn(CollectionModel.of(Collections.singletonList(usuarioDTO)));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.usuarios[0].username").value("juanperez"));
    }

    @Test
    void testGetUsuarioById_NotFound() throws Exception {
        Long usuarioId = 99L;
        when(usuarioService.getUsuarioById(usuarioId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/{id}", usuarioId))
                .andExpect(status().isNotFound());
    }

    // Nota: Un test para getUsuarioById exitoso no se ha añadido porque
    // el controlador `UsuarioController` parece no tener un endpoint para ello.
    // Si lo añades, el test sería similar a `testGetAlimentoById` en `AlimentoControllerTest`.


    @Test
    void testRegistrarUsuario() throws Exception {
        // 1. Entidad de entrada (sin ID) que llega en el body
        Usuario usuarioParaRegistrar = new Usuario(null, "Juan Pérez", "juanperez", "juan@example.com", "hashed_password", null);

        // 2. Entidad de salida (con ID) que devuelve el servicio
        Usuario usuarioRegistrado = new Usuario(1L, "Juan Pérez", "juanperez", "juan@example.com", "hashed_password", null);

        when(usuarioService.registrarUsuario(any(Usuario.class))).thenReturn(usuarioRegistrado);
        when(usuarioAssembler.toModel(any(Usuario.class))).thenReturn(usuarioDTO);

        mockMvc.perform(post("/api/usuarios/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioParaRegistrar)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("juanperez"));
    }

    @Test
    void testDeleteUsuario() throws Exception {
        Long usuarioId = 1L;
        // El servicio no devuelve nada (void), así que usamos doNothing()
        doNothing().when(usuarioService).deleteUsuario(usuarioId);

        mockMvc.perform(delete("/api/usuarios/{id}", usuarioId))
                .andExpect(status().isNoContent());
    }

}
