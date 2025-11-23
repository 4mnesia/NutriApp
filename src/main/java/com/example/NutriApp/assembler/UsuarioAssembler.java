package com.example.NutriApp.assembler;

import com.example.NutriApp.controller.UsuarioController;
import com.example.NutriApp.dto.UsuarioDTO;
import com.example.NutriApp.model.Usuario;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioAssembler extends RepresentationModelAssemblerSupport<Usuario, UsuarioDTO> {

    public UsuarioAssembler() {
        super(UsuarioController.class, UsuarioDTO.class);
    }

    @Override
    public UsuarioDTO toModel(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setFullName(usuario.getFullName());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());

        // Agregar links HATEOAS
        dto.add(linkTo(methodOn(UsuarioController.class).getUsuarioById(usuario.getId())).withSelfRel());
        dto.add(linkTo(methodOn(UsuarioController.class).getAllUsuarios()).withRel("usuarios"));
        dto.add(linkTo(methodOn(UsuarioController.class).updateUsuario(usuario.getId(), null)).withRel("update"));
        dto.add(linkTo(methodOn(UsuarioController.class).deleteUsuario(usuario.getId())).withRel("delete"));

        return dto;
    }
}
