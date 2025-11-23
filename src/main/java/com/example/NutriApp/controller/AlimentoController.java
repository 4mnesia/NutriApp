package com.example.NutriApp.controller;

import com.example.NutriApp.model.Alimento;
import com.example.NutriApp.service.AlimentoService;
import com.example.NutriApp.dto.AlimentoDTO;
import com.example.NutriApp.assembler.AlimentoAssembler;
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
@RequestMapping("/api/alimentos")
@CrossOrigin(origins = "*")
@Tag(name = "Alimentos", description = "Endpoints para gestionar alimentos y su información nutricional")
public class AlimentoController {

    @Autowired
    private AlimentoService alimentoService;

    @Autowired
    private AlimentoAssembler alimentoAssembler;

    @GetMapping
    @Operation(summary = "Obtener todos los alimentos", description = "Retorna la lista de todos los alimentos disponibles con HATEOAS links")
    @ApiResponse(responseCode = "200", description = "Lista de alimentos obtenida exitosamente")
    public ResponseEntity<CollectionModel<AlimentoDTO>> getAllAlimentos() {
        List<Alimento> alimentos = alimentoService.getAllAlimentos();
        CollectionModel<AlimentoDTO> alimentosDTO = alimentoAssembler.toCollectionModel(alimentos);
        return ResponseEntity.ok(alimentosDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener alimento por ID", description = "Retorna un alimento específico según su ID con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Alimento encontrado"),
        @ApiResponse(responseCode = "404", description = "Alimento no encontrado")
    })
    public ResponseEntity<AlimentoDTO> getAlimentoById(
            @Parameter(description = "ID del alimento", example = "1")
            @PathVariable Long id) {
        Optional<Alimento> alimento = alimentoService.getAlimentoById(id);
        return alimento.map(a -> ResponseEntity.ok(alimentoAssembler.toModel(a))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar alimentos por nombre", description = "Realiza una búsqueda de alimentos que contengan el nombre especificado (case-insensitive) con HATEOAS links")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    public ResponseEntity<CollectionModel<AlimentoDTO>> buscarAlimentos(
            @Parameter(description = "Nombre o parte del nombre a buscar", example = "Pollo")
            @RequestParam String nombre) {
        List<Alimento> alimentos = alimentoService.buscarAlimentos(nombre);
        CollectionModel<AlimentoDTO> alimentosDTO = alimentoAssembler.toCollectionModel(alimentos);
        return ResponseEntity.ok(alimentosDTO);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo alimento", description = "Añade un nuevo alimento a la base de datos con su información nutricional y retorna DTO con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Alimento creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos nutricionales inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> crearAlimento(@RequestBody Alimento alimento) {
        try {
            Alimento nuevoAlimento = alimentoService.crearAlimento(alimento);
            AlimentoDTO alimentoDTO = alimentoAssembler.toModel(nuevoAlimento);
            return ResponseEntity.status(HttpStatus.CREATED).body(alimentoDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear alimento");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar alimento", description = "Actualiza la información nutricional de un alimento existente y retorna DTO con HATEOAS links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Alimento actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Alimento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> updateAlimento(
            @Parameter(description = "ID del alimento", example = "1")
            @PathVariable Long id,
            @RequestBody Alimento alimentoDetails) {
        try {
            Alimento actualizado = alimentoService.updateAlimento(id, alimentoDetails);
            AlimentoDTO alimentoDTO = alimentoAssembler.toModel(actualizado);
            return ResponseEntity.ok(alimentoDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar alimento");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar alimento", description = "Elimina un alimento de la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Alimento eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Alimento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> deleteAlimento(
            @Parameter(description = "ID del alimento", example = "1")
            @PathVariable Long id) {
        try {
            alimentoService.deleteAlimento(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar alimento");
        }
    }
}
