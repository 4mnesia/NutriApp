package com.example.NutriApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "DTO para Usuario con HATEOAS links")
public class UsuarioDTO extends RepresentationModel<UsuarioDTO> {
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String fullName;

    @Schema(description = "Nombre de usuario único", example = "juanperez")
    private String username;

    @Schema(description = "Email único del usuario", example = "juan@example.com")
    private String email;
}
