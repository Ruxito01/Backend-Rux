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
        public ResponseEntity<?> create(
                        @Parameter(description = "Datos del viaje a crear", required = true) @RequestBody @NonNull Viaje entity) {
                try {
                        return ResponseEntity.ok(service.save(entity));
                } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.status(500).body("Error creating via: " + e.getMessage());
                }
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

        @Operation(summary = "Buscar viaje por código de invitación", description = "Retorna un viaje según su código de invitación único")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Viaje encontrado", content = @Content(schema = @Schema(implementation = Viaje.class))),
                        @ApiResponse(responseCode = "404", description = "Viaje no encontrado con ese código")
        })
        @GetMapping("/codigo/{codigoInvitacion}")
        public ResponseEntity<Viaje> findByCodigoInvitacion(
                        @Parameter(description = "Código de invitación del viaje", required = true, example = "ABC12345") @PathVariable @NonNull String codigoInvitacion) {
                Viaje viaje = service.findByCodigoInvitacion(codigoInvitacion);
                return viaje != null ? ResponseEntity.ok(viaje) : ResponseEntity.notFound().build();
        }

        @Operation(summary = "Agregar participante a viaje", description = "Agrega un usuario como participante de un viaje existente")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Participante agregado exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Viaje o usuario no encontrado")
        })
        @PostMapping("/{viajeId}/participante/{usuarioId}")
        public ResponseEntity<Void> agregarParticipante(
                        @Parameter(description = "ID del viaje", required = true, example = "1") @PathVariable @NonNull Long viajeId,
                        @Parameter(description = "ID del usuario a agregar", required = true, example = "1") @PathVariable @NonNull Long usuarioId) {
                boolean agregado = service.agregarParticipante(viajeId, usuarioId);
                return agregado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        }

        @Operation(summary = "Obtener viajes de un participante", description = "Retorna todos los viajes donde el usuario es participante")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de viajes obtenida exitosamente")
        })
        @GetMapping("/participante/{usuarioId}")
        public ResponseEntity<List<Viaje>> findByParticipanteId(
                        @Parameter(description = "ID del usuario participante", required = true, example = "1") @PathVariable @NonNull Long usuarioId,
                        @Parameter(description = "Estado del viaje (opcional)", required = false) @RequestParam(required = false) String estado) {
                return ResponseEntity.ok(service.findByParticipanteId(usuarioId, estado));
        }

        @Operation(summary = "Actualizar estado de viaje", description = "Actualiza únicamente el estado de un viaje (programado, en_curso, finalizado)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente", content = @Content(schema = @Schema(implementation = Viaje.class))),
                        @ApiResponse(responseCode = "404", description = "Viaje no encontrado"),
                        @ApiResponse(responseCode = "400", description = "Estado inválido")
        })
        @PutMapping("/{id}/estado")
        public ResponseEntity<Viaje> updateEstado(
                        @Parameter(description = "ID del viaje a actualizar", required = true, example = "1") @PathVariable @NonNull Long id,
                        @Parameter(description = "Nuevo estado del viaje", required = true) @RequestBody @NonNull java.util.Map<String, String> estadoMap) {
                Viaje existing = service.findById(id);
                if (existing == null) {
                        return ResponseEntity.notFound().build();
                }

                String nuevoEstado = estadoMap.get("estado");
                if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
                        return ResponseEntity.badRequest().build();
                }

                // Validar que el estado sea válido
                if (!nuevoEstado.equals("programado") && !nuevoEstado.equals("en_curso")
                                && !nuevoEstado.equals("finalizado") && !nuevoEstado.equals("cancelado")) {
                        return ResponseEntity.badRequest().build();
                }

                existing.setEstado(nuevoEstado);
                return ResponseEntity.ok(service.save(existing));
        }

        @Operation(summary = "Actualizar estado de participante", description = "Actualiza el estado de un participante en un viaje (registrado, ingresa, cancela, finaliza)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Estado de participante actualizado exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Viaje o participante no encontrado"),
                        @ApiResponse(responseCode = "400", description = "Estado inválido")
        })
        @PutMapping("/{viajeId}/participante/{usuarioId}/estado")
        public ResponseEntity<Void> updateParticipanteEstado(
                        @Parameter(description = "ID del viaje", required = true, example = "1") @PathVariable @NonNull Long viajeId,
                        @Parameter(description = "ID del participante", required = true, example = "1") @PathVariable @NonNull Long usuarioId,
                        @Parameter(description = "Nuevo estado y km recorridos (estado: ingresa, kmRecorridos: 15.5)", required = true) @RequestBody @NonNull java.util.Map<String, String> estadoMap) {
                String nuevoEstado = estadoMap.get("estado");
                if (nuevoEstado == null) {
                        return ResponseEntity.badRequest().build();
                }

                // Parsear km recorridos si viene en el body
                java.math.BigDecimal kmRecorridos = null;
                String kmRecorridosStr = estadoMap.get("kmRecorridos");
                if (kmRecorridosStr != null && !kmRecorridosStr.isEmpty()) {
                        try {
                                kmRecorridos = new java.math.BigDecimal(kmRecorridosStr);
                        } catch (NumberFormatException e) {
                                // Ignorar si no es un número válido
                        }
                }

                boolean updated = service.updateEstadoParticipante(viajeId, usuarioId, nuevoEstado, kmRecorridos);
                return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        }

        @Operation(summary = "Obtener viaje activo del usuario", description = "Retorna el viaje donde el usuario es participante, el viaje está 'en_curso' y el participante tiene estado 'ingresa'")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Viaje activo encontrado", content = @Content(schema = @Schema(implementation = Viaje.class))),
                        @ApiResponse(responseCode = "404", description = "No hay viaje activo que cumpla las condiciones")
        })
        @GetMapping("/activo/{usuarioId}")
        public ResponseEntity<Viaje> getViajeActivo(
                        @Parameter(description = "ID del usuario", required = true, example = "1") @PathVariable @NonNull Long usuarioId) {
                // Buscar viaje en estado "en_curso" donde el participante tiene estado
                // "ingresa"
                Viaje viajeActivo = service.findActiveViajeByUsuarioAndEstados(usuarioId, "en_curso", "ingresa");
                return viajeActivo != null ? ResponseEntity.ok(viajeActivo) : ResponseEntity.notFound().build();
        }

        @Operation(summary = "Obtener viajes por ruta", description = "Retorna todos los viajes asociados a una ruta específica")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de viajes obtenida exitosamente")
        })
        @GetMapping("/ruta/{rutaId}")
        public ResponseEntity<List<Viaje>> findByRutaId(
                        @Parameter(description = "ID de la ruta", required = true, example = "1") @PathVariable @NonNull Long rutaId) {
                return ResponseEntity.ok(service.findByRutaId(rutaId));
        }

        @Operation(summary = "Verificar conflicto de fechas", description = "Verifica si el usuario ya participa en viajes con fechas que se solapan con el viaje destino")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de viajes en conflicto (vacía si no hay)"),
                        @ApiResponse(responseCode = "404", description = "Viaje no encontrado")
        })
        @GetMapping("/{viajeId}/conflicto-fechas/{usuarioId}")
        public ResponseEntity<List<Viaje>> verificarConflictoFechas(
                        @Parameter(description = "ID del viaje destino", required = true, example = "1") @PathVariable @NonNull Long viajeId,
                        @Parameter(description = "ID del usuario", required = true, example = "1") @PathVariable @NonNull Long usuarioId) {
                Viaje viaje = service.findById(viajeId);
                if (viaje == null) {
                        return ResponseEntity.notFound().build();
                }
                List<Viaje> conflictos = service.verificarConflictoFechas(usuarioId, viajeId);
                return ResponseEntity.ok(conflictos);
        }

        @Operation(summary = "Obtener viajes por usuario y fecha", description = "Retorna los viajes activos del usuario en una fecha específica (mismo día)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de viajes del día")
        })
        @GetMapping("/usuario/{usuarioId}/fecha")
        public ResponseEntity<List<Viaje>> getViajesByUsuarioAndFecha(
                        @Parameter(description = "ID del usuario", required = true, example = "1") @PathVariable @NonNull Long usuarioId,
                        @Parameter(description = "Fecha a buscar (ISO format)", required = true) @RequestParam @NonNull String fecha) {
                java.time.LocalDateTime fechaDateTime = java.time.LocalDateTime.parse(fecha);
                return ResponseEntity.ok(service.findViajesByUsuarioAndFecha(usuarioId, fechaDateTime));
        }

        @Operation(summary = "Obtener viajes por comunidad", description = "Retorna los viajes asociados a una comunidad específica")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de viajes de la comunidad")
        })
        @GetMapping("/comunidad/{comunidadId}")
        public ResponseEntity<List<Viaje>> findByComunidadId(
                        @Parameter(description = "ID de la comunidad", required = true, example = "1") @PathVariable @NonNull Long comunidadId) {
                return ResponseEntity.ok(service.findByComunidadId(comunidadId));
        }
}
