package com.example.demo.models.service;

import com.example.demo.models.entity.SolicitudComunidad;

import java.util.List;

public interface ISolicitudComunidadService {

    /**
     * Buscar todas las solicitudes
     */
    List<SolicitudComunidad> findAll();

    /**
     * Buscar solicitud por ID
     */
    SolicitudComunidad findById(Long id);

    /**
     * Crear nueva solicitud
     */
    SolicitudComunidad save(SolicitudComunidad solicitud);

    /**
     * Eliminar solicitud
     */
    void deleteById(Long id);

    /**
     * Buscar solicitudes pendientes de una comunidad
     */
    List<SolicitudComunidad> findPendientesByComunidadId(Long comunidadId);

    /**
     * Buscar si existe solicitud pendiente del usuario para la comunidad
     */
    SolicitudComunidad findPendienteByUsuarioAndComunidad(Long usuarioId, Long comunidadId);

    /**
     * Buscar todas las solicitudes de un usuario
     */
    List<SolicitudComunidad> findByUsuarioId(Long usuarioId);

    /**
     * Buscar todas las solicitudes de una comunidad
     */
    List<SolicitudComunidad> findByComunidadId(Long comunidadId);

    /**
     * Aprobar solicitud y agregar usuario a la comunidad
     */
    SolicitudComunidad aprobarSolicitud(Long solicitudId, Long respondidoPorId);

    /**
     * Rechazar solicitud
     */
    SolicitudComunidad rechazarSolicitud(Long solicitudId, Long respondidoPorId);
}
