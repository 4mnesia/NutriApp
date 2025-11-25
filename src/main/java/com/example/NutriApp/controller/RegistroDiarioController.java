package com.example.NutriApp.controller;

import com.example.NutriApp.assembler.RegistroDiarioAssembler;
import com.example.NutriApp.dto.RegistroDiarioDTO;
import com.example.NutriApp.model.RegistroDiario;
import com.example.NutriApp.service.RegistroDiarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros-diarios")
@CrossOrigin(origins = "*")
@Tag(name = "Registros Diarios", description = "Endpoints para gestionar los resúmenes diarios de macros y calorías")
public class RegistroDiarioController {

    @Autowired
    private RegistroDiarioService registroDiarioService;

    @Autowired
    private RegistroDiarioAssembler registroDiarioAssembler;

    @GetMapping("/usuario/{usuarioId}/semana")
    @Operation(summary = "Obtener registros de la última semana", description = "Retorna una lista de hasta 7 registros diarios para un usuario, representando la última semana.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registros obtenidos exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<CollectionModel<RegistroDiarioDTO>> getRegistrosSemanales(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long usuarioId) {
        try {
            List<RegistroDiario> registros = registroDiarioService.getRegistrosSemanales(usuarioId);
            CollectionModel<RegistroDiarioDTO> dtoList = registroDiarioAssembler.toCollectionModel(registros);
            return ResponseEntity.ok(dtoList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
