package com.example.demo.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Solicitud de un usuario para unirse a una comunidad privada.
 * El creador de la comunidad puede aprobar o rechazar la solicitud.
 */
@Entity
@Table(name = "solicitud_comunidad", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "usuario_id", "comunidad_id" })
})
@Data
public class SolicitudComunidad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties({ "comunidades", "viajes", "logros", "avatares", "contrasena", "fotosCarrusel",
            "hibernateLazyInitializer", "handler" })
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "comunidad_id", nullable = false)
    @JsonIgnoreProperties({ "miembros", "creador", "hibernateLazyInitializer", "handler" })
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Comunidad comunidad;

    /**
     * Estado de la solicitud: 'pendiente', 'aprobada', 'rechazada'
     */
    @Column(nullable = false)
    private String estado = "pendiente";

    /**
     * Mensaje opcional del usuario al solicitar unirse
     */
    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    @Column(name = "fecha_respuesta")
    private LocalDateTime fechaRespuesta;

    @ManyToOne
    @JoinColumn(name = "respondido_por")
    @JsonIgnoreProperties({ "comunidades", "viajes", "logros", "avatares", "contrasena", "fotosCarrusel",
            "hibernateLazyInitializer", "handler" })
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario respondidoPor;

    private static final long serialVersionUID = 1L;
}
