package com.example.demo.models.controller;

import com.example.demo.models.entity.Viaje;
import com.example.demo.models.service.IViajeService;
import com.example.demo.models.service.ViajeNotificationService;
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

        @Autowired
        private ViajeNotificationService notificationService;

        @Operation(summary = "Obtener todos los viajes", description = "Retorna una lista con todos los viajes programados o en curso en el sistema")
        @ApiResponse(responseCode = "200", description = "Lista de viajes obtenida exitosamente")
        @GetMapping
        public ResponseEntity<List<Viaje>> findAll() {
                // USAR VERSIÓN OPTIMIZADA: Carga relaciones en 1 sola query (evita N+1)
                return ResponseEntity.ok(service.findAllWithRelations());
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
                        Viaje viajeGuardado = service.save(entity);

                        // Si el viaje tiene comunidad asociada, notificar a los miembros
                        if (viajeGuardado.getComunidad() != null && viajeGuardado.getComunidad().getId() != null) {
                                String nombreUsuario = "Un usuario";
                                String nombreRuta = "una ruta";
                                Long organizadorId = null;

                                if (viajeGuardado.getOrganizador() != null) {
                                        organizadorId = viajeGuardado.getOrganizador().getId();
                                        if (viajeGuardado.getOrganizador().getNombre() != null) {
                                                nombreUsuario = viajeGuardado.getOrganizador().getNombre();
                                        }
                                }

                                if (viajeGuardado.getRuta() != null && viajeGuardado.getRuta().getNombre() != null) {
                                        nombreRuta = viajeGuardado.getRuta().getNombre();
                                }

                                // Enviar notificación push a miembros de la comunidad
                                notificationService.notificarRutaCompartida(
                                                viajeGuardado.getComunidad().getId(),
                                                nombreUsuario,
                                                nombreRuta,
                                                organizadorId);
                        }

                        return ResponseEntity.ok(viajeGuardado);
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

                // Si el nuevo estado es 'finalizado', calcular estadísticas del viaje
                if ("finalizado".equals(nuevoEstado)) {
                        // Establecer fecha_fin_real si no existe
                        if (existing.getFechaFinReal() == null) {
                                existing.setFechaFinReal(java.time.LocalDateTime.now());
                        }

                        // Calcular tiempo total de movimiento en minutos
                        if (existing.getFechaInicioReal() != null
                                        && existing.getTiempoTotalMovimientoMinutos() == null) {
                                long minutos = java.time.Duration.between(
                                                existing.getFechaInicioReal(),
                                                existing.getFechaFinReal()).toMinutes();
                                existing.setTiempoTotalMovimientoMinutos((int) minutos);
                        }

                        // CAMBIAR ESTADO DE TODOS LOS PARTICIPANTES QUE AÚN ESTÁN 'INGRESA' A
                        // 'FINALIZA'
                        if (existing.getParticipantes() != null) {
                                java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
                                for (com.example.demo.models.entity.ParticipanteViaje p : existing.getParticipantes()) {
                                        if (p.getEstado() == com.example.demo.models.entity.EstadoParticipante.ingresa) {
                                                p.setEstado(com.example.demo.models.entity.EstadoParticipante.finaliza);
                                                p.setFechaFinIndividual(ahora);

                                                // Calcular tiempo individual si tiene fecha de inicio
                                                if (p.getFechaInicioIndividual() != null) {
                                                        long minutosIndividuales = java.time.Duration.between(
                                                                        p.getFechaInicioIndividual(), ahora)
                                                                        .toMinutes();
                                                        p.setTiempoIndividualMinutos((int) minutosIndividuales);

                                                        // Calcular velocidad individual si tiene km
                                                        if (p.getKmRecorridos() != null && minutosIndividuales > 0) {
                                                                double horas = minutosIndividuales / 60.0;
                                                                double velocidad = p.getKmRecorridos().doubleValue()
                                                                                / horas;
                                                                p.setVelocidadIndividual(
                                                                                java.math.BigDecimal.valueOf(velocidad)
                                                                                                .setScale(2, java.math.RoundingMode.HALF_UP));
                                                        }
                                                }
                                                System.out.println("👤 Participante " + p.getUsuario().getId()
                                                                + " cambiado a 'finaliza'");
                                        }
                                }
                        }

                        // Calcular estadísticas basadas en participantes
                        if (existing.getParticipantes() != null && !existing.getParticipantes().isEmpty()) {
                                // Calcular promedio de km recorridos
                                java.math.BigDecimal totalKm = existing.getParticipantes().stream()
                                                .filter(p -> p.getKmRecorridos() != null)
                                                .map(p -> p.getKmRecorridos())
                                                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                                long count = existing.getParticipantes().stream()
                                                .filter(p -> p.getKmRecorridos() != null)
                                                .count();

                                if (count > 0 && existing.getDistanciaTotalRealKm() == null) {
                                        java.math.BigDecimal promedio = totalKm.divide(
                                                        java.math.BigDecimal.valueOf(count),
                                                        2, java.math.RoundingMode.HALF_UP);
                                        existing.setDistanciaTotalRealKm(promedio);

                                        // Calcular velocidad promedio (km/h)
                                        if (existing.getTiempoTotalMovimientoMinutos() != null
                                                        && existing.getTiempoTotalMovimientoMinutos() > 0
                                                        && existing.getVelocidadPromedioGrupo() == null) {
                                                double horas = existing.getTiempoTotalMovimientoMinutos() / 60.0;
                                                double velocidad = promedio.doubleValue() / horas;
                                                existing.setVelocidadPromedioGrupo(
                                                                java.math.BigDecimal.valueOf(velocidad).setScale(2,
                                                                                java.math.RoundingMode.HALF_UP));
                                        }
                                }

                                // Calcular tiempo promedio grupal
                                int sumaTiempos = existing.getParticipantes().stream()
                                                .filter(p -> p.getTiempoIndividualMinutos() != null)
                                                .mapToInt(p -> p.getTiempoIndividualMinutos())
                                                .sum();
                                long countTiempos = existing.getParticipantes().stream()
                                                .filter(p -> p.getTiempoIndividualMinutos() != null)
                                                .count();
                                if (countTiempos > 0 && existing.getTiempoPromedioGrupoMinutos() == null) {
                                        existing.setTiempoPromedioGrupoMinutos((int) (sumaTiempos / countTiempos));
                                }
                        }

                        System.out.println("📊 Estadísticas calculadas para viaje " + id + ": " +
                                        "distancia=" + existing.getDistanciaTotalRealKm() + " km, " +
                                        "tiempo=" + existing.getTiempoTotalMovimientoMinutos() + " min, " +
                                        "velocidad=" + existing.getVelocidadPromedioGrupo() + " km/h");
                }

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

        @Operation(summary = "Obtener viajes de la comunidad", description = "Retorna los viajes asociados a una comunidad específica")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de viajes obtenida exitosamente")
        })
        @GetMapping("/comunidad/{comunidadId}")
        public ResponseEntity<List<Viaje>> findByComunidadId(
                        @Parameter(description = "ID de la comunidad", required = true, example = "1") @PathVariable @NonNull Long comunidadId) {
                return ResponseEntity.ok(service.findByComunidadId(comunidadId));
        }

        @Operation(summary = "Obtener viajes recientes", description = "Retorna los viajes de los últimos X días")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de viajes recientes")
        })
        @GetMapping("/recientes")
        public ResponseEntity<List<Viaje>> getRecientes(
                        @Parameter(description = "Días hacia atrás (default 7)", required = false) @RequestParam(defaultValue = "7") int dias) {
                return ResponseEntity.ok(service.findRecientes(dias));
        }

        @Operation(summary = "Cancelar viaje (organizador)", description = "Cancela el viaje completo y notifica a todos los participantes. Solo el organizador puede ejecutar esta acción.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Viaje cancelado exitosamente"),
                        @ApiResponse(responseCode = "403", description = "El usuario no es el organizador"),
                        @ApiResponse(responseCode = "404", description = "Viaje no encontrado")
        })
        @PutMapping("/{viajeId}/cancelar")
        public ResponseEntity<Void> cancelarViaje(
                        @Parameter(description = "ID del viaje", required = true) @PathVariable @NonNull Long viajeId,
                        @Parameter(description = "ID del organizador que cancela", required = true) @RequestParam @NonNull Long organizadorId) {

                Viaje viaje = service.findById(viajeId);
                if (viaje == null) {
                        return ResponseEntity.notFound().build();
                }

                // Verificar que es el organizador
                if (viaje.getOrganizador() == null || !viaje.getOrganizador().getId().equals(organizadorId)) {
                        return ResponseEntity.status(403).build();
                }

                // Obtener nombre del organizador antes de cancelar
                String nombreOrganizador = viaje.getOrganizador().getNombre() != null
                                ? viaje.getOrganizador().getNombre()
                                : "El organizador";

                // Cancelar el viaje y todos los participantes
                boolean cancelado = service.cancelarViajeCompleto(viajeId);
                if (!cancelado) {
                        return ResponseEntity.status(500).build();
                }

                // Enviar notificaciones push a todos los participantes
                notificationService.notificarViajeCancelado(viaje, nombreOrganizador, organizadorId);

                return ResponseEntity.ok().build();
        }

        @Operation(summary = "Re-admitir participante", description = "Permite que un participante que canceló vuelva a unirse al viaje. Solo el organizador puede ejecutar esta acción.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Participante re-admitido exitosamente"),
                        @ApiResponse(responseCode = "403", description = "El solicitante no es el organizador"),
                        @ApiResponse(responseCode = "404", description = "Viaje o participante no encontrado")
        })
        @PutMapping("/{viajeId}/participante/{usuarioId}/readmitir")
        public ResponseEntity<Void> readmitirParticipante(
                        @Parameter(description = "ID del viaje", required = true) @PathVariable @NonNull Long viajeId,
                        @Parameter(description = "ID del participante a re-admitir", required = true) @PathVariable @NonNull Long usuarioId,
                        @Parameter(description = "ID del organizador", required = true) @RequestParam @NonNull Long organizadorId) {

                Viaje viaje = service.findById(viajeId);
                if (viaje == null) {
                        return ResponseEntity.notFound().build();
                }

                // Verificar que es el organizador
                if (viaje.getOrganizador() == null || !viaje.getOrganizador().getId().equals(organizadorId)) {
                        return ResponseEntity.status(403).build();
                }

                // Cambiar estado de 'cancela' a 'registrado'
                boolean readmitido = service.updateEstadoParticipante(viajeId, usuarioId, "registrado", null);
                return readmitido ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        }

        @Operation(summary = "Solicitar readmisión (usuario)", description = "Un participante que abandonó el viaje solicita volver a unirse.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Solicitud enviada exitosamente"),
                        @ApiResponse(responseCode = "400", description = "No cumple requisitos (tiempo > 15m)"),
                        @ApiResponse(responseCode = "404", description = "Viaje o usuario no encontrado")
        })
        @PostMapping("/{viajeId}/readmision/solicitar")
        public ResponseEntity<Void> solicitarReadmision(
                        @Parameter(description = "ID del viaje", required = true) @PathVariable @NonNull Long viajeId,
                        @Parameter(description = "ID del usuario solicitante", required = true) @RequestParam @NonNull Long usuarioId) {
                boolean exito = service.solicitarReadmision(viajeId, usuarioId);
                return exito ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
        }

        @Operation(summary = "Responder readmisión (organizador)", description = "El organizador acepta o rechaza la solicitud de readmisión.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Respuesta procesada exitosamente"),
                        @ApiResponse(responseCode = "403", description = "El usuario no es el organizador")
        })
        @PostMapping("/{viajeId}/readmision/responder")
        public ResponseEntity<Void> responderReadmision(
                        @Parameter(description = "ID del viaje", required = true) @PathVariable @NonNull Long viajeId,
                        @Parameter(description = "ID del participante", required = true) @RequestParam @NonNull Long usuarioId,
                        @Parameter(description = "Aceptado (true/false)", required = true) @RequestParam boolean aceptado,
                        @Parameter(description = "ID del organizador", required = true) @RequestParam @NonNull Long organizadorId) {

                // Validar organizador
                Viaje viaje = service.findById(viajeId);
                if (viaje == null || viaje.getOrganizador() == null
                                || !viaje.getOrganizador().getId().equals(organizadorId)) {
                        return ResponseEntity.status(403).build();
                }

                boolean exito = service.responderReadmision(viajeId, usuarioId, aceptado);
                return exito ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
        }

        @Operation(summary = "Verificar reingreso disponible", description = "Busca viajes donde el usuario puede solicitar reingreso (canceló hace < 15 min).")
        @GetMapping("/usuario/{usuarioId}/reingreso-disponible")
        public ResponseEntity<List<Viaje>> getViajesReingresoDisponible(
                        @Parameter(description = "ID del usuario", required = true) @PathVariable @NonNull Long usuarioId) {
                return ResponseEntity.ok(service.findViajesReingreso(usuarioId));
        }

        @Operation(summary = "Expirar reingreso (tiempo agotado)", description = "Actualiza el estado a 'cancela' si ha pasado el tiempo límite de reingreso (15 min).")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reingreso expirado exitosamente (estado cambiado a cancela)"),
                        @ApiResponse(responseCode = "304", description = "No expiró (aún dentro de tiempo o estado incorrecto)"),
                        @ApiResponse(responseCode = "404", description = "Viaje o participante no encontrado")
        })
        @PutMapping("/{viajeId}/participante/{usuarioId}/expirar-reingreso")
        public ResponseEntity<Void> expirarReingreso(
                        @Parameter(description = "ID del viaje", required = true) @PathVariable @NonNull Long viajeId,
                        @Parameter(description = "ID del participante", required = true) @PathVariable @NonNull Long usuarioId) {
                boolean expirado = service.expirarReingreso(viajeId, usuarioId);
                // Si expiró y cambió estado -> 200 OK
                if (expirado) {
                        return ResponseEntity.ok().build();
                }
                // Si no expiró (porque no pasó el tiempo o no estaba en estado correcto) -> 304
                // Not Modified o 200 OK sin cambios?
                // Usaremos 200 OK para simplificar manejo en cliente, pero indicando que no
                // hubo cambio real (o quizás 204 No Content)
                return ResponseEntity.ok().build();
        }
}
