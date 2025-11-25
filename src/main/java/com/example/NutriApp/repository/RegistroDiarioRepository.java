package com.example.NutriApp.repository;

import com.example.NutriApp.model.RegistroDiario;
import com.example.NutriApp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroDiarioRepository extends JpaRepository<RegistroDiario, Long> {

    Optional<RegistroDiario> findByUsuarioAndFecha(Usuario usuario, LocalDate fecha);

    /**
     * Busca todos los registros diarios para un usuario entre dos fechas, ordenados por fecha.
     * @param usuario El objeto del usuario.
     * @param fechaInicio La fecha de inicio del rango.
     * @param fechaFin La fecha de fin del rango.
     * @return Una lista de registros diarios.
     */
    List<RegistroDiario> findByUsuarioAndFechaBetweenOrderByFechaAsc(Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin);
}
