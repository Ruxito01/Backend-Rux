package com.example.demo.models.service;

import com.example.demo.models.entity.Marca;
import java.util.List;

/**
 * Interfaz de servicio para Marca
 */
public interface IMarcaService {
    List<Marca> findAll();

    Marca findById(Long id);

    Marca findByNombre(String nombre);

    Marca save(Marca entity);

    void deleteById(Long id);
}
