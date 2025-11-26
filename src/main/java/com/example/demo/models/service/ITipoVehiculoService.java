package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.TipoVehiculo;

public interface ITipoVehiculoService {
    public List<TipoVehiculo> findAll();

    public TipoVehiculo findById(Long id);

    public TipoVehiculo save(TipoVehiculo tipoVehiculo);

    public void delete(Long id);
}
