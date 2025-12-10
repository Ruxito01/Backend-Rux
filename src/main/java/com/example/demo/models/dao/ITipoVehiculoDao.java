package com.example.demo.models.dao;

import com.example.demo.models.entity.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITipoVehiculoDao extends JpaRepository<TipoVehiculo, Long> {
}
