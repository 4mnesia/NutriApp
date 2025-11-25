package com.example.NutriApp.controller;

import com.example.NutriApp.assembler.HistorialPesoAssembler;
import com.example.NutriApp.dto.HistorialPesoDTO;
import com.example.NutriApp.dto.NuevoPesoRequest;import com.example.NutriApp.model.HistorialPeso;
import com.example.NutriApp.service.HistorialPesoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/historial-peso")
@CrossOrigin(origins = "*")
@Tag(name = "Historial de Peso", description = "Endpoints para gestionar el historial de peso de un usuario")
public class HistorialPesoController {

    @Autowired
    private HistorialPesoService historialPesoService;

    @Autowired
    private HistorialPesoAssembler historialPesoAssembler;

    @PostMapping
    @Operation(summary = "Añadir un nuevo registro de peso", description = "Crea una nueva entrada en el historial de peso para un usuario y actualiza su peso actual.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Registro de peso creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario no encontrado")
    })
    public ResponseEntity<HistorialPesoDTO> addWeightEntry(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable Long usuarioId,
            @RequestBody NuevoPesoRequest request) {
        try {
            HistorialPeso newEntry = historialPesoService.saveNewWeightEntry(usuarioId, request.getPeso());
            HistorialPesoDTO dto = historialPesoAssembler.toModel(newEntry);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Obtener el historial de peso de un usuario", description = "Retorna una lista de todos los registros de peso para un usuario, ordenados por fecha.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<CollectionModel<HistorialPesoDTO>> getWeightHistory(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable Long usuarioId) {
        try {
            List<HistorialPeso> history = historialPesoService.getHistorialByUsuarioId(usuarioId);
            CollectionModel<HistorialPesoDTO> dtoList = historialPesoAssembler.toCollectionModel(history);
            return ResponseEntity.ok(dtoList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}