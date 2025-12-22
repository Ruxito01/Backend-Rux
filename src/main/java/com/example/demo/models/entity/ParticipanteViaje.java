package com.example.demo.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "participantes_viaje")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipanteViaje implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ParticipanteViajeId id = new ParticipanteViajeId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties("viajesParticipados")
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("viajeId")
    @JoinColumn(name = "viaje_id")
    @JsonIgnoreProperties("participantes")
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private Viaje viaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoParticipante estado;

    // Km recorridos por el participante durante el viaje
    @Column(name = "km_recorridos", precision = 10, scale = 2)
    private java.math.BigDecimal kmRecorridos;

    // Constructor helper
    public ParticipanteViaje(Usuario usuario, Viaje viaje, EstadoParticipante estado) {
        this.usuario = usuario;
        this.viaje = viaje;
        this.estado = estado;
        this.id = new ParticipanteViajeId(usuario.getId(), viaje.getId());
    }
}
