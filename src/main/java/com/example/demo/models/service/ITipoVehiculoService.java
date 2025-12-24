package com.example.demo.models.service;

import com.example.demo.models.entity.TipoVehiculo;
import java.util.List;

public interface ITipoVehiculoService {
    List<TipoVehiculo> findAll();

    TipoVehiculo findById(Long id);

    TipoVehiculo findByNombre(String nombre);

    TipoVehiculo save(TipoVehiculo entity);

    void deleteById(Long id);
}
