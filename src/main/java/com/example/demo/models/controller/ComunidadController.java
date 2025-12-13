package com.example.demo.models.controller;

import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.service.IComunidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comunidad")
@Tag(name = "Comunidad", description = "API para gestión de Comunidad")
public class ComunidadController {

    @Autowired
    private IComunidadService comunidadService;

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

    @Operation(summary = "Crear nueva comunidad")
    @PostMapping
    public ResponseEntity<Comunidad> save(@RequestBody Comunidad comunidad) {
        Comunidad nuevaComunidad = comunidadService.save(comunidad);
        return new ResponseEntity<>(nuevaComunidad, HttpStatus.CREATED);
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
