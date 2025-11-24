package com.example.NutriApp.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "comida")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Comida de un usuario en una fecha específica")
public class Comida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la comida", example = "1")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Tipo de comida", example = "DESAYUNO", allowableValues = {"DESAYUNO", "ALMUERZO", "CENA", "SNACK"})
    private TipoDeComida tipoDeComida;

    @Column(nullable = false)
    @Schema(description = "Fecha de la comida", example = "2025-11-23")
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @Schema(description = "Usuario propietario de la comida")
    private Usuario usuario;

    @OneToMany(mappedBy = "comida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComidaAlimento> alimentosConCantidad;

    // Getters
    public Long getId() {
        return id;
    }

    public TipoDeComida getTipoDeComida() {
        return tipoDeComida;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<ComidaAlimento> getAlimentosConCantidad() {
        return alimentosConCantidad;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTipoDeComida(TipoDeComida tipoDeComida) {
        this.tipoDeComida = tipoDeComida;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setAlimentosConCantidad(List<ComidaAlimento> alimentosConCantidad) {
        this.alimentosConCantidad = alimentosConCantidad;
    }
}