package com.example.demo.models.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad para almacenar códigos de recuperación de contraseña.
 * Los códigos expiran después de 15 minutos.
 */
@Entity
@Table(name = "codigos_recuperacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodigoRecuperacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 6)
    private String codigo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(nullable = false)
    private boolean usado = false;

    /**
     * Verifica si el código ha expirado (más de 15 minutos)
     */
    public boolean haExpirado() {
        return LocalDateTime.now().isAfter(fechaCreacion.plusMinutes(15));
    }
}
