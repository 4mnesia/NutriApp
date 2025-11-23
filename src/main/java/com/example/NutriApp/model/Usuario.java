package com.example.NutriApp.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "usuario")
@Data
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

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de comidas del usuario")
    private List<Comida> comidas;
}
