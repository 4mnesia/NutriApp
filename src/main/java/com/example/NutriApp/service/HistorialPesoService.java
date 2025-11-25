package com.example.NutriApp.service;

import com.example.NutriApp.model.HistorialPeso;
import com.example.NutriApp.model.Usuario;
import com.example.NutriApp.repository.HistorialPesoRepository;
import com.example.NutriApp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class HistorialPesoService {

    @Autowired
    private HistorialPesoRepository historialPesoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<HistorialPeso> getHistorialByUsuarioId(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new IllegalArgumentException("Usuario no encontrado con id: " + usuarioId);
        }
        return historialPesoRepository.findByUsuarioIdOrderByFechaAsc(usuarioId);
    }

    public HistorialPeso saveNewWeightEntry(Long usuarioId, Double peso) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + usuarioId));

        HistorialPeso newEntry = new HistorialPeso();
        newEntry.setUsuario(usuario);
        newEntry.setPeso(peso);
        newEntry.setFecha(LocalDate.now());

        // Opcional: Actualizar el peso actual en el perfil del usuario
        usuario.setPeso(peso);
        usuarioRepository.save(usuario);

        return historialPesoRepository.save(newEntry);
    }
}