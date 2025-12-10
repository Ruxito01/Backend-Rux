package com.example.demo.models.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Puntos que conforman una ruta.
 * Pueden ser de dos tipos:
 * - 'paso' (Waypoint): El GPS debe pasar por ahí pero no detener.
 * - 'servicio' (Stop): Requiere detenerse (gasolinera, comida, mirador, etc.).
 */
@Entity
@Table(name = "puntos_ruta")
@Data
public class PuntoRuta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ruta_id", nullable = false)
    private Ruta ruta;

    /**
     * Tipo de punto.
     * - 'paso': Solo waypoint para guiar el GPS
     * - 'servicio': Stop point donde se debe/puede detener
     */
    @Column(name = "tipo_punto", nullable = false)
    private String tipoPunto;

    /**
     * Nombre descriptivo del punto.
     * Ej: "Cruce del Río", "Gasolinera Shell", "Mirador del Valle"
     */
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(precision = 10, scale = 7, nullable = false)
    private BigDecimal latitud;

    @Column(precision = 10, scale = 7, nullable = false)
    private BigDecimal longitud;

    /**
     * Orden secuencial del punto en la ruta (1, 2, 3, 4...)
     */
    @Column(name = "orden_secuencia", nullable = false)
    private Integer ordenSecuencia;

    /**
     * Si es un punto de 'servicio', tiempo estimado de estadía en minutos.
     * Ej: 15 min para combustible, 30 min para comida.
     * Si es 'paso', este valor será 0.
     */
    @Column(name = "tiempo_estancia_minutos")
    private Integer tiempoEstanciaMinutos = 0;

    private static final long serialVersionUID = 1L;
}
