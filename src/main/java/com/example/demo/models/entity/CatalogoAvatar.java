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
     * URL del modelo 3D (.glb) almacenado en Supabase Storage.
     * Bucket: "avatares"
     */
    @Column(name = "url_modelo_3d")
    private String urlModelo3d;

    /**
     * URL de imagen preview del avatar (PNG/JPG) para mostrar en listas.
     * Opcional - permite cargar una miniatura sin cargar el modelo 3D completo.
     */
    @Column(name = "url_preview")
    private String urlPreview;

    /**
     * Indica si el avatar es premium (requiere compra o logro especial)
     */
    @Column(name = "es_premium")
    private Boolean esPremium = false;

    /**
     * Lista de nombres de animaciones disponibles en el modelo.
     * Ejemplo: ["Run", "Idle", "Dance"]
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "catalogo_avatar_animaciones", joinColumns = @JoinColumn(name = "catalogo_avatar_id"))
    @Column(name = "animacion")
    private Set<String> animaciones = new HashSet<>();

    // Relación muchos a muchos con Usuario
    // Se usa @JsonIgnore para evitar recursión infinita con avatarActivo
    @ManyToMany(mappedBy = "avatares", fetch = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<Usuario> usuarios = new HashSet<>();

    private static final long serialVersionUID = 1L;
}
