package com.example.demo.models.service;

import com.example.demo.models.entity.Vehiculo;
import java.util.List;

public interface IVehiculoService {
    List<Vehiculo> findAll();
    Vehiculo findById(Long id);
    Vehiculo save(Vehiculo entity);
    void deleteById(Long id);
}
