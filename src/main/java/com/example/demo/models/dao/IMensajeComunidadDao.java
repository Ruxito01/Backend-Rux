package com.example.demo.models.dao;

import com.example.demo.models.entity.MensajeComunidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * DAO para mensajes de chat en comunidades
 */
public interface IMensajeComunidadDao extends JpaRepository<MensajeComunidad, Long> {

    /**
     * Obtener mensajes de una comunidad ordenados por fecha
     */
    List<MensajeComunidad> findByComunidadIdOrderByFechaEnvioAsc(Long comunidadId);

    /**
     * Obtener últimos N mensajes de una comunidad
     */
    /**
     * Obtener mensajes paginados de una comunidad
     */
    Page<MensajeComunidad> findByComunidadId(Long comunidadId, Pageable pageable);
}
