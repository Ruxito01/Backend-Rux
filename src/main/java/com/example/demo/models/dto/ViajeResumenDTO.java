package com.example.demo.models.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViajeResumenDTO implements Serializable {

    private Long id;
    private String codigoInvitacion;
    private String estado;
    private LocalDateTime fechaProgramada;
    private LocalDateTime fechaCreacion;

    // Datos de Ruta embebidos (para no traer objeto Ruta completo)
    private Long rutaId;
    private String rutaNombre;
    private BigDecimal rutaLatInicio;
    private BigDecimal rutaLngInicio;

    // Datos Organizador
    private Long organizadorId;
    private String organizadorNombre;
    private String organizadorApellido;

    // Estadistica
    private Long cantidadParticipantes;

    // Constructor que coincide con la query JPQL
    public ViajeResumenDTO(Long id, String codigoInvitacion, String estado, LocalDateTime fechaProgramada,
            LocalDateTime fechaCreacion,
            Long rutaId, String rutaNombre, BigDecimal rutaLatInicio, BigDecimal rutaLngInicio,
            Long organizadorId, String organizadorNombre, String organizadorApellido,
            Long cantidadParticipantes) {
        this.id = id;
        this.codigoInvitacion = codigoInvitacion;
        this.estado = estado;
        this.fechaProgramada = fechaProgramada;
        this.fechaCreacion = fechaCreacion;
        this.rutaId = rutaId;
        this.rutaNombre = rutaNombre;
        this.rutaLatInicio = rutaLatInicio;
        this.rutaLngInicio = rutaLngInicio;
        this.organizadorId = organizadorId;
        this.organizadorNombre = organizadorNombre;
        this.organizadorApellido = organizadorApellido;
        this.cantidadParticipantes = cantidadParticipantes;
    }
}
