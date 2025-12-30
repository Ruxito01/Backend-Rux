package com.example.demo.models.controller;

import com.example.demo.models.entity.Ruta;
import com.example.demo.models.service.IRutaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ruta")
@Tag(name = "Ruta", description = "API para gestión de Ruta")
public class RutaController {

    @Autowired
    private IRutaService service;

    @Operation(summary = "Obtener todas las rutas", description = "Retorna una lista con todas las rutas planificadas en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de rutas obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Ruta>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener ruta por ID", description = "Retorna una ruta específica según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ruta encontrada", content = @Content(schema = @Schema(implementation = Ruta.class))),
            @ApiResponse(responseCode = "404", description = "Ruta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Ruta> findById(
            @Parameter(description = "ID de la ruta a buscar", required = true, example = "1") @PathVariable @NonNull Long id) {
        Ruta entity = service.findById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear nueva ruta", description = "Crea una nueva ruta planificada en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ruta creada exitosamente", content = @Content(schema = @Schema(implementation = Ruta.class)))
    })
    @PostMapping
    public ResponseEntity<Ruta> create(
            @Parameter(description = "Datos de la ruta a crear", required = true) @RequestBody @NonNull Ruta entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Actualizar ruta", description = "Actualiza los datos de una ruta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ruta actualizada exitosamente", content = @Content(schema = @Schema(implementation = Ruta.class))),
            @ApiResponse(responseCode = "404", description = "Ruta no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Ruta> update(
            @Parameter(description = "ID de la ruta a actualizar", required = true, example = "1") @PathVariable @NonNull Long id,
            @Parameter(description = "Nuevos datos de la ruta", required = true) @RequestBody @NonNull Ruta entity) {
        Ruta existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        entity.setId(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Eliminar ruta", description = "Elimina una ruta del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ruta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Ruta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la ruta a eliminar", required = true, example = "1") @PathVariable @NonNull Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener rutas por comunidad", description = "Retorna las rutas compartidas con una comunidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de rutas de la comunidad")
    })
    @GetMapping("/comunidad/{comunidadId}")
    public ResponseEntity<List<Ruta>> findByComunidad(
            @Parameter(description = "ID de la comunidad", required = true, example = "1") @PathVariable @NonNull Long comunidadId) {
        return ResponseEntity.ok(service.findByComunidadId(comunidadId));
    }

    @Operation(summary = "Obtener rutas para Dashboard", description = "Retorna todas las rutas con sus relaciones (Creador, Comunidad) cargadas EAGERly para evitar N+1")
    @GetMapping("/dashboard")
    public ResponseEntity<List<Ruta>> findAllForDashboard() {
        return ResponseEntity.ok(service.findAllWithRelations());
    }
}
