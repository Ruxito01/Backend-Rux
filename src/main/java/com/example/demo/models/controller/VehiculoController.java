package com.example.demo.models.controller;

import com.example.demo.models.entity.Vehiculo;
import com.example.demo.models.service.IVehiculoService;
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
@RequestMapping("/api/vehiculo")
@Tag(name = "Vehiculo", description = "API para gestión de Vehiculo")
public class VehiculoController {

    @Autowired
    private IVehiculoService service;

    @Operation(summary = "Obtener todos los vehículos", description = "Retorna una lista con todos los vehículos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de vehículos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Vehiculo>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener vehículo por ID", description = "Retorna un vehículo específico según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado", content = @Content(schema = @Schema(implementation = Vehiculo.class))),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> findById(
            @Parameter(description = "ID del vehículo a buscar", required = true, example = "1") @PathVariable @NonNull Long id) {
        Vehiculo entity = service.findById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear nuevo vehículo", description = "Crea un nuevo vehículo en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo creado exitosamente", content = @Content(schema = @Schema(implementation = Vehiculo.class)))
    })
    @PostMapping
    public ResponseEntity<Vehiculo> create(
            @Parameter(description = "Datos del vehículo a crear", required = true) @RequestBody @NonNull Vehiculo entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Actualizar vehículo", description = "Actualiza los datos de un vehículo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo actualizado exitosamente", content = @Content(schema = @Schema(implementation = Vehiculo.class))),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> update(
            @Parameter(description = "ID del vehículo a actualizar", required = true, example = "1") @PathVariable @NonNull Long id,
            @Parameter(description = "Nuevos datos del vehículo", required = true) @RequestBody @NonNull Vehiculo entity) {
        Vehiculo existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Eliminar vehículo", description = "Elimina un vehículo del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del vehículo a eliminar", required = true, example = "1") @PathVariable @NonNull Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
