package com.example.demo.models.controller;

import com.example.demo.models.entity.Marca;
import com.example.demo.models.service.IMarcaService;
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

/**
 * Controlador REST para gestión de Marcas de vehículos
 */
@RestController
@RequestMapping("/api/marca")
@Tag(name = "Marca", description = "API para gestión de Marcas de vehículos")
public class MarcaController {

    @Autowired
    private IMarcaService service;

    @Operation(summary = "Obtener todas las marcas", description = "Retorna lista de todas las marcas con sus modelos")
    @ApiResponse(responseCode = "200", description = "Lista de marcas obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Marca>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener marca por ID", description = "Retorna una marca específica con sus modelos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca encontrada", content = @Content(schema = @Schema(implementation = Marca.class))),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Marca> findById(
            @Parameter(description = "ID de la marca", required = true, example = "1") @PathVariable @NonNull Long id) {
        Marca entity = service.findById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar marca por nombre", description = "Busca una marca por su nombre exacto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca encontrada"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<Marca> findByNombre(
            @Parameter(description = "Nombre de la marca", required = true, example = "Hyundai") @PathVariable @NonNull String nombre) {
        Marca entity = service.findByNombre(nombre);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear nueva marca", description = "Crea una nueva marca de vehículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca creada exitosamente", content = @Content(schema = @Schema(implementation = Marca.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe una marca con ese nombre")
    })
    @PostMapping
    public ResponseEntity<?> create(
            @Parameter(description = "Datos de la marca", required = true) @RequestBody @NonNull Marca entity) {
        // Validar nombre duplicado
        Marca existente = service.findByNombre(entity.getNombre());
        if (existente != null) {
            return ResponseEntity.status(409).body("Ya existe una marca con el nombre: " + entity.getNombre());
        }
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Actualizar marca", description = "Actualiza los datos de una marca existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe otra marca con ese nombre")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "ID de la marca", required = true) @PathVariable @NonNull Long id,
            @Parameter(description = "Nuevos datos de la marca", required = true) @RequestBody @NonNull Marca entity) {
        Marca existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        // Validar nombre duplicado (excepto si es el mismo registro)
        Marca conMismoNombre = service.findByNombre(entity.getNombre());
        if (conMismoNombre != null && !conMismoNombre.getId().equals(id)) {
            return ResponseEntity.status(409).body("Ya existe otra marca con el nombre: " + entity.getNombre());
        }
        entity.setId(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Eliminar marca", description = "Elimina una marca si no tiene modelos asociados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca eliminada exitosamente"),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar, tiene modelos asociados"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID de la marca", required = true) @PathVariable @NonNull Long id) {
        Marca existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        // Validar que no tenga modelos asociados
        long countModelos = service.countModelosByMarcaId(id);
        if (countModelos > 0) {
            return ResponseEntity.badRequest()
                    .body("No se puede eliminar la marca porque tiene " + countModelos + " modelo(s) asociado(s)");
        }
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Contar modelos de una marca", description = "Retorna la cantidad de modelos asociados a una marca")
    @ApiResponse(responseCode = "200", description = "Cantidad de modelos")
    @GetMapping("/{id}/count-modelos")
    public ResponseEntity<Long> countModelos(
            @Parameter(description = "ID de la marca", required = true) @PathVariable @NonNull Long id) {
        return ResponseEntity.ok(service.countModelosByMarcaId(id));
    }
}
