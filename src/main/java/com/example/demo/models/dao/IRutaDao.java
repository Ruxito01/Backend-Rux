package com.example.demo.models.dao;

import com.example.demo.models.entity.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRutaDao extends JpaRepository<Ruta, Long> {
    List<Ruta> findByComunidad_Id(Long comunidadId);

    @org.springframework.data.jpa.repository.Query("SELECT new com.example.demo.models.dto.RutaResumenDTO(" +
            "r.id, r.nombre, r.distanciaEstimadaKm, " +
            "r.latitudInicio, r.longitudInicio, r.latitudFin, r.longitudFin, " +
            "r.creador.id, r.creador.nombre, r.creador.apellido) " +
            "FROM Ruta r")
    List<com.example.demo.models.dto.RutaResumenDTO> findAllResumen();
}
