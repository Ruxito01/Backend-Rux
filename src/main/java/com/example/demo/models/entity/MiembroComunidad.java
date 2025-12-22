package com.example.demo.models.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad intermedia para la relación ManyToMany entre Usuario y Comunidad
 * Almacena la fecha en que el usuario se unió a la comunidad
 */
@Entity
@Table(name = "miembro_comunidad")
@Data
public class MiembroComunidad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "comunidad_id", nullable = false)
    private Comunidad comunidad;

    @Column(name = "fecha_union", nullable = false)
    private LocalDateTime fechaUnion = LocalDateTime.now();

    @Column(name = "estado", nullable = false)
    private String estado = "activo"; // "activo" o "inactivo"

    private static final long serialVersionUID = 1L;
}
