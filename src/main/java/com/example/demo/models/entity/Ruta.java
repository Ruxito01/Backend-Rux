package com.example.demo.models.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Ruta planificada por un usuario.
 * Define el recorrido, puntos de interés y paradas.
 * Las rutas pueden ser públicas o privadas y se usan como base para crear
 * Viajes.
 */
@Entity
@Table(name = "rutas")
@Data
public class Ruta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creador_id", nullable = false)
    private Usuario creador;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Nivel de dificultad de la ruta.
     * Valores: 'facil', 'medio', 'expertos'
     */
    @Column(name = "nivel_dificultad")
    private String nivelDificultad;

    // ==========================================
    // DATOS TÉCNICOS ESTIMADOS
    // ==========================================

    /**
     * Distancia total estimada de la ruta en kilómetros
     */
    @Column(name = "distancia_estimada_km", precision = 10, scale = 2)
    private BigDecimal distanciaEstimadaKm;

    /**
     * Duración estimada total en minutos (incluyendo paradas)
     */
    @Column(name = "duracion_estimada_minutos")
    private Integer duracionEstimadaMinutos;

    /**
     * Latitud del punto de inicio de la ruta
     */
    @Column(name = "latitud_inicio", precision = 10, scale = 7)
    private BigDecimal latitudInicio;

    /**
     * Longitud del punto de inicio de la ruta
     */
    @Column(name = "longitud_inicio", precision = 10, scale = 7)
    private BigDecimal longitudInicio;

    /**
     * Latitud del punto de fin de la ruta
     */
    @Column(name = "latitud_fin", precision = 10, scale = 7)
    private BigDecimal latitudFin;

    /**
     * Longitud del punto de fin de la ruta
     */
    @Column(name = "longitud_fin", precision = 10, scale = 7)
    private BigDecimal longitudFin;

    // ==========================================
    // PRIVACIDAD Y ESTADO
    // ==========================================

    /**
     * Nivel de privacidad de la ruta.
     * Valores: 'publica', 'amigos', 'privada'
     */
    private String privacidad;

    /**
     * Estado de publicación de la ruta.
     * Valores: 'borrador', 'publicada'
     */
    private String estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private static final long serialVersionUID = 1L;
}
