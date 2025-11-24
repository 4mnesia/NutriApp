package com.example.NutriApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "alimento")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Alimento con información nutricional por 100g")
public class Alimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del alimento", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nombre del alimento", example = "Pollo")
    private String nombre;

    @Column(nullable = false)
    @Schema(description = "Calorías por 100g", example = "165")
    private Integer caloriasPor100g;

    @Column(nullable = false)
    @Schema(description = "Proteínas en gramos por 100g", example = "31.0")
    private Float proteinasPor100g;

    @Column(nullable = false)
    @Schema(description = "Carbohidratos en gramos por 100g", example = "0.0")
    private Float carbosPor100g;

    @Column(nullable = false)
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
