package com.example.demo.models.controller;

import com.example.demo.models.entity.AlertaViaje;
import com.example.demo.models.service.IAlertaViajeService;
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
@RequestMapping("/api/alertaviaje")
@Tag(name = "AlertaViaje", description = "API para gestión de AlertaViaje")
public class AlertaViajeController {

    @Autowired
    private IAlertaViajeService service;

    @Operation(summary = "Obtener todas las alertas de viaje", description = "Retorna una lista con todas las alertas emitidas durante viajes")
    @ApiResponse(responseCode = "200", description = "Lista de alertas obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<AlertaViaje>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener alerta de viaje por ID", description = "Retorna una alerta de viaje específica según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerta encontrada", content = @Content(schema = @Schema(implementation = AlertaViaje.class))),
            @ApiResponse(responseCode = "404", description = "Alerta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AlertaViaje> findById(
            @Parameter(description = "ID de la alerta a buscar", required = true, example = "1") @PathVariable @NonNull Long id) {
        AlertaViaje entity = service.findById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear nueva alerta de viaje", description = "Crea una nueva alerta durante un viaje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerta creada exitosamente", content = @Content(schema = @Schema(implementation = AlertaViaje.class)))
    })
    @PostMapping
    public ResponseEntity<AlertaViaje> create(
            @Parameter(description = "Datos de la alerta a crear", required = true) @RequestBody @NonNull AlertaViaje entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Actualizar alerta de viaje", description = "Actualiza los datos de una alerta de viaje existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerta actualizada exitosamente", content = @Content(schema = @Schema(implementation = AlertaViaje.class))),
            @ApiResponse(responseCode = "404", description = "Alerta no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AlertaViaje> update(
            @Parameter(description = "ID de la alerta a actualizar", required = true, example = "1") @PathVariable @NonNull Long id,
            @Parameter(description = "Nuevos datos de la alerta", required = true) @RequestBody @NonNull AlertaViaje entity) {
        AlertaViaje existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Eliminar alerta de viaje", description = "Elimina una alerta de viaje del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alerta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la alerta a eliminar", required = true, example = "1") @PathVariable @NonNull Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Crear alerta de viaje simplificada", description = "Crea una alerta recibiendo solo los IDs necesarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/crear")
    public ResponseEntity<?> crearAlertaSimplificada(@RequestBody @NonNull java.util.Map<String, Object> request) {
        try {
            Long viajeId = Long.valueOf(request.get("viajeId").toString());
            Long usuarioId = Long.valueOf(request.get("usuarioId").toString());
            String tipoAlerta = request.get("tipoAlerta").toString();
            java.math.BigDecimal latitud = new java.math.BigDecimal(request.get("latitud").toString());
            java.math.BigDecimal longitud = new java.math.BigDecimal(request.get("longitud").toString());
            String mensaje = request.get("mensaje") != null ? request.get("mensaje").toString() : null;

            AlertaViaje alerta = service.crearAlerta(viajeId, usuarioId, tipoAlerta, latitud, longitud, mensaje);

            // Retornar solo los campos necesarios para evitar lazy loading
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("id", alerta.getId());
            response.put("tipoAlerta", alerta.getTipoAlerta());
            response.put("mensaje", alerta.getMensaje());
            response.put("latitud", alerta.getLatitud());
            response.put("longitud", alerta.getLongitud());
            response.put("estaActiva", alerta.getEstaActiva());
            response.put("fechaReporte", alerta.getFechaReporte());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
}
