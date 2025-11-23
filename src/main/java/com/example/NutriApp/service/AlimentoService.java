package com.example.NutriApp.service;

import com.example.NutriApp.model.Alimento;
import com.example.NutriApp.repository.AlimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AlimentoService {

    @Autowired
    private AlimentoRepository alimentoRepository;

    public List<Alimento> getAllAlimentos() {
        return alimentoRepository.findAll();
    }

    public Optional<Alimento> getAlimentoById(Long id) {
        return alimentoRepository.findById(id);
    }

    public List<Alimento> buscarAlimentos(String nombre) {
        return alimentoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Optional<Alimento> getAlimentoByNombre(String nombre) {
        return alimentoRepository.findByNombre(nombre);
    }

    public Alimento crearAlimento(Alimento alimento) {
        if (alimento.getNombre() == null || alimento.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del alimento es requerido");
        }
        if (alimento.getCaloriasPor100g() == null || alimento.getCaloriasPor100g() < 0) {
            throw new IllegalArgumentException("Las calorías deben ser un valor válido");
        }
        if (alimento.getProteinasPor100g() == null || alimento.getProteinasPor100g() < 0) {
            throw new IllegalArgumentException("Las proteínas deben ser un valor válido");
        }
        if (alimento.getCarbosPor100g() == null || alimento.getCarbosPor100g() < 0) {
            throw new IllegalArgumentException("Los carbohidratos deben ser un valor válido");
        }
        if (alimento.getGrasasPor100g() == null || alimento.getGrasasPor100g() < 0) {
            throw new IllegalArgumentException("Las grasas deben ser un valor válido");
        }
        return alimentoRepository.save(alimento);
    }

    public Alimento updateAlimento(Long id, Alimento alimentoDetails) {
        Optional<Alimento> alimento = alimentoRepository.findById(id);
        if (alimento.isPresent()) {
            Alimento existing = alimento.get();
            if (alimentoDetails.getNombre() != null && !alimentoDetails.getNombre().isEmpty()) {
                existing.setNombre(alimentoDetails.getNombre());
            }
            if (alimentoDetails.getCaloriasPor100g() != null) {
                existing.setCaloriasPor100g(alimentoDetails.getCaloriasPor100g());
            }
            if (alimentoDetails.getProteinasPor100g() != null) {
                existing.setProteinasPor100g(alimentoDetails.getProteinasPor100g());
            }
            if (alimentoDetails.getCarbosPor100g() != null) {
                existing.setCarbosPor100g(alimentoDetails.getCarbosPor100g());
            }
            if (alimentoDetails.getGrasasPor100g() != null) {
                existing.setGrasasPor100g(alimentoDetails.getGrasasPor100g());
            }
            return alimentoRepository.save(existing);
        }
        throw new IllegalArgumentException("Alimento no encontrado");
    }

    public void deleteAlimento(Long id) {
        if (!alimentoRepository.existsById(id)) {
            throw new IllegalArgumentException("Alimento no encontrado");
        }
        alimentoRepository.deleteById(id);
    }

    public boolean existeAlimento(Long id) {
        return alimentoRepository.existsById(id);
    }
}
