package com.example.demo.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Comunidad de usuarios off-road.
 * Grupos donde los usuarios comparten rutas y organizan viajes grupales.
 */
@Entity
@Table(name = "comunidades")
@Data
public class Comunidad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creador_id", nullable = false)
    @JsonIgnoreProperties({ "comunidades", "password", "fechaRegistro" })
    private Usuario creador;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Nivel de privacidad de la comunidad.
     * Valores: 'publica', 'privada'
     */
    @Column(name = "nivel_privacidad")
    private String nivelPrivacidad;

    /**
     * URL de la imagen/logo de la comunidad
     */
    @Column(name = "url_imagen")
    private String urlImagen;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Relación muchos a muchos con Usuario (miembros de la comunidad)
    @ManyToMany(mappedBy = "comunidades", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("comunidades")
    private Set<Usuario> miembros = new HashSet<>();

    private static final long serialVersionUID = 1L;
}
