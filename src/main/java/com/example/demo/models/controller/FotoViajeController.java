package com.example.demo.models.controller;

import com.example.demo.models.entity.FotoViaje;
import com.example.demo.models.service.IFotoViajeService;
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
@RequestMapping("/api/fotoviaje")
@Tag(name = "FotoViaje", description = "API para gestión de FotoViaje")
public class FotoViajeController {

    @Autowired
    private IFotoViajeService service;

    @Operation(summary = "Obtener todas las fotos de viajes", description = "Retorna una lista con todas las fotos subidas en viajes")
    @ApiResponse(responseCode = "200", description = "Lista de fotos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<FotoViaje>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener foto de viaje por ID", description = "Retorna una foto de viaje específica según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto encontrada", content = @Content(schema = @Schema(implementation = FotoViaje.class))),
            @ApiResponse(responseCode = "404", description = "Foto no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FotoViaje> findById(
            @Parameter(description = "ID de la foto a buscar", required = true, example = "1") @PathVariable @NonNull Long id) {
        FotoViaje entity = service.findById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear nueva foto de viaje", description = "Sube una nueva foto asociada a un viaje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto creada exitosamente", content = @Content(schema = @Schema(implementation = FotoViaje.class)))
    })
    @PostMapping
    public ResponseEntity<FotoViaje> create(
            @Parameter(description = "Datos de la foto a crear", required = true) @RequestBody @NonNull FotoViaje entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Actualizar foto de viaje", description = "Actualiza los datos de una foto de viaje existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto actualizada exitosamente", content = @Content(schema = @Schema(implementation = FotoViaje.class))),
            @ApiResponse(responseCode = "404", description = "Foto no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FotoViaje> update(
            @Parameter(description = "ID de la foto a actualizar", required = true, example = "1") @PathVariable @NonNull Long id,
            @Parameter(description = "Nuevos datos de la foto", required = true) @RequestBody @NonNull FotoViaje entity) {
        FotoViaje existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Eliminar foto de viaje", description = "Elimina una foto de viaje del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Foto no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la foto a eliminar", required = true, example = "1") @PathVariable @NonNull Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
