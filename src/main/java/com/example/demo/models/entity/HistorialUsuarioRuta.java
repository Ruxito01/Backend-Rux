package com.example.demo.models.entity;

import java.io.Serializable;
import java.util.Date;

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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "historial_usuario_ruta")
public class HistorialUsuarioRuta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruta_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Ruta ruta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_ruta_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private SesionRuta sesionRuta;

    @Column(name = "fecha_completado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCompletado;

    @Column(name = "tiempo_total_segundos")
    private Long tiempoTotalSegundos;

    @Column(name = "distancia_recorrida")
    private Double distanciaRecorrida;

    @Column(name = "velocidad_media")
    private Double velocidadMedia;
}
