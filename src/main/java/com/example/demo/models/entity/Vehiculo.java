package com.example.demo.models.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;

/**
 * Vehículo del garaje de un usuario.
 * Representa motos, jeeps, quads, etc. que el usuario usa para sus viajes
 * off-road.
 */
@Entity
@Table(name = "vehiculos")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Vehiculo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Modelo del vehículo (incluye marca y tipo de vehículo).
     * Al seleccionar un modelo, se obtiene automáticamente:
     * - La marca (a través de modeloEntidad.getMarca())
     * - El tipo de vehículo (a través de modeloEntidad.getTipoVehiculo())
     */
    @ManyToOne
    @JoinColumn(name = "modelo_id")
    private Modelo modeloEntidad;

    /**
     * Alias/apodo del vehículo.
     * Ej: "La Bestia", "El Rayo", "Mi Enduro"
     */
    private String alias;

    /**
     * Marca del vehículo (legacy/personalizado).
     * Se usa si modeloEntidad es null.
     * Ej: "Yamaha", "Jeep", "Honda"
     */
    private String marca;

    /**
     * Modelo del vehículo (legacy/personalizado).
     * Se usa si modeloEntidad es null.
     * Ej: "WR450F", "Wrangler", "CRF450L"
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

    /**
     * Estado del vehículo.
     * Valores: "en_posesion", "eliminado"
     */
    private String estado;

    @PrePersist
    public void prePersist() {
        if (this.estado == null) {
            this.estado = "en_posesion";
        }
    }

    private static final long serialVersionUID = 1L;
}
