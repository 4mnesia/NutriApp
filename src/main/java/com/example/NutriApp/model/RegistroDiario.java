package com.example.NutriApp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "registro_diario")
public class RegistroDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "total_calorias", columnDefinition = "int default 0")
    private int totalCalorias = 0;

    @Column(name = "total_proteinas", columnDefinition = "int default 0")
    private int totalProteinas = 0;

    @Column(name = "total_carbos", columnDefinition = "int default 0")
    private int totalCarbos = 0;

    @Column(name = "total_grasas", columnDefinition = "int default 0")
    private int totalGrasas = 0;

    // Constructor, getters y setters generados por Lombok @Data
}
