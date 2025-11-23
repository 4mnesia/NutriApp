package com.example.NutriApp.assembler;

import com.example.NutriApp.controller.ComidaController;
import com.example.NutriApp.dto.ComidaDTO;
import com.example.NutriApp.model.Comida;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ComidaAssembler extends RepresentationModelAssemblerSupport<Comida, ComidaDTO> {

    public ComidaAssembler() {
        super(ComidaController.class, ComidaDTO.class);
    }

    @Override
    public ComidaDTO toModel(Comida comida) {
        ComidaDTO dto = new ComidaDTO();
        dto.setId(comida.getId());
        dto.setTipoDeComida(comida.getTipoDeComida());
        dto.setFecha(comida.getFecha());
        dto.setUsuarioId(comida.getUsuario().getId());

        // Agregar links HATEOAS
        dto.add(linkTo(methodOn(ComidaController.class).getComidaById(comida.getId())).withSelfRel());
        dto.add(linkTo(methodOn(ComidaController.class).getAllComidas()).withRel("comidas"));
        dto.add(linkTo(methodOn(ComidaController.class).getComidasByUsuario(comida.getUsuario().getId())).withRel("usuario-comidas"));
        dto.add(linkTo(methodOn(ComidaController.class).getComidasByUsuarioAndFecha(comida.getUsuario().getId(), comida.getFecha())).withRel("comidas-fecha"));
        dto.add(linkTo(methodOn(ComidaController.class).updateComida(comida.getId(), null)).withRel("update"));
        dto.add(linkTo(methodOn(ComidaController.class).deleteComida(comida.getId())).withRel("delete"));

        return dto;
    }
}
