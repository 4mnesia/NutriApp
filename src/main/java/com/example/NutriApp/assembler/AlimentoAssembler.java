package com.example.NutriApp.assembler;

import com.example.NutriApp.controller.AlimentoController;
import com.example.NutriApp.dto.AlimentoDTO;
import com.example.NutriApp.model.Alimento;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AlimentoAssembler extends RepresentationModelAssemblerSupport<Alimento, AlimentoDTO> {

    public AlimentoAssembler() {
        super(AlimentoController.class, AlimentoDTO.class);
    }

    @Override
    public AlimentoDTO toModel(Alimento alimento) {
        AlimentoDTO dto = new AlimentoDTO();
        dto.setId(alimento.getId());
        dto.setNombre(alimento.getNombre());
        dto.setCaloriasPor100g(alimento.getCaloriasPor100g());
        dto.setProteinasPor100g(alimento.getProteinasPor100g());
        dto.setCarbosPor100g(alimento.getCarbosPor100g());
        dto.setGrasasPor100g(alimento.getGrasasPor100g());

        // Agregar links HATEOAS
        dto.add(linkTo(methodOn(AlimentoController.class).getAlimentoById(alimento.getId())).withSelfRel());
        dto.add(linkTo(methodOn(AlimentoController.class).getAllAlimentos()).withRel("alimentos"));
        dto.add(linkTo(methodOn(AlimentoController.class).buscarAlimentos(alimento.getNombre())).withRel("buscar"));
        dto.add(linkTo(methodOn(AlimentoController.class).updateAlimento(alimento.getId(), null)).withRel("update"));
        dto.add(linkTo(methodOn(AlimentoController.class).deleteAlimento(alimento.getId())).withRel("delete"));

        return dto;
    }
}
