package com.example.NutriApp.repository;

import com.example.NutriApp.model.HistorialPeso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistorialPesoRepository extends JpaRepository<HistorialPeso, Long> {
    List<HistorialPeso> findByUsuarioIdOrderByFechaAsc(Long usuarioId);
}
