package com.example.demo.models.controller;

import com.example.demo.models.entity.TipoVehiculo;
import com.example.demo.models.service.ITipoVehiculoService;
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
@RequestMapping("/api/tipovehiculo")
@Tag(name = "TipoVehiculo", description = "API para gestión de TipoVehiculo")
public class TipoVehiculoController {

    @Autowired
    private ITipoVehiculoService service;

    @Operation(summary = "Obtener todos los tipos de vehículo", description = "Retorna una lista con todos los tipos de vehículos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de tipos de vehículo obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<TipoVehiculo>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener tipo de vehículo por ID", description = "Retorna un tipo de vehículo específico según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de vehículo encontrado", content = @Content(schema = @Schema(implementation = TipoVehiculo.class))),
            @ApiResponse(responseCode = "404", description = "Tipo de vehículo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoVehiculo> findById(
            @Parameter(description = "ID del tipo de vehículo a buscar", required = true, example = "1") @PathVariable @NonNull Long id) {
        TipoVehiculo entity = service.findById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear nuevo tipo de vehículo", description = "Crea un nuevo tipo de vehículo en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de vehículo creado exitosamente", content = @Content(schema = @Schema(implementation = TipoVehiculo.class)))
    })
    @PostMapping
    public ResponseEntity<TipoVehiculo> create(
            @Parameter(description = "Datos del tipo de vehículo a crear", required = true) @RequestBody @NonNull TipoVehiculo entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Actualizar tipo de vehículo", description = "Actualiza los datos de un tipo de vehículo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de vehículo actualizado exitosamente", content = @Content(schema = @Schema(implementation = TipoVehiculo.class))),
            @ApiResponse(responseCode = "404", description = "Tipo de vehículo no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TipoVehiculo> update(
            @Parameter(description = "ID del tipo de vehículo a actualizar", required = true, example = "1") @PathVariable @NonNull Long id,
            @Parameter(description = "Nuevos datos del tipo de vehículo", required = true) @RequestBody @NonNull TipoVehiculo entity) {
        TipoVehiculo existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Eliminar tipo de vehículo", description = "Elimina un tipo de vehículo del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de vehículo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tipo de vehículo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del tipo de vehículo a eliminar", required = true, example = "1") @PathVariable @NonNull Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
