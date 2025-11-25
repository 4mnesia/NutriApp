package com.example.NutriApp.assembler;

import com.example.NutriApp.controller.HistorialPesoController;
import com.example.NutriApp.controller.UsuarioController;
import com.example.NutriApp.dto.UsuarioDTO;
import com.example.NutriApp.model.Usuario;
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

        // --- CAMPOS NUEVOS AÑADIDOS ---
        dto.setPeso(usuario.getPeso());
        dto.setMetaCalorias(usuario.getMetaCalorias());
        dto.setMetaProteinas(usuario.getMetaProteinas());
        dto.setMetaCarbos(usuario.getMetaCarbos());
        dto.setMetaGrasas(usuario.getMetaGrasas());

        // Agregar links HATEOAS
        dto.add(linkTo(methodOn(UsuarioController.class).getUsuarioById(usuario.getId())).withSelfRel());
        dto.add(linkTo(methodOn(UsuarioController.class).getAllUsuarios()).withRel("usuarios"));
        dto.add(linkTo(methodOn(UsuarioController.class).updateUsuario(usuario.getId(), null)).withRel("update"));
        dto.add(linkTo(methodOn(UsuarioController.class).deleteUsuario(usuario.getId())).withRel("delete"));

        // Link al historial de peso (buena práctica)
        try {
            dto.add(linkTo(methodOn(HistorialPesoController.class).getWeightHistory(usuario.getId())).withRel("historial-peso"));
        } catch (Exception e) {
            // Evita errores si el controlador aún no está completamente mapeado durante el build
        }

        return dto;
    }
}
