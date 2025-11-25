package com.example.NutriApp.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "usuario")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Usuario de la aplicación NutriApp")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String fullName;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre de usuario único", example = "juanperez")
    private String username;

    @Column(nullable = false, unique = true)
    @Schema(description = "Email único del usuario", example = "juan@example.com")
    private String email;

    @Column(nullable = false)
    @Schema(description = "Hash de la contraseña", example = "hashed_password_here")
    private String passwordHash;

    @Column
    @Schema(description = "Peso actual del usuario en kg", example = "70.5")
    private Double peso;

    // --- CAMPOS NUEVOS PARA METAS ---
    @Column
    @Schema(description = "Meta de calorías diarias", example = "2000")
    private Integer metaCalorias;

    @Column
    @Schema(description = "Meta de proteínas diarias en gramos", example = "150")
    private Integer metaProteinas;

    @Column
    @Schema(description = "Meta de carbohidratos diarios en gramos", example = "250")
    private Integer metaCarbos;

    @Column
    @Schema(description = "Meta de grasas diarias en gramos", example = "70")
    private Integer metaGrasas;

    // --- Relaciones ---
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comida> comidas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialPeso> historialPeso;

    // --- Getters ---
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Double getPeso() { return peso; }
    public Integer getMetaCalorias() { return metaCalorias; }
    public Integer getMetaProteinas() { return metaProteinas; }
    public Integer getMetaCarbos() { return metaCarbos; }
    public Integer getMetaGrasas() { return metaGrasas; }
    public List<Comida> getComidas() { return comidas; }
    public List<HistorialPeso> getHistorialPeso() { return historialPeso; }

    // --- Setters ---
    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setPeso(Double peso) { this.peso = peso; }
    public void setMetaCalorias(Integer metaCalorias) { this.metaCalorias = metaCalorias; }
    public void setMetaProteinas(Integer metaProteinas) { this.metaProteinas = metaProteinas; }
    public void setMetaCarbos(Integer metaCarbos) { this.metaCarbos = metaCarbos; }
    public void setMetaGrasas(Integer metaGrasas) { this.metaGrasas = metaGrasas; }
    public void setComidas(List<Comida> comidas) { this.comidas = comidas; }
    public void setHistorialPeso(List<HistorialPeso> historialPeso) { this.historialPeso = historialPeso; }
}
