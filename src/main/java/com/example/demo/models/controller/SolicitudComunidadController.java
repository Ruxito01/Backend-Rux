package com.example.demo.models.controller;

import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.entity.SolicitudComunidad;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.service.IComunidadService;
import com.example.demo.models.service.ISolicitudComunidadService;
import com.example.demo.models.service.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solicitud-comunidad")
@Tag(name = "Solicitud Comunidad", description = "API para gestión de solicitudes de unión a comunidades")
public class SolicitudComunidadController {

    @Autowired
    private ISolicitudComunidadService solicitudService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IComunidadService comunidadService;

    @Operation(summary = "Obtener todas las solicitudes")
    @GetMapping
    public List<SolicitudComunidad> findAll() {
        return solicitudService.findAll();
    }

    @Operation(summary = "Obtener solicitud por ID")
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudComunidad> findById(@PathVariable Long id) {
        SolicitudComunidad solicitud = solicitudService.findById(id);
        return solicitud != null
                ? new ResponseEntity<>(solicitud, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Obtener solicitudes pendientes de una comunidad")
    @GetMapping("/comunidad/{comunidadId}/pendientes")
    public List<SolicitudComunidad> getPendientesByComunidad(@PathVariable Long comunidadId) {
        return solicitudService.findPendientesByComunidadId(comunidadId);
    }

    @Operation(summary = "Obtener solicitudes pendientes de un usuario")
    @GetMapping("/usuario/{usuarioId}/pendientes")
    public List<Map<String, Object>> getPendientesByUsuario(@PathVariable Long usuarioId) {
        List<SolicitudComunidad> solicitudes = solicitudService.findPendientesByUsuarioId(usuarioId);
        // Retornar solo los datos necesarios (comunidadId)
        return solicitudes.stream()
                .map(s -> Map.<String, Object>of(
                        "id", s.getId(),
                        "comunidadId", s.getComunidad().getId(),
                        "estado", s.getEstado()))
                .toList();
    }

    @Operation(summary = "Crear nueva solicitud para unirse a comunidad privada")
    @PostMapping
    public ResponseEntity<?> crearSolicitud(@RequestBody Map<String, Object> payload) {
        try {
            // Extraer datos del payload
            Object usuarioIdObj = payload.get("usuarioId");
            Object comunidadIdObj = payload.get("comunidadId");
            String mensaje = (String) payload.get("mensaje");

            if (usuarioIdObj == null || comunidadIdObj == null) {
                return new ResponseEntity<>(
                        Map.of("error", "usuarioId y comunidadId son requeridos"),
                        HttpStatus.BAD_REQUEST);
            }

            Long usuarioId = usuarioIdObj instanceof Integer
                    ? ((Integer) usuarioIdObj).longValue()
                    : (Long) usuarioIdObj;

            Long comunidadId = comunidadIdObj instanceof Integer
                    ? ((Integer) comunidadIdObj).longValue()
                    : (Long) comunidadIdObj;

            // Buscar usuario y comunidad
            Usuario usuario = usuarioService.findById(usuarioId).orElse(null);
            Comunidad comunidad = comunidadService.findById(comunidadId);

            if (usuario == null || comunidad == null) {
                return new ResponseEntity<>(
                        Map.of("error", "Usuario o comunidad no encontrados"),
                        HttpStatus.NOT_FOUND);
            }

            // Verificar que la comunidad sea privada
            if (!"privada".equals(comunidad.getNivelPrivacidad())) {
                return new ResponseEntity<>(
                        Map.of("error", "Solo se pueden enviar solicitudes a comunidades privadas"),
                        HttpStatus.BAD_REQUEST);
            }

            // Verificar que no sea ya miembro
            if (comunidad.getMiembros().contains(usuario)) {
                return new ResponseEntity<>(
                        Map.of("error", "Ya eres miembro de esta comunidad"),
                        HttpStatus.BAD_REQUEST);
            }

            // Verificar que no tenga ya una solicitud pendiente
            SolicitudComunidad existente = solicitudService.findPendienteByUsuarioAndComunidad(
                    usuarioId, comunidadId);
            if (existente != null) {
                return new ResponseEntity<>(
                        Map.of("error", "Ya tienes una solicitud pendiente para esta comunidad"),
                        HttpStatus.BAD_REQUEST);
            }

            // Crear la solicitud
            SolicitudComunidad solicitud = new SolicitudComunidad();
            solicitud.setUsuario(usuario);
            solicitud.setComunidad(comunidad);
            solicitud.setMensaje(mensaje);

            SolicitudComunidad nuevaSolicitud = solicitudService.save(solicitud);

            return new ResponseEntity<>(nuevaSolicitud, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    Map.of("error", "Error al crear la solicitud"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Aprobar solicitud y agregar usuario como miembro")
    @PutMapping("/{id}/aprobar")
    public ResponseEntity<?> aprobarSolicitud(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> payload) {
        try {
            Long respondidoPorId = null;
            if (payload != null && payload.get("respondidoPorId") != null) {
                Object respondidoPorIdObj = payload.get("respondidoPorId");
                respondidoPorId = respondidoPorIdObj instanceof Integer
                        ? ((Integer) respondidoPorIdObj).longValue()
                        : (Long) respondidoPorIdObj;
            }

            SolicitudComunidad solicitud = solicitudService.aprobarSolicitud(id, respondidoPorId);
            return new ResponseEntity<>(solicitud, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    Map.of("error", e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    Map.of("error", "Error al aprobar la solicitud"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Rechazar solicitud")
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarSolicitud(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> payload) {
        try {
            Long respondidoPorId = null;
            if (payload != null && payload.get("respondidoPorId") != null) {
                Object respondidoPorIdObj = payload.get("respondidoPorId");
                respondidoPorId = respondidoPorIdObj instanceof Integer
                        ? ((Integer) respondidoPorIdObj).longValue()
                        : (Long) respondidoPorIdObj;
            }

            SolicitudComunidad solicitud = solicitudService.rechazarSolicitud(id, respondidoPorId);
            return new ResponseEntity<>(solicitud, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    Map.of("error", e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    Map.of("error", "Error al rechazar la solicitud"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Eliminar solicitud")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        SolicitudComunidad existing = solicitudService.findById(id);
        if (existing == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        solicitudService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
