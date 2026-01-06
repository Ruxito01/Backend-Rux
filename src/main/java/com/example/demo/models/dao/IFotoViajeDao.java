package com.example.demo.models.dao;

import com.example.demo.models.entity.FotoViaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFotoViajeDao extends JpaRepository<FotoViaje, Long> {
    List<FotoViaje> findByViajeId(Long viajeId);
}
