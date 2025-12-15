package com.example.demo.models.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Modelo de vehículo asociado a una marca.
 * Ej: Para Hyundai: i10, i20, Creta, Tucson
 * Para Yamaha: WR450F, YZ250, Tenere 700
 */
@Entity
@Table(name = "modelos")
@Data
public class Modelo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del modelo.
     * Ej: "i10", "Corolla", "Wrangler"
     */
    @Column(nullable = false)
    private String nombre;

    /**
     * Marca a la que pertenece este modelo
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "marca_id", nullable = false)
    @JsonBackReference
    private Marca marca;

    /**
     * Campo transient para obtener el ID de la marca en respuestas JSON
     */
    @Transient
    public Long getMarcaId() {
        return marca != null ? marca.getId() : null;
    }

    /**
     * Campo transient para obtener el nombre de la marca en respuestas JSON
     */
    @Transient
    public String getMarcaNombre() {
        return marca != null ? marca.getNombre() : null;
    }
}
