package com.example.demo.models.dao;

import com.example.demo.models.entity.PuntoRuta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPuntoRutaDao extends JpaRepository<PuntoRuta, Long> {
    // Buscar puntos por ID de ruta
    List<PuntoRuta> findByRuta_IdOrderByOrdenSecuenciaAsc(Long rutaId);
}
