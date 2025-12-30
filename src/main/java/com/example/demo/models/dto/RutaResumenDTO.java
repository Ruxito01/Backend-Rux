package com.example.demo.models.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RutaResumenDTO implements Serializable {

    private Long id;
    private String nombre;
    private BigDecimal distanciaEstimadaKm;

    // Coordenadas para mapa
    private BigDecimal latitudInicio;
    private BigDecimal longitudInicio;
    private BigDecimal latitudFin;
    private BigDecimal longitudFin;

    // Creador
    private Long creadorId;
    private String creadorNombre;
    private String creadorApellido;

    public RutaResumenDTO(Long id, String nombre, BigDecimal distanciaEstimadaKm,
            BigDecimal latitudInicio, BigDecimal longitudInicio, BigDecimal latitudFin, BigDecimal longitudFin,
            Long creadorId, String creadorNombre, String creadorApellido) {
        this.id = id;
        this.nombre = nombre;
        this.distanciaEstimadaKm = distanciaEstimadaKm;
        this.latitudInicio = latitudInicio;
        this.longitudInicio = longitudInicio;
        this.latitudFin = latitudFin;
        this.longitudFin = longitudFin;
        this.creadorId = creadorId;
        this.creadorNombre = creadorNombre;
        this.creadorApellido = creadorApellido;
    }
}
