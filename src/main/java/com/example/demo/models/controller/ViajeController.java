package com.example.demo.models.controller;

import com.example.demo.models.entity.Viaje;
import com.example.demo.models.service.IViajeService;
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
@RequestMapping("/api/viaje")
@Tag(name = "Viaje", description = "API para gestión de Viaje")
public class ViajeController {

    @Autowired
    private IViajeService service;

    @Operation(summary = "Obtener todos los viajes", description = "Retorna una lista con todos los viajes programados o en curso en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de viajes obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Viaje>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener viaje por ID", description = "Retorna un viaje específico según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Viaje encontrado", content = @Content(schema = @Schema(implementation = Viaje.class))),
            @ApiResponse(responseCode = "404", description = "Viaje no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Viaje> findById(
            @Parameter(description = "ID del viaje a buscar", required = true, example = "1") @PathVariable @NonNull Long id) {
        Viaje entity = service.findById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear nuevo viaje", description = "Crea un nuevo viaje en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Viaje creado exitosamente", content = @Content(schema = @Schema(implementation = Viaje.class)))
    })
    @PostMapping
    public ResponseEntity<Viaje> create(
            @Parameter(description = "Datos del viaje a crear", required = true) @RequestBody @NonNull Viaje entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Actualizar viaje", description = "Actualiza los datos de un viaje existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Viaje actualizado exitosamente", content = @Content(schema = @Schema(implementation = Viaje.class))),
            @ApiResponse(responseCode = "404", description = "Viaje no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Viaje> update(
            @Parameter(description = "ID del viaje a actualizar", required = true, example = "1") @PathVariable @NonNull Long id,
            @Parameter(description = "Nuevos datos del viaje", required = true) @RequestBody @NonNull Viaje entity) {
        Viaje existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Eliminar viaje", description = "Elimina un viaje del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Viaje eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Viaje no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del viaje a eliminar", required = true, example = "1") @PathVariable @NonNull Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
