package com.example.NutriApp.repository;

import com.example.NutriApp.model.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlimentoRepository extends JpaRepository<Alimento, Long> {
    Optional<Alimento> findByNombre(String nombre);
    List<Alimento> findByNombreContainingIgnoreCase(String nombre);
}
