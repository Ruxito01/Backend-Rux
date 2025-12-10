package com.example.demo.models.dao;

import com.example.demo.models.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVehiculoDao extends JpaRepository<Vehiculo, Long> {
}
