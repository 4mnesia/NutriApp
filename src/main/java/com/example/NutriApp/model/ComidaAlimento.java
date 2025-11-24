package com.example.NutriApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "comida_alimento")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Relación entre una comida y los alimentos que contiene")
public class ComidaAlimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la relación", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comida_id", nullable = false)
    @Schema(description = "Comida asociada")
    private Comida comida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alimento_id", nullable = false)
    @Schema(description = "Alimento asociado")
    private Alimento alimento;

    @Column(nullable = false)
    @Schema(description = "Cantidad en gramos del alimento", example = "150")
    private Integer cantidadEnGramos;

    // Getters
    public Long getId() {
        return id;
    }

    public Comida getComida() {
        return comida;
    }

    public Alimento getAlimento() {
        return alimento;
    }

    public Integer getCantidadEnGramos() {
        return cantidadEnGramos;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setComida(Comida comida) {
        this.comida = comida;
    }

    public void setAlimento(Alimento alimento) {
        this.alimento = alimento;
    }

    public void setCantidadEnGramos(Integer cantidadEnGramos) {
        this.cantidadEnGramos = cantidadEnGramos;
    }
}
