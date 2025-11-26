package com.example.demo.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.entity.TipoVehiculo;

public interface ITipoVehiculoDao extends CrudRepository<TipoVehiculo, Long> {
}
