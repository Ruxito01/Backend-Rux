package com.example.demo.models.controller;

import com.example.demo.models.dao.IViajeDao;
import com.example.demo.models.dao.IUsuarioDao;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.entity.Viaje;
import com.example.demo.models.service.FirebaseMessagingService;
import com.example.demo.models.service.ViajeNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller para probar notificaciones manualmente
 */
@RestController
@RequestMapping("/api/test-notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionTestController {

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    @Autowired
    private ViajeNotificationService viajeNotificationService;

    @Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private IViajeDao viajeDao;

    /**
     * Endpoint para probar notificación directa a un usuario
     * GET /api/test-notificaciones/usuario/{id}
     */
    @GetMapping("/usuario/{id}")
    public ResponseEntity<Map<String, Object>> probarNotificacionUsuario(@PathVariable Long id) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            Usuario usuario = usuarioDao.findById(id).orElse(null);

            if (usuario == null) {
                respuesta.put("error", "Usuario no encontrado");
                return ResponseEntity.notFound().build();
            }

            respuesta.put("usuarioId", id);
            respuesta.put("nombre", usuario.getNombre());
            respuesta.put("email", usuario.getEmail());
            respuesta.put("fcmToken", usuario.getFcmToken() != null ? "Presente (oculto)" : "NULL");
            respuesta.put("tokenLength", usuario.getFcmToken() != null ? usuario.getFcmToken().length() : 0);

            if (usuario.getFcmToken() == null || usuario.getFcmToken().isEmpty()) {
                respuesta.put("error", "Usuario sin FCM token");
                return ResponseEntity.ok(respuesta);
            }

            // Enviar notificación de prueba
            String resultado = firebaseMessagingService.enviarNotificacionViaje(
                    usuario.getFcmToken(),
                    "🧪 Prueba de Notificación",
                    "Esta es una prueba manual desde el backend",
                    999L,
                    "test");

            respuesta.put("notificacionEnviada", resultado != null);
            respuesta.put("resultadoFirebase", resultado);

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            respuesta.put("error", e.getMessage());
            respuesta.put("stackTrace", e.getClass().getName());
            return ResponseEntity.internalServerError().body(respuesta);
        }
    }

    /**
     * Endpoint para buscar viajes en un rango de tiempo
     * GET
     * /api/test-notificaciones/buscar-viajes?desde=2026-01-08T10:00:00&hasta=2026-01-08T12:00:00
     */
    @GetMapping("/buscar-viajes")
    public ResponseEntity<Map<String, Object>> buscarViajes(
            @RequestParam String desde,
            @RequestParam String hasta) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime fechaDesde = LocalDateTime.parse(desde, formatter);
            LocalDateTime fechaHasta = LocalDateTime.parse(hasta, formatter);

            respuesta.put("busqueda", Map.of(
                    "desde", desde,
                    "hasta", hasta));

            List<Viaje> viajes = viajeNotificationService.buscarViajesEnRango(fechaDesde, fechaHasta);

            respuesta.put("viajesEncontrados", viajes.size());
            respuesta.put("viajes", viajes.stream().map(v -> Map.of(
                    "id", v.getId(),
                    "estado", v.getEstado(),
                    "fechaProgramada", v.getFechaProgramada() != null ? v.getFechaProgramada().toString() : "null",
                    "rutaNombre", v.getRuta() != null ? v.getRuta().getNombre() : "Sin nombre",
                    "participantes", v.getParticipantes() != null ? v.getParticipantes().size() : 0)).toList());

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            respuesta.put("error", e.getMessage());
            respuesta.put("stackTrace", e.getClass().getName());
            return ResponseEntity.internalServerError().body(respuesta);
        }
    }

    /**
     * Endpoint para enviar notificación a un viaje específico
     * POST /api/test-notificaciones/viaje/{id}
     */
    @PostMapping("/viaje/{id}")
    public ResponseEntity<Map<String, Object>> notificarViaje(@PathVariable Long id) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            Viaje viaje = viajeDao.findById(id).orElse(null);

            if (viaje == null) {
                respuesta.put("error", "Viaje no encontrado");
                return ResponseEntity.notFound().build();
            }

            respuesta.put("viajeId", id);
            respuesta.put("estado", viaje.getEstado());
            respuesta.put("fechaProgramada",
                    viaje.getFechaProgramada() != null ? viaje.getFechaProgramada().toString() : "null");

            // Intentar enviar notificación
            viajeNotificationService.enviarRecordatorioViaje(viaje, "test");

            respuesta.put("notificacionEnviada", true);

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            respuesta.put("error", e.getMessage());
            respuesta.put("stackTrace", e.getClass().getName());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(respuesta);
        }
    }
}
