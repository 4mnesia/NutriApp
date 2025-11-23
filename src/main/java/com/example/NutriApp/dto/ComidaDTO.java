package com.example.NutriApp.dto;

import com.example.NutriApp.model.TipoDeComida;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "DTO para Comida con HATEOAS links")
public class ComidaDTO extends RepresentationModel<ComidaDTO> {
    @Schema(description = "ID único de la comida", example = "1")
    private Long id;

    @Schema(description = "Tipo de comida", example = "DESAYUNO")
    private TipoDeComida tipoDeComida;

    @Schema(description = "Fecha de la comida", example = "2025-11-23")
    private LocalDate fecha;

    @Schema(description = "ID del usuario propietario", example = "1")
    private Long usuarioId;
}
