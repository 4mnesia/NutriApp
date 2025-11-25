package com.example.NutriApp.assembler;import com.example.NutriApp.controller.HistorialPesoController;
import com.example.NutriApp.dto.HistorialPesoDTO;
import com.example.NutriApp.model.HistorialPeso;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class HistorialPesoAssembler extends RepresentationModelAssemblerSupport<HistorialPeso, HistorialPesoDTO> {

    public HistorialPesoAssembler() {
        super(HistorialPesoController.class, HistorialPesoDTO.class);
    }

    @Override
    public HistorialPesoDTO toModel(HistorialPeso entity) {
        HistorialPesoDTO dto = new HistorialPesoDTO(entity.getId(), entity.getPeso(), entity.getFecha());
        // Link a la colección de historial del usuario
        dto.add(linkTo(methodOn(HistorialPesoController.class).getWeightHistory(entity.getUsuario().getId())).withRel("historial-usuario"));
        return dto;
    }
}