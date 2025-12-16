package com.example.demo.models.ServiceImpl;

import com.example.demo.models.dao.IMensajeComunidadDao;
import com.example.demo.models.entity.MensajeComunidad;
import com.example.demo.models.entity.UbicacionUsuario;
import com.example.demo.models.service.IRuxSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación del servicio WebSocket
 * Maneja la lógica de broadcasting para chat y GPS
 */
@Service
public class RuxSocketServiceImpl implements IRuxSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private IMensajeComunidadDao mensajeComunidadDao;

    @Override
    @Transactional
    public void enviarMensaje(MensajeComunidad mensaje) {
        // 1. Asegurar que tiene fecha
        if (mensaje.getFechaEnvio() == null) {
            mensaje.setFechaEnvio(LocalDateTime.now());
        }

        // 2. Guardar en base de datos
        MensajeComunidad mensajeGuardado = mensajeComunidadDao.save(mensaje);

        // 3. Hacer broadcast a todos los suscritos a esta comunidad
        String destino = "/topic/comunidad/" + mensajeGuardado.getComunidad().getId();
        messagingTemplate.convertAndSend(destino, mensajeGuardado);

        System.out.println("💬 Mensaje enviado a " + destino);
    }

    @Override
    public void actualizarUbicacion(UbicacionUsuario ubicacion) {
        // NO guardar en DB - solo broadcast para performance
        // Enviar inmediatamente a todos los participantes del viaje
        String destino = "/topic/viaje/" + ubicacion.getViajeId();
        messagingTemplate.convertAndSend(destino, ubicacion);

        System.out.println("📍 Ubicación actualizada en " + destino +
                " - Lat: " + ubicacion.getLatitud() + ", Lng: " + ubicacion.getLongitud());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MensajeComunidad> obtenerHistorialMensajes(Long comunidadId) {
        // Retornar últimos 50 mensajes ordenados por fecha descendente
        return mensajeComunidadDao.findTop50ByComunidadIdOrderByFechaEnvioDesc(comunidadId);
    }
}
