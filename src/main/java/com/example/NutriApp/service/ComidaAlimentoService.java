package com.example.NutriApp.service;

import com.example.NutriApp.model.ComidaAlimento;
import com.example.NutriApp.model.Comida;
import com.example.NutriApp.repository.ComidaAlimentoRepository;
import com.example.NutriApp.repository.ComidaRepository;
import com.example.NutriApp.repository.AlimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ComidaAlimentoService {

    @Autowired
    private ComidaAlimentoRepository comidaAlimentoRepository;

    @Autowired
    private ComidaRepository comidaRepository;

    @Autowired
    private AlimentoRepository alimentoRepository;

    public List<ComidaAlimento> getAllComidaAlimentos() {
        return comidaAlimentoRepository.findAll();
    }

    public Optional<ComidaAlimento> getComidaAlimentoById(Long id) {
        return comidaAlimentoRepository.findById(id);
    }

    public List<ComidaAlimento> getAlimentosByComida(Long comidaId) {
        Optional<Comida> comida = comidaRepository.findById(comidaId);
        if (comida.isPresent()) {
            return comidaAlimentoRepository.findByComida(comida.get());
        }
        throw new IllegalArgumentException("Comida no encontrada");
    }

    public ComidaAlimento agregarAlimentoAComida(ComidaAlimento comidaAlimento) {
        if (comidaAlimento.getComida() == null || comidaAlimento.getComida().getId() == null) {
            throw new IllegalArgumentException("La comida es requerida");
        }
        if (comidaAlimento.getAlimento() == null || comidaAlimento.getAlimento().getId() == null) {
            throw new IllegalArgumentException("El alimento es requerido");
        }
        if (comidaAlimento.getCantidadEnGramos() == null || comidaAlimento.getCantidadEnGramos() <= 0) {
            throw new IllegalArgumentException("La cantidad en gramos debe ser mayor a 0");
        }
        
        if (!comidaRepository.existsById(comidaAlimento.getComida().getId())) {
            throw new IllegalArgumentException("Comida no encontrada");
        }
        if (!alimentoRepository.existsById(comidaAlimento.getAlimento().getId())) {
            throw new IllegalArgumentException("Alimento no encontrado");
        }
        
        return comidaAlimentoRepository.save(comidaAlimento);
    }

    public ComidaAlimento updateComidaAlimento(Long id, ComidaAlimento comidaAlimentoDetails) {
        Optional<ComidaAlimento> comidaAlimento = comidaAlimentoRepository.findById(id);
        if (comidaAlimento.isPresent()) {
            ComidaAlimento existing = comidaAlimento.get();
            if (comidaAlimentoDetails.getCantidadEnGramos() != null && comidaAlimentoDetails.getCantidadEnGramos() > 0) {
                existing.setCantidadEnGramos(comidaAlimentoDetails.getCantidadEnGramos());
            }
            return comidaAlimentoRepository.save(existing);
        }
        throw new IllegalArgumentException("ComidaAlimento no encontrado");
    }

    public void deleteComidaAlimento(Long id) {
        if (!comidaAlimentoRepository.existsById(id)) {
            throw new IllegalArgumentException("ComidaAlimento no encontrado");
        }
        comidaAlimentoRepository.deleteById(id);
    }

    public boolean existeComidaAlimento(Long id) {
        return comidaAlimentoRepository.existsById(id);
    }

    public void deleteAlimentosDeComida(Long comidaId) {
        Optional<Comida> comida = comidaRepository.findById(comidaId);
        if (comida.isPresent()) {
            List<ComidaAlimento> alimentos = comidaAlimentoRepository.findByComida(comida.get());
            comidaAlimentoRepository.deleteAll(alimentos);
        }
    }
}
