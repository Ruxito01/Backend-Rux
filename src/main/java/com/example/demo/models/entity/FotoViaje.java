package com.example.demo.models.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Fotos tomadas durante un viaje.
 * Los usuarios pueden compartir fotos con geolocalización durante sus
 * recorridos.
 */
@Entity
@Table(name = "fotos_viaje")
@Data
public class FotoViaje implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "viaje_id", nullable = false)
    private Viaje viaje;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * URL de la foto almacenada (Supabase Storage u otro servicio)
     */
    @Column(name = "url_foto", nullable = false)
    private String urlFoto;

    /**
     * Latitud donde se tomó la foto
     */
    @Column(precision = 10, scale = 7)
    private BigDecimal latitud;

    /**
     * Longitud donde se tomó la foto
     */
    @Column(precision = 10, scale = 7)
    private BigDecimal longitud;

    /**
     * Fecha y hora en que se tomó la foto
     */
    @Column(name = "fecha_toma")
    private LocalDateTime fechaToma = LocalDateTime.now();

    private static final long serialVersionUID = 1L;
}
