package com.example.NutriApp.repository;

import com.example.NutriApp.model.ComidaAlimento;
import com.example.NutriApp.model.Comida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComidaAlimentoRepository extends JpaRepository<ComidaAlimento, Long> {
    List<ComidaAlimento> findByComida(Comida comida);
}
