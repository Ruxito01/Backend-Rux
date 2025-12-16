package com.example.demo.models.controller;

import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.service.IComunidadService;
import com.example.demo.models.service.IUsuarioService;
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

    @Operation(summary = "Obtener miembros de una comunidad")
    @GetMapping("/{id}/miembros")
    public ResponseEntity<Set<Usuario>> getMiembros(@PathVariable Long id) {
        Set<Usuario> miembros = comunidadService.getMiembrosByComunidadId(id);
        return miembros != null
                ? new ResponseEntity<>(miembros, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
            // Actualizar AMBOS lados de la relación ManyToMany
            comunidad.getMiembros().add(creador);
            creador.getComunidades().add(comunidad);

            Comunidad nuevaComunidad = comunidadService.save(comunidad);
            return new ResponseEntity<>(nuevaComunidad, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Actualizar comunidad existente")
    @PutMapping("/{id}")
    public ResponseEntity<Comunidad> update(@PathVariable Long id, @RequestBody Comunidad comunidad) {
        Comunidad existing = comunidadService.findById(id);
        if (existing == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        existing.setNombre(comunidad.getNombre());
        existing.setDescripcion(comunidad.getDescripcion());
        existing.setNivelPrivacidad(comunidad.getNivelPrivacidad());
        existing.setUrlImagen(comunidad.getUrlImagen());
        existing.setCreador(comunidad.getCreador());
        return new ResponseEntity<>(comunidadService.save(existing), HttpStatus.OK);
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
