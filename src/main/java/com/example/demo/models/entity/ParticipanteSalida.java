package com.example.demo.models.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participante_salida")
public class ParticipanteSalida implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id; // Added ID as it's required for JPA entity, though not explicitly in schema
                     // diagram for this table

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salida_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private SalidaGrupal salida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Usuario usuario;
}
