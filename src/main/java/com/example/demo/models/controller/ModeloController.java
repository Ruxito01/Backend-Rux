package com.example.demo.models.controller;

import com.example.demo.models.entity.Marca;
import com.example.demo.models.entity.Modelo;
import com.example.demo.models.service.IMarcaService;
import com.example.demo.models.service.IModeloService;
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
import java.util.Map;

/**
 * Controlador REST para gestión de Modelos de vehículos
 */
@RestController
@RequestMapping("/api/modelo")
@Tag(name = "Modelo", description = "API para gestión de Modelos de vehículos")
public class ModeloController {

    @Autowired
    private IModeloService service;

    @Autowired
    private IMarcaService marcaService;

    @Operation(summary = "Obtener todos los modelos", description = "Retorna lista de todos los modelos")
    @ApiResponse(responseCode = "200", description = "Lista de modelos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Modelo>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener modelo por ID", description = "Retorna un modelo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modelo encontrado", content = @Content(schema = @Schema(implementation = Modelo.class))),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Modelo> findById(
            @Parameter(description = "ID del modelo", required = true, example = "1") @PathVariable @NonNull Long id) {
        Modelo entity = service.findById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obtener modelos por marca", description = "Retorna todos los modelos de una marca específica")
    @ApiResponse(responseCode = "200", description = "Lista de modelos de la marca")
    @GetMapping("/por-marca/{marcaId}")
    public ResponseEntity<List<Modelo>> findByMarcaId(
            @Parameter(description = "ID de la marca", required = true, example = "1") @PathVariable @NonNull Long marcaId) {
        return ResponseEntity.ok(service.findByMarcaId(marcaId));
    }

    @Operation(summary = "Buscar modelos por nombre", description = "Busca modelos cuyo nombre contenga el texto dado")
    @ApiResponse(responseCode = "200", description = "Lista de modelos encontrados")
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<Modelo>> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre", required = true, example = "i10") @PathVariable @NonNull String nombre) {
        return ResponseEntity.ok(service.buscarPorNombre(nombre));
    }

    @Operation(summary = "Crear nuevo modelo", description = "Crea un nuevo modelo asociado a una marca")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modelo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Marca no especificada o no encontrada")
    })
    @PostMapping
    public ResponseEntity<?> create(
            @Parameter(description = "Datos del modelo con marcaId", required = true) @RequestBody @NonNull Map<String, Object> body) {

        // Extraer datos del body
        String nombre = (String) body.get("nombre");
        Long marcaId = body.get("marcaId") != null ? Long.valueOf(body.get("marcaId").toString()) : null;

        if (nombre == null || nombre.isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre del modelo es obligatorio");
        }

        if (marcaId == null) {
            return ResponseEntity.badRequest().body("El ID de la marca es obligatorio");
        }

        Marca marca = marcaService.findById(marcaId);
        if (marca == null) {
            return ResponseEntity.badRequest().body("Marca no encontrada con ID: " + marcaId);
        }

        Modelo modelo = new Modelo();
        modelo.setNombre(nombre);
        modelo.setMarca(marca);

        return ResponseEntity.ok(service.save(modelo));
    }

    @Operation(summary = "Actualizar modelo", description = "Actualiza los datos de un modelo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modelo actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "ID del modelo", required = true) @PathVariable @NonNull Long id,
            @Parameter(description = "Nuevos datos del modelo", required = true) @RequestBody @NonNull Map<String, Object> body) {

        Modelo existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        String nombre = (String) body.get("nombre");
        Long marcaId = body.get("marcaId") != null ? Long.valueOf(body.get("marcaId").toString()) : null;

        if (nombre != null && !nombre.isEmpty()) {
            existing.setNombre(nombre);
        }

        if (marcaId != null) {
            Marca marca = marcaService.findById(marcaId);
            if (marca == null) {
                return ResponseEntity.badRequest().body("Marca no encontrada con ID: " + marcaId);
            }
            existing.setMarca(marca);
        }

        return ResponseEntity.ok(service.save(existing));
    }

    @Operation(summary = "Eliminar modelo", description = "Elimina un modelo del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modelo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del modelo", required = true) @PathVariable @NonNull Long id) {
        Modelo existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
