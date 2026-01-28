package com.example.demo.models.controller;

import com.example.demo.models.entity.Logro;
import com.example.demo.models.service.ILogroService;
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
@RequestMapping("/api/logro")
@Tag(name = "Logro", description = "API para gestión de Logro")
public class LogroController {

    @Autowired
    private ILogroService service;

    @Operation(summary = "Obtener todos los logros", description = "Retorna una lista con todos los logros disponibles en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de logros obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Logro>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener logros con estadísticas", description = "Retorna lista de logros incluyendo cantidad de usuarios que lo han desbloqueado")
    @GetMapping("/stats")
    public ResponseEntity<List<java.util.Map<String, Object>>> findAllWithStats() {
        List<Object[]> resultados = service.findAllWithUserCount();
        List<java.util.Map<String, Object>> respuesta = new java.util.ArrayList<>();

        for (Object[] fila : resultados) {
            Logro logro = (Logro) fila[0];
            Long count = (Long) fila[1];

            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", logro.getId());
            map.put("nombre", logro.getNombre());
            map.put("descripcion", logro.getDescripcion());
            map.put("urlIcono", logro.getUrlIcono());
            map.put("criterioDesbloqueo", logro.getCriterioDesbloqueo());
            map.put("cantidadDesbloqueos", count);
            respuesta.add(map);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Obtener logro por ID", description = "Retorna un logro específico según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logro encontrado", content = @Content(schema = @Schema(implementation = Logro.class))),
            @ApiResponse(responseCode = "404", description = "Logro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Logro> findById(
            @Parameter(description = "ID del logro a buscar", required = true, example = "1") @PathVariable @NonNull Long id) {
        Logro entity = service.findById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear nuevo logro", description = "Crea un nuevo logro en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logro creado exitosamente", content = @Content(schema = @Schema(implementation = Logro.class)))
    })
    @PostMapping
    public ResponseEntity<Logro> create(
            @Parameter(description = "Datos del logro a crear", required = true) @RequestBody @NonNull Logro entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Actualizar logro", description = "Actualiza los datos de un logro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logro actualizado exitosamente", content = @Content(schema = @Schema(implementation = Logro.class))),
            @ApiResponse(responseCode = "404", description = "Logro no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Logro> update(
            @Parameter(description = "ID del logro a actualizar", required = true, example = "1") @PathVariable @NonNull Long id,
            @Parameter(description = "Nuevos datos del logro", required = true) @RequestBody @NonNull Logro entity) {
        Logro existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Eliminar logro", description = "Elimina un logro del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logro eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Logro no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del logro a eliminar", required = true, example = "1") @PathVariable @NonNull Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
