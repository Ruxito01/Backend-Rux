package com.example.demo.models.service;

import com.example.demo.models.entity.MensajeComunidad;
import com.example.demo.models.entity.UbicacionUsuario;
import java.util.List;

/**
 * Servicio para WebSocket - Chat y GPS
 */
public interface IRuxSocketService {

    /**
     * Enviar mensaje de chat a comunidad
     * - Guarda en DB
     * - Hace broadcast a /topic/comunidad/{id}
     */
    void enviarMensaje(MensajeComunidad mensaje);

    /**
     * Actualizar ubicación GPS en tiempo real
     * - NO guarda en DB (performance)
     * - Solo hace broadcast a /topic/viaje/{viajeId}
     */
    void actualizarUbicacion(UbicacionUsuario ubicacion);

    /**
     * Obtener historial de mensajes de una comunidad
     */
    List<MensajeComunidad> obtenerHistorialMensajes(Long comunidadId);

    /**
     * Editar un mensaje existente
     * - Actualiza el contenido
     * - Marca como editado
     * - Hace broadcast a la comunidad
     */
    MensajeComunidad editarMensaje(Long mensajeId, String nuevoContenido);

    /**
     * Borrar un mensaje (soft delete)
     * - Cambia contenido a "Mensaje borrado"
     * - Marca como borrado
     * - Hace broadcast a la comunidad
     */
    MensajeComunidad borrarMensaje(Long mensajeId);
}
