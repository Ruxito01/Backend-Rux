package com.example.demo.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipanteViajeId implements Serializable {

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "viaje_id")
    private Long viajeId;
}
