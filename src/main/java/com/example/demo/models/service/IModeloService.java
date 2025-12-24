package com.example.demo.models.service;

import com.example.demo.models.entity.Modelo;
import java.util.List;

/**
 * Interfaz de servicio para Modelo
 */
public interface IModeloService {
    List<Modelo> findAll();

    Modelo findById(Long id);

    List<Modelo> findByMarcaId(Long marcaId);

    List<Modelo> buscarPorNombre(String nombre);

    Modelo findByNombreAndMarcaId(String nombre, Long marcaId);

    Modelo save(Modelo entity);

    void deleteById(Long id);

    /**
     * Cuenta vehiculos asociados a un modelo
     */
    long countVehiculosByModeloId(Long modeloId);
}
