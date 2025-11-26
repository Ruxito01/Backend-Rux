package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.PuntoInteres;

public interface IPuntoInteresService {
    public List<PuntoInteres> findAll();

    public PuntoInteres findById(Long id);

    public PuntoInteres save(PuntoInteres puntoInteres);

    public void delete(Long id);
}
