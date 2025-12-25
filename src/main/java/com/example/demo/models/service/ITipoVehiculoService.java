package com.example.demo.models.service;

import com.example.demo.models.entity.TipoVehiculo;
import java.util.List;

public interface ITipoVehiculoService {
    List<TipoVehiculo> findAll();

    TipoVehiculo findById(Long id);

    TipoVehiculo findByNombre(String nombre);

    TipoVehiculo save(TipoVehiculo entity);

    void deleteById(Long id);

    /**
     * Cuenta vehiculos asociados a un tipo de vehiculo
     */
    long countVehiculosByTipoId(Long tipoId);

    /**
     * Obtiene los tipos de vehículo de los vehículos de un usuario
     */
    List<TipoVehiculo> findByUsuarioId(Long usuarioId);
}
