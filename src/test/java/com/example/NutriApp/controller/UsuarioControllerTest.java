package com.example.NutriApp.controller;

import com.example.NutriApp.assembler.UsuarioAssembler;
import com.example.NutriApp.dto.LoginRequest;
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
    void testGetUsuarioById_Success() throws Exception {
        Long usuarioId = 1L;
        Usuario usuario = new Usuario(usuarioId, "Juan Pérez", "juanperez", "juan@example.com", "hashed_password", null);

        when(usuarioService.getUsuarioById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioAssembler.toModel(any(Usuario.class))).thenReturn(usuarioDTO);

        mockMvc.perform(get("/api/usuarios/{id}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json")) 
                .andExpect(jsonPath("$.id").value(usuarioId))
                .andExpect(jsonPath("$.username").value("juanperez"));
    }

    @Test
    void testGetUsuarioById_NotFound() throws Exception {
        Long usuarioId = 99L;
        when(usuarioService.getUsuarioById(usuarioId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/{id}", usuarioId))
                .andExpect(status().isNotFound());
    }


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
    void testLoginUsuario_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("juanperez");
        loginRequest.setPasswordHash("hashed_password");

        Usuario usuarioLogueado = new Usuario(1L, "Juan Pérez", "juanperez", "juan@example.com", "hashed_password", null);

        when(usuarioService.loginUsuario("juanperez", "hashed_password")).thenReturn(Optional.of(usuarioLogueado));
        when(usuarioAssembler.toModel(any(Usuario.class))).thenReturn(usuarioDTO);

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("juanperez"));
    }

    @Test
    void testLoginUsuario_InvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("juanperez");
        loginRequest.setPasswordHash("wrong_password");

        when(usuarioService.loginUsuario("juanperez", "wrong_password")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateUsuario_Success() throws Exception {
        Long usuarioId = 1L;
        Usuario usuarioDetails = new Usuario(null, "Juan Pérez Actualizado", "juanperez", "juan.perez@example.com", null, null);
        Usuario usuarioActualizado = new Usuario(usuarioId, "Juan Pérez Actualizado", "juanperez", "juan.perez@example.com", "hashed_password", null);
        UsuarioDTO usuarioActualizadoDTO = new UsuarioDTO(usuarioId, "Juan Pérez Actualizado", "juanperez", "juan.perez@example.com");

        when(usuarioService.updateUsuario(any(Long.class), any(Usuario.class))).thenReturn(usuarioActualizado);
        when(usuarioAssembler.toModel(any(Usuario.class))).thenReturn(usuarioActualizadoDTO);

        mockMvc.perform(put("/api/usuarios/{id}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioId))
                .andExpect(jsonPath("$.fullName").value("Juan Pérez Actualizado"))
                .andExpect(jsonPath("$.email").value("juan.perez@example.com"));
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
