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
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // --- CORRECCIÓN 1: El DTO ahora tiene más campos ---
        usuarioDTO = new UsuarioDTO(1L, "Juan Pérez", "juanperez", "juan@example.com", 70.5, 2000, 150, 250, 70);

        // --- CORRECCIÓN 2: La entidad Usuario ahora tiene más campos ---
        usuario = new Usuario(1L, "Juan Pérez", "juanperez", "juan@example.com", "hashed_password", 70.5, 2000, 150, 250, 70, null, null);
    }

    @Test
    void testGetAllUsuarios() throws Exception {
        when(usuarioService.getAllUsuarios()).thenReturn(Collections.singletonList(usuario));
        when(usuarioAssembler.toCollectionModel(any())).thenReturn(CollectionModel.of(Collections.singletonList(usuarioDTO)));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.usuarios[0].username").value("juanperez"));
    }

    @Test
    void testGetUsuarioById_Success() throws Exception {
        Long usuarioId = 1L;
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
        when(usuarioService.registrarUsuario(any(Usuario.class))).thenReturn(usuario);
        when(usuarioAssembler.toModel(any(Usuario.class))).thenReturn(usuarioDTO);

        mockMvc.perform(post("/api/usuarios/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("juanperez"));
    }

    @Test
    void testLoginUsuario_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("juanperez");
        loginRequest.setPasswordHash("hashed_password");

        when(usuarioService.loginUsuario("juanperez", "hashed_password")).thenReturn(Optional.of(usuario));
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
        UsuarioDTO usuarioActualizadoDTO = new UsuarioDTO(usuarioId, "Juan Pérez Actualizado", "juanperez", "juan.perez@example.com", 75.5, 2100, 160, 270, 75);
        
        // Creamos una entidad actualizada que el servicio devolvería
        Usuario usuarioActualizado = new Usuario(usuarioId, "Juan Pérez Actualizado", "juanperez", "juan.perez@example.com", "hashed_password", 75.5, 2100, 160, 270, 75, null, null);

        when(usuarioService.updateUsuario(any(Long.class), any(Usuario.class))).thenReturn(usuarioActualizado);
        when(usuarioAssembler.toModel(any(Usuario.class))).thenReturn(usuarioActualizadoDTO);

        mockMvc.perform(put("/api/usuarios/{id}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario))) // El body de la request puede ser la entidad original
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioId))
                .andExpect(jsonPath("$.fullName").value("Juan Pérez Actualizado"))
                .andExpect(jsonPath("$.email").value("juan.perez@example.com"));
    }

    @Test
    void testDeleteUsuario() throws Exception {
        Long usuarioId = 1L;
        doNothing().when(usuarioService).deleteUsuario(usuarioId);

        mockMvc.perform(delete("/api/usuarios/{id}", usuarioId))
                .andExpect(status().isNoContent());
    }
}
