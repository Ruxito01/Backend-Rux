package com.example.demo.models.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Alertas e incidencias durante un viaje.
 * Sistema S.O.S para reportar problemas mecánicos, médicos, etc.
 * Al crear una alerta, Supabase Realtime notifica a todos los participants en
 * tiempo real.
 */
@Entity
@Table(name = "alertas_viaje")
@Data
public class AlertaViaje implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "viaje_id", nullable = false)
    private Viaje viaje;

    @ManyToOne
    @JoinColumn(name = "usuario_reporta_id", nullable = false)
    private Usuario usuarioReporta;

    /**
     * Tipo de alerta.
     * Valores: 'mecanica', 'medica', 'combustible', 'policia', 'informativa'
     */
    @Column(name = "tipo_alerta", nullable = false)
    private String tipoAlerta;

    /**
     * Mensaje descriptivo de la alerta.
     * Ej: "Se rompió la dirección", "Necesito gasolina urgente"
     */
    @Column(columnDefinition = "TEXT")
    private String mensaje;

    // ==========================================
    // UBICACIÓN DEL INCIDENTE
    // ==========================================

    /**
     * Latitud donde quedó detenido/ocurrió el incidente
     */
    @Column(precision = 10, scale = 7, nullable = false)
    private BigDecimal latitud;

    /**
     * Longitud donde quedó detenido/ocurrió el incidente
     */
    @Column(precision = 10, scale = 7, nullable = false)
    private BigDecimal longitud;

    // ==========================================
    // ESTADO DE LA ALERTA
    // ==========================================

    /**
     * Indica si la alerta sigue activa o ya fue resuelta
     */
    @Column(name = "esta_activa")
    private Boolean estaActiva = true;

    @Column(name = "fecha_reporte")
    private LocalDateTime fechaReporte = LocalDateTime.now();

    /**
     * Fecha en que se marcó como resuelta
     */
    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    private static final long serialVersionUID = 1L;
}
