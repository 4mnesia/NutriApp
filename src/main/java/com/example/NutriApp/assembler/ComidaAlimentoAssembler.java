package com.example.NutriApp.assembler;

import com.example.NutriApp.controller.ComidaAlimentoController;
import com.example.NutriApp.dto.ComidaAlimentoDTO;
import com.example.NutriApp.model.ComidaAlimento;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ComidaAlimentoAssembler extends RepresentationModelAssemblerSupport<ComidaAlimento, ComidaAlimentoDTO> {

    public ComidaAlimentoAssembler() {
        super(ComidaAlimentoController.class, ComidaAlimentoDTO.class);
    }

    @Override
    public ComidaAlimentoDTO toModel(ComidaAlimento comidaAlimento) {
        ComidaAlimentoDTO dto = new ComidaAlimentoDTO();
        dto.setId(comidaAlimento.getId());
        dto.setComidaId(comidaAlimento.getComida().getId());
        dto.setAlimentoId(comidaAlimento.getAlimento().getId());
        dto.setCantidadEnGramos(comidaAlimento.getCantidadEnGramos());

        // Agregar links HATEOAS
        dto.add(linkTo(methodOn(ComidaAlimentoController.class).getComidaAlimentoById(comidaAlimento.getId())).withSelfRel());
        dto.add(linkTo(methodOn(ComidaAlimentoController.class).getAllComidaAlimentos()).withRel("comida-alimentos"));
        dto.add(linkTo(methodOn(ComidaAlimentoController.class).getAlimentosByComida(comidaAlimento.getComida().getId())).withRel("alimentos-comida"));
        dto.add(linkTo(methodOn(ComidaAlimentoController.class).updateComidaAlimento(comidaAlimento.getId(), null)).withRel("update"));
        dto.add(linkTo(methodOn(ComidaAlimentoController.class).deleteComidaAlimento(comidaAlimento.getId())).withRel("delete"));

        return dto;
    }
}
