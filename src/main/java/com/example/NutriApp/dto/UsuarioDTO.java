package com.example.NutriApp.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import io.swagger.v3.oas.annotations.media.Schema;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "DTO para Usuario con HATEOAS links")
@Relation(collectionRelation = "usuarios", itemRelation = "usuario")
public class UsuarioDTO extends RepresentationModel<UsuarioDTO> {
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String fullName;

    @Schema(description = "Nombre de usuario único", example = "juanperez")
    private String username;

    @Schema(description = "Email único del usuario", example = "juan@example.com")
    private String email;

    // --- CAMPOS NUEVOS ---
    @Schema(description = "Peso actual del usuario en kg", example = "70.5")
    private Double peso;

    @Schema(description = "Meta de calorías diarias", example = "2000")
    private Integer metaCalorias;

    @Schema(description = "Meta de proteínas diarias en gramos", example = "150")
    private Integer metaProteinas;

    @Schema(description = "Meta de carbohidratos diarios en gramos", example = "250")
    private Integer metaCarbos;

    @Schema(description = "Meta de grasas diarias en gramos", example = "70")
    private Integer metaGrasas;

    // Getters
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Double getPeso() { return peso; }
    public Integer getMetaCalorias() { return metaCalorias; }
    public Integer getMetaProteinas() { return metaProteinas; }
    public Integer getMetaCarbos() { return metaCarbos; }
    public Integer getMetaGrasas() { return metaGrasas; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPeso(Double peso) { this.peso = peso; }
    public void setMetaCalorias(Integer metaCalorias) { this.metaCalorias = metaCalorias; }
    public void setMetaProteinas(Integer metaProteinas) { this.metaProteinas = metaProteinas; }
    public void setMetaCarbos(Integer metaCarbos) { this.metaCarbos = metaCarbos; }
    public void setMetaGrasas(Integer metaGrasas) { this.metaGrasas = metaGrasas; }
}
