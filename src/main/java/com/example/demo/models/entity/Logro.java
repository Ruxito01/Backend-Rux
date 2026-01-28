package com.example.demo.models.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Logros del sistema que los usuarios pueden desbloquear.
 * Ej: "Primera Ruta", "100 km Recorridos", "Maestro Off-Road"
 */
@Entity
@Table(name = "logros")
@Data
public class Logro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /**
     * URL del icono del logro
     */
    @Column(name = "url_icono")
    private String urlIcono;

    /**
     * Criterio técnico para desbloquear (para lógica del sistema).
     * Ej: "DISTANCIA_100KM", "RUTAS_COMPLETADAS_10", "PRIMER_VIAJE"
     */
    @Column(name = "criterio_desbloqueo")
    private String criterioDesbloqueo;

    // Relación Uno a Muchos con LogroUsuario
    @OneToMany(mappedBy = "logro", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<LogroUsuario> usuariosQueLoTienen = new HashSet<>();

    private static final long serialVersionUID = 1L;
}
