package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.DificultadRuta;

public interface IDificultadRutaService {
    public List<DificultadRuta> findAll();

    public DificultadRuta findById(Long id);

    public DificultadRuta save(DificultadRuta dificultadRuta);

    public void delete(Long id);
}
