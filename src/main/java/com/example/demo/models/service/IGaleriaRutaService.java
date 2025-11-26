package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.GaleriaRuta;

public interface IGaleriaRutaService {
    public List<GaleriaRuta> findAll();

    public GaleriaRuta findById(Long id);

    public GaleriaRuta save(GaleriaRuta galeriaRuta);

    public void delete(Long id);
}
