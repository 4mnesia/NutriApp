package com.example.NutriApp.service;

import com.example.NutriApp.model.Alimento;
import com.example.NutriApp.model.Comida;
import com.example.NutriApp.model.ComidaAlimento;
import com.example.NutriApp.model.RegistroDiario;
import com.example.NutriApp.repository.AlimentoRepository;
import com.example.NutriApp.repository.ComidaAlimentoRepository;
import com.example.NutriApp.repository.ComidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // --- NUEVO SERVICIO INYECTADO ---
    @Autowired
    private RegistroDiarioService registroDiarioService;

    public List<ComidaAlimento> getAllComidaAlimentos() {
        return comidaAlimentoRepository.findAll();
    }

    public Optional<ComidaAlimento> getComidaAlimentoById(Long id) {
        return comidaAlimentoRepository.findById(id);
    }

    public List<ComidaAlimento> getAlimentosByComida(Long comidaId) {
        Comida comida = comidaRepository.findById(comidaId)
                .orElseThrow(() -> new IllegalArgumentException("Comida no encontrada con id: " + comidaId));
        return comidaAlimentoRepository.findByComida(comida);
    }

    // --- MÉTODO DE AGREGAR MEJORADO ---
    @Transactional
    public ComidaAlimento agregarAlimentoAComida(ComidaAlimento comidaAlimento) {
        // 1. Validar y obtener las entidades completas
        Comida comida = comidaRepository.findById(comidaAlimento.getComida().getId())
                .orElseThrow(() -> new IllegalArgumentException("Comida no encontrada"));
        Alimento alimento = alimentoRepository.findById(comidaAlimento.getAlimento().getId())
                .orElseThrow(() -> new IllegalArgumentException("Alimento no encontrado"));

        Integer cantidad = comidaAlimento.getCantidadEnGramos();
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad en gramos debe ser mayor a 0");
        }

        // Asignamos las entidades completas por si acaso
        comidaAlimento.setComida(comida);
        comidaAlimento.setAlimento(alimento);

        // 2. Guardar la nueva relación
        ComidaAlimento nuevoComidaAlimento = comidaAlimentoRepository.save(comidaAlimento);

        // 3. Actualizar el registro diario (Sumar los macros)
        RegistroDiario registro = registroDiarioService.findOrCreateRegistroDiario(comida.getUsuario().getId(), comida.getFecha());
        
        registro.setTotalCalorias(registro.getTotalCalorias() + (int) ((alimento.getCaloriasPor100g() / 100.0) * cantidad));
        registro.setTotalProteinas(registro.getTotalProteinas() + (int) ((alimento.getProteinasPor100g() / 100.0) * cantidad));
        registro.setTotalCarbos(registro.getTotalCarbos() + (int) ((alimento.getCarbosPor100g() / 100.0) * cantidad));
        registro.setTotalGrasas(registro.getTotalGrasas() + (int) ((alimento.getGrasasPor100g() / 100.0) * cantidad));
        // La entidad 'registro' está gestionada por la transacción, se guardará al final.

        return nuevoComidaAlimento;
    }

    // --- MÉTODO DE ACTUALIZAR MEJORADO ---
    @Transactional
    public ComidaAlimento updateComidaAlimento(Long id, ComidaAlimento comidaAlimentoDetails) {
        ComidaAlimento existing = comidaAlimentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ComidaAlimento no encontrado"));

        Integer nuevaCantidad = comidaAlimentoDetails.getCantidadEnGramos();
        if (nuevaCantidad == null || nuevaCantidad <= 0) {
            throw new IllegalArgumentException("La cantidad en gramos debe ser mayor a 0");
        }

        // 1. Obtener datos para el cálculo
        Integer cantidadAntigua = existing.getCantidadEnGramos();
        Alimento alimento = existing.getAlimento();
        Comida comida = existing.getComida();

        // 2. Actualizar el registro diario
        RegistroDiario registro = registroDiarioService.findOrCreateRegistroDiario(comida.getUsuario().getId(), comida.getFecha());

        // 3. Calcular la diferencia y aplicarla a los totales
        registro.setTotalCalorias(registro.getTotalCalorias() - (int)((alimento.getCaloriasPor100g() / 100.0) * cantidadAntigua) + (int)((alimento.getCaloriasPor100g() / 100.0) * nuevaCantidad));
        registro.setTotalProteinas(registro.getTotalProteinas() - (int)((alimento.getProteinasPor100g() / 100.0) * cantidadAntigua) + (int)((alimento.getProteinasPor100g() / 100.0) * nuevaCantidad));
        registro.setTotalCarbos(registro.getTotalCarbos() - (int)((alimento.getCarbosPor100g() / 100.0) * cantidadAntigua) + (int)((alimento.getCarbosPor100g() / 100.0) * nuevaCantidad));
        registro.setTotalGrasas(registro.getTotalGrasas() - (int)((alimento.getGrasasPor100g() / 100.0) * cantidadAntigua) + (int)((alimento.getGrasasPor100g() / 100.0) * nuevaCantidad));

        // 4. Actualizar la cantidad en la entidad principal
        existing.setCantidadEnGramos(nuevaCantidad);

        return comidaAlimentoRepository.save(existing);
    }

    // --- MÉTODO DE BORRAR MEJORADO ---
    @Transactional
    public void deleteComidaAlimento(Long id) {
        ComidaAlimento comidaAlimento = comidaAlimentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ComidaAlimento no encontrado"));

        // 1. Obtener datos para el cálculo inverso
        Comida comida = comidaAlimento.getComida();
        Alimento alimento = comidaAlimento.getAlimento();
        Integer cantidad = comidaAlimento.getCantidadEnGramos();

        // 2. Actualizar el registro diario (restando los macros)
        RegistroDiario registro = registroDiarioService.findOrCreateRegistroDiario(comida.getUsuario().getId(), comida.getFecha());
        
        registro.setTotalCalorias(Math.max(0, registro.getTotalCalorias() - (int) ((alimento.getCaloriasPor100g() / 100.0) * cantidad)));
        registro.setTotalProteinas(Math.max(0, registro.getTotalProteinas() - (int) ((alimento.getProteinasPor100g() / 100.0) * cantidad)));
        registro.setTotalCarbos(Math.max(0, registro.getTotalCarbos() - (int) ((alimento.getCarbosPor100g() / 100.0) * cantidad)));
        registro.setTotalGrasas(Math.max(0, registro.getTotalGrasas() - (int) ((alimento.getGrasasPor100g() / 100.0) * cantidad)));

        // 3. Borrar la entidad
        comidaAlimentoRepository.delete(comidaAlimento);
    }
    
    public boolean existeComidaAlimento(Long id) {
        return comidaAlimentoRepository.existsById(id);
    }

    @Transactional
    public void deleteAlimentosDeComida(Long comidaId) {
        Comida comida = comidaRepository.findById(comidaId)
                .orElseThrow(() -> new IllegalArgumentException("Comida no encontrada con id: " + comidaId));
        
        // Reiniciar el registro diario para esta fecha antes de borrar
        RegistroDiario registro = registroDiarioService.findOrCreateRegistroDiario(comida.getUsuario().getId(), comida.getFecha());
        registro.setTotalCalorias(0);
        registro.setTotalProteinas(0);
        registro.setTotalCarbos(0);
        registro.setTotalGrasas(0);

        // Borrar todos los alimentos asociados a la comida
        List<ComidaAlimento> alimentos = comidaAlimentoRepository.findByComida(comida);
        comidaAlimentoRepository.deleteAll(alimentos);
    }
}
