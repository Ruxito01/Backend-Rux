package com.example.demo.models.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad para tracking GPS en tiempo real
 * NOTA: Esta entidad NO se persiste en DB (para performance)
 * Solo se usa para enviar datos vía WebSocket
 * 
 * @Transient en todos los campos para evitar que JPA intente persistir
 */
@Entity
@Table(name = "ubicacion_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "viajeId")
public class UbicacionUsuario {

    @Transient // NO persistir en DB
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Transient // NO persistir en DB
    @Column(name = "viaje_id")
    private Long viajeId;

    @Transient // NO persistir en DB
    @Column(name = "latitud")
    private Double latitud;

    @Transient // NO persistir en DB
    @Column(name = "longitud")
    private Double longitud;

    @Transient // NO persistir en DB
    @Column(name = "velocidad")
    private Double velocidad;

    @Transient // NO persistir en DB
    @Column(name = "rumbo")
    private Double rumbo; // Dirección en grados (0-360)
}
