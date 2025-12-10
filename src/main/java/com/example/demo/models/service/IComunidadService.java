package com.example.demo.models.service;

import com.example.demo.models.entity.Comunidad;
import java.util.List;

public interface IComunidadService {
    List<Comunidad> findAll();
    Comunidad findById(Long id);
    Comunidad save(Comunidad entity);
    void deleteById(Long id);
}
