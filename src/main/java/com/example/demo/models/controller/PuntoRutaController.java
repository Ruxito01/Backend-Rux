package com.example.demo.models.controller;

import com.example.demo.models.entity.PuntoRuta;
import com.example.demo.models.service.IPuntoRutaService;
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
@RequestMapping("/api/puntoruta")
@Tag(name = "PuntoRuta", description = "API para gestión de PuntoRuta")
public class PuntoRutaController {

    @Autowired
    private IPuntoRutaService service;

    @Operation(summary = "Obtener todos los puntos de ruta", description = "Retorna una lista con todos los puntos de interés de las rutas")
    @ApiResponse(responseCode = "200", description = "Lista de puntos de ruta obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<PuntoRuta>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener punto de ruta por ID", description = "Retorna un punto de ruta específico según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Punto de ruta encontrado", content = @Content(schema = @Schema(implementation = PuntoRuta.class))),
            @ApiResponse(responseCode = "404", description = "Punto de ruta no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PuntoRuta> findById(
            @Parameter(description = "ID del punto de ruta a buscar", required = true, example = "1") @PathVariable @NonNull Long id) {
        PuntoRuta entity = service.findById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear nuevo punto de ruta", description = "Crea un nuevo punto de interés en una ruta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Punto de ruta creado exitosamente", content = @Content(schema = @Schema(implementation = PuntoRuta.class)))
    })
    @PostMapping
    public ResponseEntity<PuntoRuta> create(
            @Parameter(description = "Datos del punto de ruta a crear", required = true) @RequestBody @NonNull PuntoRuta entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Actualizar punto de ruta", description = "Actualiza los datos de un punto de ruta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Punto de ruta actualizado exitosamente", content = @Content(schema = @Schema(implementation = PuntoRuta.class))),
            @ApiResponse(responseCode = "404", description = "Punto de ruta no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PuntoRuta> update(
            @Parameter(description = "ID del punto de ruta a actualizar", required = true, example = "1") @PathVariable @NonNull Long id,
            @Parameter(description = "Nuevos datos del punto de ruta", required = true) @RequestBody @NonNull PuntoRuta entity) {
        PuntoRuta existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Eliminar punto de ruta", description = "Elimina un punto de ruta del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Punto de ruta eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Punto de ruta no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del punto de ruta a eliminar", required = true, example = "1") @PathVariable @NonNull Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener puntos por ruta", description = "Retorna todos los puntos de una ruta especifica ordenados por secuencia")
    @ApiResponse(responseCode = "200", description = "Lista de puntos de ruta obtenida exitosamente")
    @GetMapping("/ruta/{rutaId}")
    public ResponseEntity<List<PuntoRuta>> findByRutaId(
            @Parameter(description = "ID de la ruta", required = true, example = "1") @PathVariable @NonNull Long rutaId) {
        return ResponseEntity.ok(service.findByRutaId(rutaId));
    }
}
