package com.example.NutriApp.repository;

import com.example.NutriApp.model.Comida;
import com.example.NutriApp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComidaRepository extends JpaRepository<Comida, Long> {
    List<Comida> findByUsuario(Usuario usuario);
    List<Comida> findByUsuarioAndFecha(Usuario usuario, LocalDate fecha);
    List<Comida> findByUsuarioAndTipoDeComida(Usuario usuario, String tipoDeComida);
}
