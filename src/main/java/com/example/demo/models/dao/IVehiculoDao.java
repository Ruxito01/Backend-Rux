package com.example.demo.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.entity.Vehiculo;

public interface IVehiculoDao extends CrudRepository<Vehiculo, Long> {
}
