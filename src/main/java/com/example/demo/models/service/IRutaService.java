package com.example.demo.models.service;

import com.example.demo.models.entity.Ruta;
import java.util.List;

public interface IRutaService {
    List<Ruta> findAll();

    List<com.example.demo.models.dto.RutaResumenDTO> findAllResumen();

    Ruta findById(Long id);

    Ruta save(Ruta entity);

    void deleteById(Long id);

    List<Ruta> findByComunidadId(Long comunidadId);
}
