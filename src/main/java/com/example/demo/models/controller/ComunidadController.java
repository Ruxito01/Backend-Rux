package com.example.demo.models.controller;

import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.service.IComunidadService;
import com.example.demo.models.service.IUsuarioService;
import com.example.demo.models.dao.IMiembroComunidadDao;
import com.example.demo.models.entity.MiembroComunidad;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/comunidad")
@Tag(name = "Comunidad", description = "API para gestión de Comunidad")
public class ComunidadController {

    @Autowired
    private IComunidadService comunidadService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IMiembroComunidadDao miembroComunidadDao;

    @Operation(summary = "Obtener todas las comunidades")
    @GetMapping
    public List<Comunidad> findAll() {
        return comunidadService.findAll();
    }

    @Operation(summary = "Obtener comunidad por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Comunidad> findById(@PathVariable Long id) {
        Comunidad comunidad = comunidadService.findById(id);
        return comunidad != null
                ? new ResponseEntity<>(comunidad, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Obtener miembros de una comunidad con fecha de unión")
    @GetMapping("/{id}/miembros")
    public ResponseEntity<List<Map<String, Object>>> getMiembros(@PathVariable Long id) {
        Set<Usuario> miembros = comunidadService.getMiembrosByComunidadId(id);
        if (miembros == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Obtener relaciones con fechas de unión - SOLO ACTIVOS
        List<MiembroComunidad> relaciones = miembroComunidadDao.findByComunidadId(id).stream()
                .filter(r -> "activo".equals(r.getEstado()))
                .toList();

        // Mapear usuarios con sus fechas de unión
        List<Map<String, Object>> resultado = new java.util.ArrayList<>();
        for (Usuario usuario : miembros) {
            Map<String, Object> miembroData = new java.util.HashMap<>();
            miembroData.put("id", usuario.getId());
            miembroData.put("nombre", usuario.getNombre());
            miembroData.put("apellido", usuario.getApellido());
            miembroData.put("alias", usuario.getAlias());
            miembroData.put("foto", usuario.getFoto());
            miembroData.put("email", usuario.getEmail());
            miembroData.put("ultimaActividad", usuario.getUltimaActividad());

            // Buscar fecha de unión
            relaciones.stream()
                    .filter(r -> r.getUsuario().getId().equals(usuario.getId()))
                    .findFirst()
                    .ifPresent(r -> miembroData.put("fechaUnion", r.getFechaUnion()));

            resultado.add(miembroData);
        }

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Crear nueva comunidad")
    @PostMapping
    public ResponseEntity<Comunidad> save(@RequestBody Map<String, Object> payload) {
        try {
            // Extraer creadorId del payload
            Object creadorIdObj = payload.get("creador_id");
            if (creadorIdObj == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Convertir a Long (puede venir como Integer desde JSON)
            Long creadorId = creadorIdObj instanceof Integer
                    ? ((Integer) creadorIdObj).longValue()
                    : (Long) creadorIdObj;

            // Buscar el usuario creador
            Usuario creador = usuarioService.findById(creadorId).orElse(null);
            if (creador == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Crear la comunidad
            Comunidad comunidad = new Comunidad();
            comunidad.setNombre((String) payload.get("nombre"));
            comunidad.setDescripcion((String) payload.get("descripcion"));
            comunidad.setNivelPrivacidad((String) payload.get("nivel_privacidad"));
            comunidad.setUrlImagen((String) payload.get("url_imagen"));
            comunidad.setCreador(creador);

            // Agregar al creador como miembro automáticamente
            comunidad.getMiembros().add(creador);
            creador.getComunidades().add(comunidad);

            Comunidad nuevaComunidad = comunidadService.save(comunidad);

            // Guardar en tabla intermedia con fechaUnion
            MiembroComunidad miembroComunidad = new MiembroComunidad();
            miembroComunidad.setUsuario(creador);
            miembroComunidad.setComunidad(nuevaComunidad);
            miembroComunidad.setFechaUnion(java.time.LocalDateTime.now());
            miembroComunidadDao.save(miembroComunidad);

            return new ResponseEntity<>(nuevaComunidad, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Actualizar comunidad existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Comunidad existing = comunidadService.findById(id);
        if (existing == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Validar que solo el creador puede actualizar
        Object usuarioIdObj = payload.get("usuarioId");
        if (usuarioIdObj != null) {
            Long usuarioId = usuarioIdObj instanceof Integer
                    ? ((Integer) usuarioIdObj).longValue()
                    : (Long) usuarioIdObj;

            if (!existing.getCreador().getId().equals(usuarioId)) {
                Map<String, String> error = Map.of("error", "Solo el creador puede editar esta comunidad");
                return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
            }
        }

        // Actualizar campos (NO permitir cambiar creador)
        if (payload.get("nombre") != null) {
            existing.setNombre((String) payload.get("nombre"));
        }
        if (payload.get("descripcion") != null) {
            existing.setDescripcion((String) payload.get("descripcion"));
        }
        if (payload.get("nivel_privacidad") != null) {
            existing.setNivelPrivacidad((String) payload.get("nivel_privacidad"));
        }
        if (payload.get("url_imagen") != null) {
            existing.setUrlImagen((String) payload.get("url_imagen"));
        }

        return new ResponseEntity<>(comunidadService.save(existing), HttpStatus.OK);
    }

    @Operation(summary = "Buscar comunidades por nombre")
    @GetMapping("/buscar")
    public ResponseEntity<List<Comunidad>> buscarComunidades(
            @RequestParam String query,
            @RequestParam Long usuarioId) {
        // Buscar todas las comunidades que coincidan con el query
        List<Comunidad> todas = comunidadService.findAll();

        // Filtrar por nombre que contenga el query
        List<Comunidad> resultados = todas.stream()
                .filter(c -> c.getNombre().toLowerCase().contains(query.toLowerCase()))
                .toList();

        // Excluir comunidades donde el usuario ya es miembro
        Usuario usuario = usuarioService.findById(usuarioId).orElse(null);
        if (usuario != null) {
            Set<Long> comunidadesUsuario = usuario.getComunidades().stream()
                    .map(Comunidad::getId)
                    .collect(java.util.stream.Collectors.toSet());

            resultados = resultados.stream()
                    .filter(c -> !comunidadesUsuario.contains(c.getId()))
                    .toList();
        }

        return new ResponseEntity<>(resultados, HttpStatus.OK);
    }

    @Operation(summary = "Unirse a una comunidad pública")
    @PostMapping("/{id}/unirse")
    public ResponseEntity<Map<String, String>> unirse(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        try {
            Comunidad comunidad = comunidadService.findById(id);
            if (comunidad == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Verificar que sea pública
            if (!"publica".equals(comunidad.getNivelPrivacidad())) {
                Map<String, String> error = Map.of(
                        "error", "Solo se puede unir directamente a comunidades públicas");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }

            // Obtener usuario
            Object usuarioIdObj = payload.get("usuarioId");
            Long usuarioId = usuarioIdObj instanceof Integer
                    ? ((Integer) usuarioIdObj).longValue()
                    : (Long) usuarioIdObj;

            Usuario usuario = usuarioService.findById(usuarioId).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Verificar que no sea ya miembro
            if (comunidad.getMiembros().contains(usuario)) {
                Map<String, String> error = Map.of(
                        "error", "Ya eres miembro de esta comunidad");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }

            // Agregar relación bidireccional
            comunidad.getMiembros().add(usuario);
            usuario.getComunidades().add(comunidad);

            // Guardar
            usuarioService.save(usuario);

            // Guardar en tabla intermedia con fechaUnion
            MiembroComunidad miembroComunidad = new MiembroComunidad();
            miembroComunidad.setUsuario(usuario);
            miembroComunidad.setComunidad(comunidad);
            miembroComunidad.setFechaUnion(java.time.LocalDateTime.now());
            miembroComunidadDao.save(miembroComunidad);

            Map<String, String> response = Map.of(
                    "mensaje", "Te has unido exitosamente a " + comunidad.getNombre());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = Map.of("error", "Error al unirse a la comunidad");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Salir de una comunidad")
    @PostMapping("/{id}/salir")
    public ResponseEntity<Map<String, String>> salir(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        try {
            Comunidad comunidad = comunidadService.findById(id);
            if (comunidad == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Obtener usuario
            Object usuarioIdObj = payload.get("usuarioId");
            Long usuarioId = usuarioIdObj instanceof Integer
                    ? ((Integer) usuarioIdObj).longValue()
                    : (Long) usuarioIdObj;

            // Validar que no sea el creador
            if (comunidad.getCreador().getId().equals(usuarioId)) {
                Map<String, String> error = Map.of(
                        "error", "El creador no puede salir de su propia comunidad");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }

            // Buscar la relación y cambiar estado a inactivo
            List<MiembroComunidad> relaciones = miembroComunidadDao.findByComunidadId(id);
            boolean found = false;

            for (MiembroComunidad relacion : relaciones) {
                if (relacion.getUsuario().getId().equals(usuarioId)) {
                    relacion.setEstado("inactivo");
                    miembroComunidadDao.save(relacion);
                    found = true;
                    break;
                }
            }

            if (!found) {
                Map<String, String> error = Map.of(
                        "error", "No eres miembro de esta comunidad");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }

            Map<String, String> response = Map.of(
                    "mensaje", "Has salido de " + comunidad.getNombre());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = Map.of("error", "Error al salir de la comunidad");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Reingresar a una comunidad")
    @PostMapping("/{id}/reingresar")
    public ResponseEntity<Map<String, String>> reingresar(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        try {
            Comunidad comunidad = comunidadService.findById(id);
            if (comunidad == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Obtener usuario
            Object usuarioIdObj = payload.get("usuarioId");
            Long usuarioId = usuarioIdObj instanceof Integer
                    ? ((Integer) usuarioIdObj).longValue()
                    : (Long) usuarioIdObj;

            Usuario usuario = usuarioService.findById(usuarioId).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Buscar si ya existe la relación
            List<MiembroComunidad> relaciones = miembroComunidadDao.findByComunidadId(id);
            MiembroComunidad relacionExistente = null;

            for (MiembroComunidad relacion : relaciones) {
                if (relacion.getUsuario().getId().equals(usuarioId)) {
                    relacionExistente = relacion;
                    break;
                }
            }

            if (relacionExistente != null) {
                // Ya existía, solo cambiar estado a activo
                if ("activo".equals(relacionExistente.getEstado())) {
                    Map<String, String> error = Map.of(
                            "error", "Ya eres miembro activo de esta comunidad");
                    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
                }
                relacionExistente.setEstado("activo");
                miembroComunidadDao.save(relacionExistente);
            } else {
                // No existía, crear nueva relación (para comunidades públicas)
                if (!"publica".equals(comunidad.getNivelPrivacidad())) {
                    Map<String, String> error = Map.of(
                            "error", "Solo puedes reingresar a comunidades públicas");
                    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
                }

                // Crear nueva relación
                comunidad.getMiembros().add(usuario);
                usuario.getComunidades().add(comunidad);
                usuarioService.save(usuario);

                MiembroComunidad nuevaRelacion = new MiembroComunidad();
                nuevaRelacion.setUsuario(usuario);
                nuevaRelacion.setComunidad(comunidad);
                nuevaRelacion.setFechaUnion(java.time.LocalDateTime.now());
                nuevaRelacion.setEstado("activo");
                miembroComunidadDao.save(nuevaRelacion);
            }

            Map<String, String> response = Map.of(
                    "mensaje", "Te has reintegrado a " + comunidad.getNombre());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = Map.of("error", "Error al reingresar a la comunidad");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Eliminar comunidad por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Comunidad existing = comunidadService.findById(id);
        if (existing == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        comunidadService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
