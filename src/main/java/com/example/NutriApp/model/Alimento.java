package com.example.NutriApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "alimentos")
@Data
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
}
