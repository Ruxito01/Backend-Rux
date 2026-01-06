package com.example.demo.models.ServiceImpl;

import com.example.demo.models.dao.IUsuarioDao;
import com.example.demo.models.dao.IViajeDao;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.entity.Viaje;
import com.example.demo.models.service.IViajeService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ViajeServiceImpl implements IViajeService {

    @Autowired
    private IViajeDao dao;

    @Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private com.example.demo.models.dao.IParticipanteViajeDao participanteViajeDao;

    // Caracteres permitidos para el código (alfanumérico)
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 8; // Longitud solicitada: 8 caracteres
    private final java.security.SecureRandom random = new java.security.SecureRandom();

    @Override
    @Transactional(readOnly = true)
    public List<Viaje> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Viaje findById(@NonNull Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Viaje save(@NonNull Viaje entity) {
        // Generar código de invitación si no existe o está vacío, SOLO para nuevos
        // registros
        boolean esNuevo = entity.getId() == null;

        if (esNuevo
                && (entity.getCodigoInvitacion() == null || entity.getCodigoInvitacion().trim().isEmpty())) {
            String codigoGenerado;
            do {
                codigoGenerado = generateRandomCode();
            } while (dao.existsByCodigoInvitacion(codigoGenerado));

            entity.setCodigoInvitacion(codigoGenerado);
        }

        // Si el viaje finaliza o se cancela, actualizar estado de participantes que
        // nunca ingresaron
        if (("cancelado".equalsIgnoreCase(entity.getEstado()) || "finalizado".equalsIgnoreCase(entity.getEstado()))
                && entity.getParticipantes() != null) {
            entity.getParticipantes().forEach(p -> {
                // Solo cambiar a 'cancela' los que nunca ingresaron (estado = registrado)
                if (p.getEstado() == com.example.demo.models.entity.EstadoParticipante.registrado) {
                    p.setEstado(com.example.demo.models.entity.EstadoParticipante.cancela);
                }
            });
        }

        Viaje viajeGuardado = dao.save(entity);

        // Si es un viaje nuevo, agregar al organizador como participante
        // automáticamente
        if (esNuevo && viajeGuardado.getOrganizador() != null) {
            Usuario organizador = usuarioDao.findById(viajeGuardado.getOrganizador().getId()).orElse(null);

            if (organizador != null) {
                // Determinar estado inicial basado en si el viaje es en curso
                com.example.demo.models.entity.EstadoParticipante estadoInicial = "en_curso"
                        .equalsIgnoreCase(viajeGuardado.getEstado())
                                ? com.example.demo.models.entity.EstadoParticipante.ingresa
                                : com.example.demo.models.entity.EstadoParticipante.registrado;

                // Crear la relación con el estado determinado
                com.example.demo.models.entity.ParticipanteViaje participante = new com.example.demo.models.entity.ParticipanteViaje(
                        organizador, viajeGuardado, estadoInicial);

                // Agregar a las colecciones (ambos lados para consistencia en memoria y
                // cascada)
                organizador.getViajesParticipados().add(participante);
                viajeGuardado.getParticipantes().add(participante);

                // Guardamos el USUARIO para persistir la relación por cascada
                // usuarioDao.save(organizador); // COMENTADO: Redundante en transacción y causa
                // NonUniqueObjectException
            }
        }

        return viajeGuardado;
    }

    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    @Override
    @Transactional
    public void deleteById(@NonNull Long id) {
        dao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Viaje findByCodigoInvitacion(@NonNull String codigoInvitacion) {
        return dao.findByCodigoInvitacion(codigoInvitacion).orElse(null);
    }

    @Override
    @Transactional
    public boolean agregarParticipante(@NonNull Long viajeId, @NonNull Long usuarioId) {
        // Buscar el viaje
        Viaje viaje = dao.findById(viajeId).orElse(null);
        if (viaje == null) {
            return false;
        }

        // Buscar el usuario
        Usuario usuario = usuarioDao.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return false;
        }

        // Verificar si ya es participante usando el DAO directamente
        // sin acceder a colecciones lazy
        com.example.demo.models.entity.ParticipanteViajeId id = new com.example.demo.models.entity.ParticipanteViajeId(
                usuarioId, viajeId);

        if (participanteViajeDao.existsById(id)) {
            return true; // Ya estaba agregado
        }

        // Crear la relación con estado inicial REGISTRADO
        com.example.demo.models.entity.ParticipanteViaje participante = new com.example.demo.models.entity.ParticipanteViaje(
                usuario, viaje, com.example.demo.models.entity.EstadoParticipante.registrado);

        // Guardar directamente usando el DAO
        participanteViajeDao.save(participante);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Viaje> findByParticipanteId(Long usuarioId) {
        return dao.findByParticipantes_Usuario_Id(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Viaje> findByParticipanteId(Long usuarioId, String estado) {
        if (estado != null && !estado.isEmpty()) {
            return dao.findByParticipantes_Usuario_IdAndEstado(usuarioId, estado);
        }
        return dao.findByParticipantes_Usuario_Id(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public Viaje findActiveViajeByUsuarioAndEstados(Long usuarioId, String estadoViaje, String estadoParticipante) {
        return dao.findActiveViajeByUsuarioAndEstados(usuarioId, estadoViaje, estadoParticipante);
    }

    @Override
    @Transactional
    public boolean updateEstadoParticipante(Long viajeId, Long usuarioId, String nuevoEstado,
            java.math.BigDecimal kmRecorridos) {
        // Validar estado
        com.example.demo.models.entity.EstadoParticipante estadoEnum;
        try {
            estadoEnum = com.example.demo.models.entity.EstadoParticipante.valueOf(nuevoEstado);
        } catch (IllegalArgumentException e) {
            return false;
        }

        // Buscar el usuario con sus viajes participados
        Usuario usuario = usuarioDao.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return false;
        }

        // Buscar la relación específica
        com.example.demo.models.entity.ParticipanteViaje participante = usuario.getViajesParticipados().stream()
                .filter(p -> p.getViaje().getId().equals(viajeId))
                .findFirst()
                .orElse(null);

        if (participante == null) {
            return false;
        }

        // Actualizar estado
        participante.setEstado(estadoEnum);

        // Guardar km recorridos si se proporciona
        if (kmRecorridos != null) {
            participante.setKmRecorridos(kmRecorridos);
        }

        usuarioDao.save(usuario); // Cascada guarda la actualización

        // Obtener el viaje para verificar estados
        Viaje viaje = dao.findById(viajeId).orElse(null);
        if (viaje != null && viaje.getParticipantes() != null) {

            // Si el participante ingresa y es el primero en ingresar,
            // guardar fecha_inicio_real y cambiar estado del viaje a en_curso
            if (estadoEnum == com.example.demo.models.entity.EstadoParticipante.ingresa) {

                // Guardar fecha de inicio INDIVIDUAL del participante
                participante.setFechaInicioIndividual(java.time.LocalDateTime.now());
                usuarioDao.save(usuario);

                boolean esPrimerIngreso = viaje.getFechaInicioReal() == null;

                if (esPrimerIngreso) {
                    viaje.setFechaInicioReal(java.time.LocalDateTime.now());
                    viaje.setEstado("en_curso");
                    dao.save(viaje);
                }
            }

            // Si el participante finaliza, calcular su tiempo individual
            if (estadoEnum == com.example.demo.models.entity.EstadoParticipante.finaliza) {
                participante.setFechaFinIndividual(java.time.LocalDateTime.now());

                // Calcular tiempo individual en minutos
                if (participante.getFechaInicioIndividual() != null) {
                    long minutos = java.time.Duration.between(
                            participante.getFechaInicioIndividual(),
                            participante.getFechaFinIndividual()).toMinutes();
                    participante.setTiempoIndividualMinutos((int) minutos);

                    // Calcular velocidad individual (km/h)
                    if (participante.getKmRecorridos() != null && minutos > 0) {
                        double horas = minutos / 60.0;
                        double velocidad = participante.getKmRecorridos().doubleValue() / horas;
                        participante.setVelocidadIndividual(
                                java.math.BigDecimal.valueOf(velocidad).setScale(2, java.math.RoundingMode.HALF_UP));
                    }

                    // ========== ACUMULAR ESTADÍSTICAS AL PERFIL DEL USUARIO ==========

                    // Sumar km recorridos al total acumulado del usuario
                    if (participante.getKmRecorridos() != null) {
                        java.math.BigDecimal kmActuales = usuario.getKmTotalesAcumulados();
                        if (kmActuales == null)
                            kmActuales = java.math.BigDecimal.ZERO;
                        usuario.setKmTotalesAcumulados(kmActuales.add(participante.getKmRecorridos()));
                    }

                    // Incrementar contador de viajes completados
                    Integer viajesActuales = usuario.getViajesTotalesCompletados();
                    if (viajesActuales == null)
                        viajesActuales = 0;
                    usuario.setViajesTotalesCompletados(viajesActuales + 1);

                    // Sumar tiempo de este viaje al tiempo total del usuario
                    Integer tiempoActual = usuario.getTiempoTotalMovimientoMinutos();
                    if (tiempoActual == null)
                        tiempoActual = 0;
                    usuario.setTiempoTotalMovimientoMinutos(tiempoActual + (int) minutos);
                }
                usuarioDao.save(usuario);
            }

            // Verificar si TODOS los participantes han salido (cancela o finaliza)
            // para finalizar el viaje automáticamente.
            boolean todosFinalizados = viaje.getParticipantes().stream()
                    .allMatch(p -> p.getEstado() == com.example.demo.models.entity.EstadoParticipante.cancela ||
                            p.getEstado() == com.example.demo.models.entity.EstadoParticipante.finaliza);

            if (todosFinalizados) {
                viaje.setEstado("finalizado");
                viaje.setFechaFinReal(java.time.LocalDateTime.now());

                // Calcular tiempo total de movimiento en minutos
                if (viaje.getFechaInicioReal() != null) {
                    long minutos = java.time.Duration.between(
                            viaje.getFechaInicioReal(),
                            viaje.getFechaFinReal()).toMinutes();
                    viaje.setTiempoTotalMovimientoMinutos((int) minutos);
                }

                // Calcular promedio de km recorridos y guardarlo en distancia_total_real_km
                java.math.BigDecimal totalKm = viaje.getParticipantes().stream()
                        .filter(p -> p.getKmRecorridos() != null)
                        .map(com.example.demo.models.entity.ParticipanteViaje::getKmRecorridos)
                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                long count = viaje.getParticipantes().stream()
                        .filter(p -> p.getKmRecorridos() != null)
                        .count();
                if (count > 0) {
                    java.math.BigDecimal promedio = totalKm.divide(
                            java.math.BigDecimal.valueOf(count),
                            2,
                            java.math.RoundingMode.HALF_UP);
                    viaje.setDistanciaTotalRealKm(promedio);

                    // Calcular velocidad promedio (km/h)
                    if (viaje.getTiempoTotalMovimientoMinutos() != null
                            && viaje.getTiempoTotalMovimientoMinutos() > 0) {
                        double horas = viaje.getTiempoTotalMovimientoMinutos() / 60.0;
                        double velocidad = promedio.doubleValue() / horas;
                        viaje.setVelocidadPromedioGrupo(
                                java.math.BigDecimal.valueOf(velocidad).setScale(2, java.math.RoundingMode.HALF_UP));
                    }
                }

                // Calcular TIEMPO PROMEDIO GRUPAL basado en tiempos individuales
                int sumaTiemposIndividuales = viaje.getParticipantes().stream()
                        .filter(p -> p.getTiempoIndividualMinutos() != null)
                        .mapToInt(com.example.demo.models.entity.ParticipanteViaje::getTiempoIndividualMinutos)
                        .sum();
                long countTiempos = viaje.getParticipantes().stream()
                        .filter(p -> p.getTiempoIndividualMinutos() != null)
                        .count();
                if (countTiempos > 0) {
                    int tiempoPromedioGrupo = (int) (sumaTiemposIndividuales / countTiempos);
                    viaje.setTiempoPromedioGrupoMinutos(tiempoPromedioGrupo);
                }

                dao.save(viaje);
            }
        }

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Viaje> findByRutaId(Long rutaId) {
        return dao.findByRutaId(rutaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Viaje> verificarConflictoFechas(Long usuarioId, Long viajeId) {
        // Buscar el viaje destino
        Viaje viajeDestino = dao.findById(viajeId).orElse(null);
        if (viajeDestino == null) {
            return java.util.Collections.emptyList();
        }

        // Determinar fechas del viaje destino
        java.time.LocalDateTime fechaInicio = viajeDestino.getFechaInicioReal() != null
                ? viajeDestino.getFechaInicioReal()
                : viajeDestino.getFechaProgramada();
        java.time.LocalDateTime fechaFin = viajeDestino.getFechaFinReal() != null
                ? viajeDestino.getFechaFinReal()
                : (fechaInicio != null ? fechaInicio : viajeDestino.getFechaProgramada());

        // Sin fecha, no hay conflicto verificable
        if (fechaInicio == null) {
            return java.util.Collections.emptyList();
        }

        // Buscar conflictos excluyendo el viaje al que se quiere unir
        return dao.findViajesConConflictoFechas(usuarioId, fechaInicio, fechaFin)
                .stream()
                .filter(v -> !v.getId().equals(viajeId))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Viaje> findViajesByUsuarioAndFecha(Long usuarioId, java.time.LocalDateTime fecha) {
        // Obtener inicio y fin del día
        java.time.LocalDateTime fechaInicioDia = fecha.toLocalDate().atStartOfDay();
        java.time.LocalDateTime fechaFinDia = fechaInicioDia.plusDays(1);

        return dao.findViajesByUsuarioAndFecha(usuarioId, fechaInicioDia, fechaFinDia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Viaje> findByComunidadId(Long comunidadId) {
        return dao.findByComunidad_Id(comunidadId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Viaje> findRecientes(int dias) {
        java.time.LocalDateTime fechaLimite = java.time.LocalDateTime.now().minusDays(dias);
        return dao.findByFechaProgramadaAfterOrderByFechaProgramadaDesc(fechaLimite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Viaje> findAllWithRelations() {
        List<Viaje> viajes = dao.findAllWithRelations();
        // Deduplicar en memoria usando LinkedHashSet (preserva el orden)
        // Necesario porque quitamos DISTINCT de la query para evitar problemas con
        // campos TEXT
        return new java.util.ArrayList<>(new java.util.LinkedHashSet<>(viajes));
    }
}
