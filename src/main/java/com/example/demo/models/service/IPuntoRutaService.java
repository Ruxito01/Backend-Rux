package com.example.demo.models.service;

import com.example.demo.models.entity.PuntoRuta;
import java.util.List;

public interface IPuntoRutaService {
    List<PuntoRuta> findAll();
    PuntoRuta findById(Long id);
    PuntoRuta save(PuntoRuta entity);
    void deleteById(Long id);
}
