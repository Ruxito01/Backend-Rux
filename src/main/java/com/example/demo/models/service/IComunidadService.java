package com.example.demo.models.service;

import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.entity.Usuario;
import java.util.List;
import java.util.Set;

public interface IComunidadService {
    List<Comunidad> findAll();

    Comunidad findById(Long id);

    Comunidad save(Comunidad entity);

    void deleteById(Long id);

    Set<Usuario> getMiembrosByComunidadId(Long comunidadId);
}
