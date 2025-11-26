package com.example.demo.models.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ruta")
public class Ruta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comunidad_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Comunidad comunidad;

    private String nombre;

    private String descripcion;

    @Column(name = "distancia_km")
    private Double distanciaKm;

    @Column(name = "tiempo_estimado_minutos")
    private Integer tiempoEstimadoMinutos;

    @Column(name = "dificultad_id")
    private Integer dificultadId;

    @Column(name = "es_publica")
    private Boolean esPublica;

    @Column(name = "geometria_ruta", columnDefinition = "TEXT")
    private String geometriaRuta;

    @Column(name = "lat_inicio")
    private Double latInicio;

    @Column(name = "lng_inicio")
    private Double lngInicio;

    @Column(name = "lat_fin")
    private Double latFin;

    @Column(name = "lng_fin")
    private Double lngFin;
}
