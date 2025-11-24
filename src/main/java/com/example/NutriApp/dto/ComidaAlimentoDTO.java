package com.example.NutriApp.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import io.swagger.v3.oas.annotations.media.Schema;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "DTO para ComidaAlimento con HATEOAS links")
public class ComidaAlimentoDTO extends RepresentationModel<ComidaAlimentoDTO> {
    @Schema(description = "ID único de la relación", example = "1")
    private Long id;

    @Schema(description = "ID de la comida", example = "1")
    private Long comidaId;

    @Schema(description = "ID del alimento", example = "1")
    private Long alimentoId;

    @Schema(description = "Cantidad en gramos del alimento", example = "150")
    private Integer cantidadEnGramos;

    // Getters
    public Long getId() {
        return id;
    }

    public Long getComidaId() {
        return comidaId;
    }

    public Long getAlimentoId() {
        return alimentoId;
    }

    public Integer getCantidadEnGramos() {
        return cantidadEnGramos;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setComidaId(Long comidaId) {
        this.comidaId = comidaId;
    }

    public void setAlimentoId(Long alimentoId) {
        this.alimentoId = alimentoId;
    }

    public void setCantidadEnGramos(Integer cantidadEnGramos) {
        this.cantidadEnGramos = cantidadEnGramos;
    }
}
