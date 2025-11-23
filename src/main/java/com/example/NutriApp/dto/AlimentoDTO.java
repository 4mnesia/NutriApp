package com.example.NutriApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "DTO para Alimento con HATEOAS links")
public class AlimentoDTO extends RepresentationModel<AlimentoDTO> {
    @Schema(description = "ID único del alimento", example = "1")
    private Long id;

    @Schema(description = "Nombre del alimento", example = "Pollo")
    private String nombre;

    @Schema(description = "Calorías por 100g", example = "165")
    private Integer caloriasPor100g;

    @Schema(description = "Proteínas en gramos por 100g", example = "31.0")
    private Float proteinasPor100g;

    @Schema(description = "Carbohidratos en gramos por 100g", example = "0.0")
    private Float carbosPor100g;

    @Schema(description = "Grasas en gramos por 100g", example = "3.6")
    private Float grasasPor100g;
}
