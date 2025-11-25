package com.example.NutriApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false) // Añadido para buena práctica con HATEOAS
public class HistorialPesoDTO extends RepresentationModel<HistorialPesoDTO> { // <-- LA CORRECCIÓN CLAVE
    private Long id;
    private Double peso;
    private LocalDate fecha;
}