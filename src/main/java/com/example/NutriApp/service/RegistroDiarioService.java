package com.example.NutriApp.service;

import com.example.NutriApp.model.RegistroDiario;
import com.example.NutriApp.model.Usuario;
import com.example.NutriApp.repository.RegistroDiarioRepository;
import com.example.NutriApp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegistroDiarioService {

    @Autowired
    private RegistroDiarioRepository registroDiarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public RegistroDiario findOrCreateRegistroDiario(Long usuarioId, LocalDate fecha) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + usuarioId));

        return registroDiarioRepository.findByUsuarioAndFecha(usuario, fecha)
                .orElseGet(() -> {
                    RegistroDiario nuevoRegistro = new RegistroDiario();
                    nuevoRegistro.setUsuario(usuario);
                    nuevoRegistro.setFecha(fecha);
                    return registroDiarioRepository.save(nuevoRegistro);
                });
    }

    /**
     * Obtiene los registros diarios de un usuario para los últimos 7 días.
     * @param usuarioId El ID del usuario.
     * @return Una lista de hasta 7 registros diarios.
     */
    @Transactional(readOnly = true)
    public List<RegistroDiario> getRegistrosSemanales(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + usuarioId));
        
        LocalDate fechaFin = LocalDate.now();
        LocalDate fechaInicio = fechaFin.minusDays(6);

        return registroDiarioRepository.findByUsuarioAndFechaBetweenOrderByFechaAsc(usuario, fechaInicio, fechaFin);
    }
}
