package com.example.demo.models.controller;

import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.service.IComunidadService;
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
@RequestMapping("/api/comunidad")
@Tag(name = "Comunidad", description = "API para gestión de Comunidad")
public class ComunidadController {

    @Autowired
    private IComunidadService service;

    @Operation(summary = "Obtener todas las comunidades", description = "Retorna una lista con todas las comunidades registradas")
    @ApiResponse(responseCode = "200", description = "Lista de comunidades obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Comunidad>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener comunidad por ID", description = "Retorna una comunidad específica según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comunidad encontrada", content = @Content(schema = @Schema(implementation = Comunidad.class))),
            @ApiResponse(responseCode = "404", description = "Comunidad no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Comunidad> findById(
            @Parameter(description = "ID de la comunidad a buscar", required = true, example = "1") @PathVariable @NonNull Long id) {
        Comunidad entity = service.findById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear nueva comunidad", description = "Crea una nueva comunidad en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comunidad creada exitosamente", content = @Content(schema = @Schema(implementation = Comunidad.class)))
    })
    @PostMapping
    public ResponseEntity<Comunidad> create(
            @Parameter(description = "Datos de la comunidad a crear", required = true) @RequestBody @NonNull Comunidad entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Actualizar comunidad", description = "Actualiza los datos de una comunidad existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comunidad actualizada exitosamente", content = @Content(schema = @Schema(implementation = Comunidad.class))),
            @ApiResponse(responseCode = "404", description = "Comunidad no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Comunidad> update(
            @Parameter(description = "ID de la comunidad a actualizar", required = true, example = "1") @PathVariable @NonNull Long id,
            @Parameter(description = "Nuevos datos de la comunidad", required = true) @RequestBody @NonNull Comunidad entity) {
        Comunidad existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Eliminar comunidad", description = "Elimina una comunidad del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comunidad eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Comunidad no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la comunidad a eliminar", required = true, example = "1") @PathVariable @NonNull Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
