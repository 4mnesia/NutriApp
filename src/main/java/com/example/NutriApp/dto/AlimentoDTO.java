package com.example.NutriApp.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import io.swagger.v3.oas.annotations.media.Schema;

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

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getCaloriasPor100g() {
        return caloriasPor100g;
    }

    public Float getProteinasPor100g() {
        return proteinasPor100g;
    }

    public Float getCarbosPor100g() {
        return carbosPor100g;
    }

    public Float getGrasasPor100g() {
        return grasasPor100g;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCaloriasPor100g(Integer caloriasPor100g) {
        this.caloriasPor100g = caloriasPor100g;
    }

    public void setProteinasPor100g(Float proteinasPor100g) {
        this.proteinasPor100g = proteinasPor100g;
    }

    public void setCarbosPor100g(Float carbosPor100g) {
        this.carbosPor100g = carbosPor100g;
    }

    public void setGrasasPor100g(Float grasasPor100g) {
        this.grasasPor100g = grasasPor100g;
    }
}
