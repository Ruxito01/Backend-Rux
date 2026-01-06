package com.example.demo.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Viaje (ejecución en tiempo real de una Ruta).
 * Un viaje puede ser individual o grupal (vinculado a una Comunidad).
 * Los participantes se conectan vía WebSocket usando el código de invitación.
 */
@Entity
@Table(name = "viajes")
@Data
public class Viaje implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ruta_id", nullable = false)
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private Ruta ruta;

    @ManyToOne
    @JoinColumn(name = "organizador_id", nullable = false)
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private Usuario organizador;

    /**
     * Comunidad asociada al viaje (opcional, para viajes grupales)
     */
    @ManyToOne
    @JoinColumn(name = "comunidad_id")
    @JsonIgnoreProperties({ "miembros", "creador" })
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private Comunidad comunidad;

    /**
     * Código único para unirse a la sala WebSocket del viaje.
     * Los participantes lo usan para conectarse en tiempo real.
     */
    @Column(name = "codigo_invitacion", unique = true, updatable = false)
    private String codigoInvitacion;

    /**
     * Estado del viaje.
     * Valores: 'programado', 'en_curso', 'finalizado'
     */
    private String estado;

    // ==========================================
    // FECHAS Y TIEMPOS
    // ==========================================

    @Column(name = "fecha_programada")
    private LocalDateTime fechaProgramada;

    @Column(name = "fecha_inicio_real")
    private LocalDateTime fechaInicioReal;

    @Column(name = "fecha_fin_real")
    private LocalDateTime fechaFinReal;

    // ==========================================
    // ESTADÍSTICAS GLOBALES DEL GRUPO
    // Se calculan al finalizar el viaje
    // ==========================================

    @Column(name = "distancia_total_real_km", precision = 10, scale = 2)
    private BigDecimal distanciaTotalRealKm;

    @Column(name = "velocidad_promedio_grupo", precision = 5, scale = 2)
    private BigDecimal velocidadPromedioGrupo;

    @Column(name = "tiempo_total_movimiento_minutos")
    private Integer tiempoTotalMovimientoMinutos;

    // Promedio de tiempos individuales de todos los participantes
    @Column(name = "tiempo_promedio_grupo_minutos")
    private Integer tiempoPromedioGrupoMinutos;

    // ==========================================
    // REPLAY DEL VIAJE
    // ==========================================

    /**
     * URL del archivo JSON almacenado en Supabase Storage.
     * Contiene todos los puntos GPS del recorrido para replay.
     * NO se guardan puntos GPS directamente en la BD.
     */
    @Column(name = "url_archivo_replay_json")
    private String urlArchivoReplayJson;

    // Relación uno a muchos con ParticipanteViaje (participantes con estado)
    @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.BatchSize(size = 50)
    @JsonIgnoreProperties("viaje")
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private Set<ParticipanteViaje> participantes = new HashSet<>();

    private static final long serialVersionUID = 1L;
}
