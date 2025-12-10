package com.example.demo.models.dao;

import com.example.demo.models.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IViajeDao extends JpaRepository<Viaje, Long> {
}
