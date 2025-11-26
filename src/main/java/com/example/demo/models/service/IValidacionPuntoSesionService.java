package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.ValidacionPuntoSesion;

public interface IValidacionPuntoSesionService {
    public List<ValidacionPuntoSesion> findAll();

    public ValidacionPuntoSesion findById(Long id);

    public ValidacionPuntoSesion save(ValidacionPuntoSesion validacionPuntoSesion);

    public void delete(Long id);
}
