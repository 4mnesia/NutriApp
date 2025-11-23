package com.example.NutriApp.controller;

import com.example.NutriApp.model.Usuario;
import com.example.NutriApp.service.UsuarioService;
import com.example.NutriApp.dto.UsuarioDTO;
import com.example.NutriApp.assembler.UsuarioAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "Endpoints para gestionar usuarios de NutriApp")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioAssembler usuarioAssembler;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista de todos los usuarios registrados con HATEOAS links")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    public ResponseEntity<CollectionModel<UsuarioDTO>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        CollectionModel<UsuarioDTO> usuariosDTO = usuarioAssembler.toCollectionModel(usuarios);
        return ResponseEntity.ok(usuariosDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Retorna un usuario específico según su ID con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UsuarioDTO> getUsuarioById(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        return usuario.map(u -> ResponseEntity.ok(usuarioAssembler.toModel(u))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario en el sistema y retorna DTO con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario duplicado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
            UsuarioDTO usuarioDTO = usuarioAssembler.toModel(nuevoUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar usuario");
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario con sus credenciales y retorna DTO con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> loginUsuario(
            @Parameter(description = "Usuario o email", example = "juanperez")
            @RequestParam String usernameOrEmail,
            @Parameter(description = "Hash de contraseña", example = "hashed_password")
            @RequestParam String passwordHash) {
        try {
            Optional<Usuario> usuario = usuarioService.loginUsuario(usernameOrEmail, passwordHash);
            if (usuario.isPresent()) {
                UsuarioDTO usuarioDTO = usuarioAssembler.toModel(usuario.get());
                return ResponseEntity.ok(usuarioDTO);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en login");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente y retorna DTO con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> updateUsuario(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id,
            @RequestBody Usuario usuarioDetails) {
        try {
            Usuario usuarioActualizado = usuarioService.updateUsuario(id, usuarioDetails);
            UsuarioDTO usuarioDTO = usuarioAssembler.toModel(usuarioActualizado);
            return ResponseEntity.ok(usuarioDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar usuario");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> deleteUsuario(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {
        try {
            usuarioService.deleteUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar usuario");
        }
    }
}
