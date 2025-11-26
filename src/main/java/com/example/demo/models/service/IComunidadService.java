package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.Comunidad;

public interface IComunidadService {
    public List<Comunidad> findAll();

    public Comunidad findById(Long id);

    public Comunidad save(Comunidad comunidad);

    public void delete(Long id);
}
