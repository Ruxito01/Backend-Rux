package com.example.demo.models.dao;

import com.example.demo.models.entity.SolicitudComunidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ISolicitudComunidadDao extends JpaRepository<SolicitudComunidad, Long> {

    /**
     * Busca todas las solicitudes pendientes de una comunidad
     */
    @Query("SELECT s FROM SolicitudComunidad s WHERE s.comunidad.id = :comunidadId AND s.estado = 'pendiente'")
    List<SolicitudComunidad> findPendientesByComunidadId(@Param("comunidadId") Long comunidadId);

    /**
     * Busca si existe una solicitud activa (pendiente) del usuario para la
     * comunidad
     */
    @Query("SELECT s FROM SolicitudComunidad s WHERE s.usuario.id = :usuarioId AND s.comunidad.id = :comunidadId AND s.estado = 'pendiente'")
    Optional<SolicitudComunidad> findPendienteByUsuarioAndComunidad(
            @Param("usuarioId") Long usuarioId,
            @Param("comunidadId") Long comunidadId);

    /**
     * Busca todas las solicitudes de un usuario
     */
    @Query("SELECT s FROM SolicitudComunidad s WHERE s.usuario.id = :usuarioId")
    List<SolicitudComunidad> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca solicitudes pendientes de un usuario
     */
    @Query("SELECT s FROM SolicitudComunidad s WHERE s.usuario.id = :usuarioId AND s.estado = 'pendiente'")
    List<SolicitudComunidad> findPendientesByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca todas las solicitudes de una comunidad (todas, no solo pendientes)
     */
    @Query("SELECT s FROM SolicitudComunidad s WHERE s.comunidad.id = :comunidadId")
    List<SolicitudComunidad> findByComunidadId(@Param("comunidadId") Long comunidadId);
}
