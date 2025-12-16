package com.example.demo.models.entity;

import lombok.*;

/**
 * POJO para tracking GPS en tiempo real
 * NO es una entidad JPA - no se persiste en DB
 * Solo se usa para transmitir datos vía WebSocket
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionUsuario {

    // ID del usuario que envía la ubicación
    private Long usuarioId;

    // Nombre del usuario (para mostrar en el mapa)
    private String nombreUsuario;

    // ID del viaje al que pertenece esta ubicación
    private Long viajeId;

    // Coordenadas GPS
    private Double latitud;
    private Double longitud;

    // Datos adicionales
    private Double velocidad; // km/h
    private Double rumbo; // Dirección en grados (0-360)
}
