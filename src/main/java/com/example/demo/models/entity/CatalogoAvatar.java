package com.example.demo.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Catálogo de avatares disponibles para desbloquear.
 * Los usuarios pueden obtener múltiples avatares según su nivel.
 */
@Entity
@Table(name = "catalogo_avatares")
@Data
public class CatalogoAvatar implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del avatar. Ej: "Robot", "Piloto Pro", "Navigator"
     */
    @Column(nullable = false)
    private String nombre;

    /**
     * Descripción del avatar
     */
    private String descripcion;

    /**
     * Ruta del asset en Flutter para el modelo 3D o imagen.
     * Ej: "assets/models/avatars/mech.glb"
     */
    @Column(name = "ruta_asset_flutter")
    private String rutaAssetFlutter;

    /**
     * Indica si el avatar es premium (requiere compra o logro especial)
     */
    @Column(name = "es_premium")
    private Boolean esPremium = false;

    // Relación muchos a muchos con Usuario
    @ManyToMany(mappedBy = "avatares", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("avatares")
    private Set<Usuario> usuarios = new HashSet<>();

    private static final long serialVersionUID = 1L;
}
