package com.example.demo.models.dao;

import com.example.demo.models.entity.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRutaDao extends JpaRepository<Ruta, Long> {
    List<Ruta> findByComunidad_Id(Long comunidadId);
}
