package com.example.demo.models.controller;

import com.example.demo.models.entity.MensajeComunidad;
import com.example.demo.models.entity.UbicacionUsuario;
import com.example.demo.models.service.IRuxSocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller WebSocket para chat y GPS tracking
 * 
 * Endpoints WebSocket:
 * - /app/chat.enviar → Enviar mensaje a comunidad
 * - /app/gps.actualizar → Actualizar ubicación GPS
 * 
 * Endpoints REST (para historial):
 * - GET /api/chat/historial/{comunidadId} → Obtener historial de mensajes
 */
@Controller
@Tag(name = "WebSocket RÜX", description = "Chat y GPS en tiempo real")
public class RuxWebSocketController {

    @Autowired
    private IRuxSocketService ruxSocketService;

    /**
     * Recibe un mensaje de chat y lo broadcast a la comunidad
     * Cliente envía a: /app/chat.enviar
     * Server broadcast a: /topic/comunidad/{id}
     */
    @MessageMapping("/chat.enviar")
    public void enviarMensaje(@Payload MensajeComunidad mensaje) {
        System.out.println("📨 Recibido mensaje para comunidad " + mensaje.getComunidad().getId());
        ruxSocketService.enviarMensaje(mensaje);
    }

    /**
     * Recibe actualización de ubicación GPS y la broadcast al viaje
     * Cliente envía a: /app/gps.actualizar
     * Server broadcast a: /topic/viaje/{viajeId}
     */
    @MessageMapping("/gps.actualizar")
    public void actualizarUbicacion(@Payload UbicacionUsuario ubicacion) {
        ruxSocketService.actualizarUbicacion(ubicacion);
    }

    /**
     * Endpoint REST para obtener historial de mensajes (útil al cargar el chat)
     */
    @Operation(summary = "Obtener historial de mensajes de una comunidad")
    @GetMapping("/api/chat/historial/{comunidadId}")
    @ResponseBody
    public ResponseEntity<List<MensajeComunidad>> obtenerHistorial(@PathVariable Long comunidadId) {
        List<MensajeComunidad> mensajes = ruxSocketService.obtenerHistorialMensajes(comunidadId);
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }
}
