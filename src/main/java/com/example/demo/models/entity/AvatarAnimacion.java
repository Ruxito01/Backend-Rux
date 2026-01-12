package com.example.demo.models.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Entidad que representa una animación individual de un avatar.
 * Reemplaza la antigua ElementCollection para tener un ID propio.
 */
@Entity
@Table(name = "catalogo_avatar_animaciones")
@Data
@NoArgsConstructor
public class AvatarAnimacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalogo_avatar_id")
    private CatalogoAvatar avatar;

    public AvatarAnimacion(String nombre, CatalogoAvatar avatar) {
        this.nombre = nombre;
        this.avatar = avatar;
    }

    private static final long serialVersionUID = 1L;
}
