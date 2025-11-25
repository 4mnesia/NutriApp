package com.example.NutriApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDiarioDTO extends RepresentationModel<RegistroDiarioDTO> {
    private Long id;
    private Long usuarioId;
    private LocalDate fecha;
    private int totalCalorias;
    private int totalProteinas;
    private int totalCarbos;
    private int totalGrasas;
}
