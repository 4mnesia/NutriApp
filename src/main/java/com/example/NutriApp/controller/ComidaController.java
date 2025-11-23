package com.example.NutriApp.controller;

import com.example.NutriApp.model.Comida;
import com.example.NutriApp.service.ComidaService;
import com.example.NutriApp.dto.ComidaDTO;
import com.example.NutriApp.assembler.ComidaAssembler;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comidas")
@CrossOrigin(origins = "*")
@Tag(name = "Comidas", description = "Endpoints para gestionar comidas y su contenido nutricional")
public class ComidaController {

    @Autowired
    private ComidaService comidaService;

    @Autowired
    private ComidaAssembler comidaAssembler;

    @GetMapping
    @Operation(summary = "Obtener todas las comidas", description = "Retorna la lista de todas las comidas registradas con HATEOAS links")
    @ApiResponse(responseCode = "200", description = "Lista de comidas obtenida exitosamente")
    public ResponseEntity<CollectionModel<ComidaDTO>> getAllComidas() {
        List<Comida> comidas = comidaService.getAllComidas();
        CollectionModel<ComidaDTO> comidasDTO = comidaAssembler.toCollectionModel(comidas);
        return ResponseEntity.ok(comidasDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener comida por ID", description = "Retorna una comida específica según su ID con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comida encontrada"),
        @ApiResponse(responseCode = "404", description = "Comida no encontrada")
    })
    public ResponseEntity<ComidaDTO> getComidaById(
            @Parameter(description = "ID de la comida", example = "1")
            @PathVariable Long id) {
        Optional<Comida> comida = comidaService.getComidaById(id);
        return comida.map(c -> ResponseEntity.ok(comidaAssembler.toModel(c))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener comidas de un usuario", description = "Retorna todas las comidas de un usuario específico con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comidas encontradas"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> getComidasByUsuario(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long usuarioId) {
        try {
            List<Comida> comidas = comidaService.getComidasByUsuario(usuarioId);
            CollectionModel<ComidaDTO> comidasDTO = comidaAssembler.toCollectionModel(comidas);
            return ResponseEntity.ok(comidasDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}/fecha/{fecha}")
    @Operation(summary = "Obtener comidas de un usuario en una fecha", description = "Retorna todas las comidas de un usuario en una fecha específica con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comidas encontradas"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> getComidasByUsuarioAndFecha(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long usuarioId,
            @Parameter(description = "Fecha en formato YYYY-MM-DD", example = "2025-11-23")
            @PathVariable LocalDate fecha) {
        try {
            List<Comida> comidas = comidaService.getComidasByUsuarioAndFecha(usuarioId, fecha);
            CollectionModel<ComidaDTO> comidasDTO = comidaAssembler.toCollectionModel(comidas);
            return ResponseEntity.ok(comidasDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}/tipo/{tipoDeComida}")
    @Operation(summary = "Obtener comidas de un usuario por tipo", description = "Retorna todas las comidas de un usuario de un tipo específico con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comidas encontradas"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> getComidasByUsuarioAndTipo(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long usuarioId,
            @Parameter(description = "Tipo de comida", example = "Desayuno")
            @PathVariable String tipoDeComida) {
        try {
            List<Comida> comidas = comidaService.getComidasByUsuarioAndTipo(usuarioId, tipoDeComida);
            CollectionModel<ComidaDTO> comidasDTO = comidaAssembler.toCollectionModel(comidas);
            return ResponseEntity.ok(comidasDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear nueva comida", description = "Registra una nueva comida para un usuario y retorna DTO con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Comida creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> crearComida(@RequestBody Comida comida) {
        try {
            Comida nuevaComida = comidaService.crearComida(comida);
            ComidaDTO comidaDTO = comidaAssembler.toModel(nuevaComida);
            return ResponseEntity.status(HttpStatus.CREATED).body(comidaDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear comida");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar comida", description = "Actualiza los datos de una comida existente y retorna DTO con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comida actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Comida no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> updateComida(
            @Parameter(description = "ID de la comida", example = "1")
            @PathVariable Long id,
            @RequestBody Comida comidaDetails) {
        try {
            Comida actualizada = comidaService.updateComida(id, comidaDetails);
            ComidaDTO comidaDTO = comidaAssembler.toModel(actualizada);
            return ResponseEntity.ok(comidaDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar comida");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar comida", description = "Elimina una comida del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Comida eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Comida no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> deleteComida(
            @Parameter(description = "ID de la comida", example = "1")
            @PathVariable Long id) {
        try {
            comidaService.deleteComida(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar comida");
        }
    }
}
