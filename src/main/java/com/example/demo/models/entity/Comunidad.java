package com.example.demo.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

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
        @JsonIgnoreProperties({ "comunidades", "viajes", "logros", "avatares", "contrasena", "fotosCarrusel",
                        "hibernateLazyInitializer", "handler" })
        @ToString.Exclude
        @EqualsAndHashCode.Exclude
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

        /**
         * ID del Tipo de vehículo permitido en la comunidad.
         * Si es null, la comunidad es "Libre" (cualquier tipo).
         */
        @Column(name = "tipo_vehiculo_comunidad")
        private Long tipoVehiculoComunidad;

        // Relación muchos a muchos con Usuario (miembros de la comunidad)
        // Se usa @JsonIgnore para excluir de la serialización JSON y evitar
        // LazyInitializationException
        // Los miembros se obtienen por separado mediante un endpoint específico
        @ManyToMany(mappedBy = "comunidades", fetch = FetchType.LAZY)
        @JsonIgnore
        @ToString.Exclude
        @EqualsAndHashCode.Exclude
        private Set<Usuario> miembros = new HashSet<>();

        private static final long serialVersionUID = 1L;
}
