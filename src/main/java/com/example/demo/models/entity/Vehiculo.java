package com.example.demo.models.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

/**
 * Vehículo del garaje de un usuario.
 * Representa motos, jeeps, quads, etc. que el usuario usa para sus viajes
 * off-road.
 */
@Entity
@Table(name = "vehiculos")
@Data
public class Vehiculo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "tipo_vehiculo_id", nullable = false)
    private TipoVehiculo tipoVehiculo;

    /**
     * Alias/apodo del vehículo.
     * Ej: "La Bestia", "El Rayo", "Mi Enduro"
     */
    private String alias;

    /**
     * Marca del vehículo. Ej: "Yamaha", "Jeep", "Honda"
     */
    private String marca;

    /**
     * Modelo del vehículo. Ej: "WR450F", "Wrangler", "CRF450L"
     */
    private String modelo;

    /**
     * URL de la foto del vehículo
     */
    @Column(name = "url_foto")
    private String urlFoto;

    /**
     * Tipo de tracción del vehículo.
     * Ej: "4x4", "4x2", "AWD", "2WD"
     */
    private String traccion;

    /**
     * Año de fabricación del vehículo
     */
    @Column(name = "anio_fabricacion")
    private Integer anioFabricacion;

    private static final long serialVersionUID = 1L;
}
