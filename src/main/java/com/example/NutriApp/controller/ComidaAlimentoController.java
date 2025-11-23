package com.example.NutriApp.controller;

import com.example.NutriApp.model.ComidaAlimento;
import com.example.NutriApp.service.ComidaAlimentoService;
import com.example.NutriApp.dto.ComidaAlimentoDTO;
import com.example.NutriApp.assembler.ComidaAlimentoAssembler;
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
@RequestMapping("/api/comida-alimentos")
@CrossOrigin(origins = "*")
@Tag(name = "Comida-Alimentos", description = "Endpoints para gestionar los alimentos dentro de las comidas")
public class ComidaAlimentoController {

    @Autowired
    private ComidaAlimentoService comidaAlimentoService;

    @Autowired
    private ComidaAlimentoAssembler comidaAlimentoAssembler;

    @GetMapping
    @Operation(summary = "Obtener todos los alimentos en comidas", description = "Retorna la lista de todas las relaciones comida-alimento con HATEOAS links")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    public ResponseEntity<CollectionModel<ComidaAlimentoDTO>> getAllComidaAlimentos() {
        List<ComidaAlimento> comidaAlimentos = comidaAlimentoService.getAllComidaAlimentos();
        CollectionModel<ComidaAlimentoDTO> comidaAlimentosDTO = comidaAlimentoAssembler.toCollectionModel(comidaAlimentos);
        return ResponseEntity.ok(comidaAlimentosDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener comida-alimento por ID", description = "Retorna una relación comida-alimento específica con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relación encontrada"),
        @ApiResponse(responseCode = "404", description = "Relación no encontrada")
    })
    public ResponseEntity<ComidaAlimentoDTO> getComidaAlimentoById(
            @Parameter(description = "ID de la relación comida-alimento", example = "1")
            @PathVariable Long id) {
        Optional<ComidaAlimento> comidaAlimento = comidaAlimentoService.getComidaAlimentoById(id);
        return comidaAlimento.map(ca -> ResponseEntity.ok(comidaAlimentoAssembler.toModel(ca))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/comida/{comidaId}")
    @Operation(summary = "Obtener alimentos de una comida", description = "Retorna todos los alimentos que componen una comida específica con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Alimentos encontrados"),
        @ApiResponse(responseCode = "404", description = "Comida no encontrada")
    })
    public ResponseEntity<?> getAlimentosByComida(
            @Parameter(description = "ID de la comida", example = "1")
            @PathVariable Long comidaId) {
        try {
            List<ComidaAlimento> alimentos = comidaAlimentoService.getAlimentosByComida(comidaId);
            CollectionModel<ComidaAlimentoDTO> alimentosDTO = comidaAlimentoAssembler.toCollectionModel(alimentos);
            return ResponseEntity.ok(alimentosDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Agregar alimento a comida", description = "Añade un alimento con cantidad específica a una comida y retorna DTO con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Alimento añadido exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> agregarAlimentoAComida(@RequestBody ComidaAlimento comidaAlimento) {
        try {
            ComidaAlimento nuevo = comidaAlimentoService.agregarAlimentoAComida(comidaAlimento);
            ComidaAlimentoDTO comidaAlimentoDTO = comidaAlimentoAssembler.toModel(nuevo);
            return ResponseEntity.status(HttpStatus.CREATED).body(comidaAlimentoDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al agregar alimento a comida");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cantidad de alimento en comida", description = "Modifica la cantidad de un alimento en una comida y retorna DTO con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Actualización exitosa"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Relación no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> updateComidaAlimento(
            @Parameter(description = "ID de la relación comida-alimento", example = "1")
            @PathVariable Long id,
            @RequestBody ComidaAlimento comidaAlimentoDetails) {
        try {
            ComidaAlimento actualizado = comidaAlimentoService.updateComidaAlimento(id, comidaAlimentoDetails);
            ComidaAlimentoDTO comidaAlimentoDTO = comidaAlimentoAssembler.toModel(actualizado);
            return ResponseEntity.ok(comidaAlimentoDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar comida alimento");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar alimento de comida", description = "Remueve un alimento específico de una comida")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Alimento eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Relación no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> deleteComidaAlimento(
            @Parameter(description = "ID de la relación comida-alimento", example = "1")
            @PathVariable Long id) {
        try {
            comidaAlimentoService.deleteComidaAlimento(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar comida alimento");
        }
    }

    @DeleteMapping("/comida/{comidaId}")
    @Operation(summary = "Eliminar todos los alimentos de una comida", description = "Remueve todos los alimentos asociados a una comida")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Alimentos eliminados exitosamente"),
        @ApiResponse(responseCode = "404", description = "Comida no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> deleteAlimentosDeComida(
            @Parameter(description = "ID de la comida", example = "1")
            @PathVariable Long comidaId) {
        try {
            comidaAlimentoService.deleteAlimentosDeComida(comidaId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar alimentos de comida");
        }
    }
}
