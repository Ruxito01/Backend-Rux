package com.example.demo.models.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
@Table(name = "validacion_punto_sesion")
public class ValidacionPuntoSesion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_ruta_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private SesionRuta sesionRuta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "punto_interes_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private PuntoInteres puntoInteres;

    @Temporal(TemporalType.TIMESTAMP)
    private Date hora;

    @PrePersist
    public void prePersist() {
        this.hora = new Date();
    }
}
