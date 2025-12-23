package com.example.demo.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Modelo de vehículo asociado a una marca y un tipo de vehículo.
 * Ej: Para Hyundai: i10, i20, Creta, Tucson
 * Para Yamaha: WR450F, YZ250, Tenere 700
 * 
 * Relación: Marca → Modelo → TipoVehiculo
 */
@Entity
@Table(name = "modelos")
@Getter
@Setter
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
     * Tipo de vehículo al que pertenece este modelo.
     * Ej: SUV, Moto Enduro, Jeep, etc.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_vehiculo_id", nullable = false)
    private TipoVehiculo tipoVehiculo;

    /**
     * Expone el ID de la marca en JSON
     */
    @JsonProperty("marcaId")
    public Long getMarcaId() {
        return marca != null ? marca.getId() : null;
    }

    /**
     * Expone el nombre de la marca en JSON
     */
    @JsonProperty("marcaNombre")
    public String getMarcaNombre() {
        return marca != null ? marca.getNombre() : null;
    }

    /**
     * Expone el ID del tipo de vehículo en JSON
     */
    @JsonProperty("tipoVehiculoId")
    public Long getTipoVehiculoId() {
        return tipoVehiculo != null ? tipoVehiculo.getId() : null;
    }

    /**
     * Expone el nombre del tipo de vehículo en JSON
     */
    @JsonProperty("tipoVehiculoNombre")
    public String getTipoVehiculoNombre() {
        return tipoVehiculo != null ? tipoVehiculo.getNombre() : null;
    }
}
