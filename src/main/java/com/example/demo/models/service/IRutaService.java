package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.Ruta;

public interface IRutaService {
    public List<Ruta> findAll();

    public Ruta findById(Long id);

    public Ruta save(Ruta ruta);

    public void delete(Long id);
}
