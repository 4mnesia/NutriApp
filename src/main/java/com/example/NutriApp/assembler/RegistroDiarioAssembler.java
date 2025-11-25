package com.example.NutriApp.assembler;

import com.example.NutriApp.controller.RegistroDiarioController;
import com.example.NutriApp.dto.RegistroDiarioDTO;
import com.example.NutriApp.model.RegistroDiario;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RegistroDiarioAssembler extends RepresentationModelAssemblerSupport<RegistroDiario, RegistroDiarioDTO> {

    public RegistroDiarioAssembler() {
        super(RegistroDiarioController.class, RegistroDiarioDTO.class);
    }

    @Override
    public RegistroDiarioDTO toModel(RegistroDiario entity) {
        RegistroDiarioDTO dto = new RegistroDiarioDTO();
        dto.setId(entity.getId());
        dto.setUsuarioId(entity.getUsuario().getId());
        dto.setFecha(entity.getFecha());
        dto.setTotalCalorias(entity.getTotalCalorias());
        dto.setTotalProteinas(entity.getTotalProteinas());
        dto.setTotalCarbos(entity.getTotalCarbos());
        dto.setTotalGrasas(entity.getTotalGrasas());

        // Opcional: añadir links HATEOAS si se necesitaran en el futuro
        // dto.add(linkTo(methodOn(RegistroDiarioController.class).getRegistroById(entity.getId())).withSelfRel());

        return dto;
    }
}
