package com.example.demo.models.dao;

import com.example.demo.models.entity.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRutaDao extends JpaRepository<Ruta, Long> {
    List<Ruta> findByComunidad_Id(Long comunidadId);

    /**
     * Obtiene todas las rutas con sus relaciones principales cargadas (Creador).
     */
    @org.springframework.data.jpa.repository.Query("SELECT r FROM Ruta r " +
            "LEFT JOIN FETCH r.creador " +
            "LEFT JOIN FETCH r.comunidad " +
            "ORDER BY r.nombre ASC")
    List<Ruta> findAllWithRelations();
}
