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
     * Lista de animaciones disponibles en el modelo.
     * Mapeado como OneToMany para tener ID propio en la tabla.
     */
    @OneToMany(mappedBy = "avatar", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @com.fasterxml.jackson.annotation.JsonIgnore // Ignoramos la lista de entidades en el JSON directo
    private Set<AvatarAnimacion> animacionesList = new HashSet<>();

    /**
     * Helper para mantener compatibilidad JSON y devolver solo los nombres.
     * El frontend/mobile espera un array de strings ["run", "idle"]
     */
    @com.fasterxml.jackson.annotation.JsonProperty("animaciones")
    public Set<String> getAnimaciones() {
        if (animacionesList == null)
            return new HashSet<>();
        Set<String> nombres = new HashSet<>();
        for (AvatarAnimacion anim : animacionesList) {
            nombres.add(anim.getNombre());
        }
        return nombres;
    }

    /**
     * Helper para setear animaciones desde strings (útil para carga inicial o
     * admin).
     */
    public void setAnimaciones(Set<String> nombres) {
        if (this.animacionesList == null) {
            this.animacionesList = new HashSet<>();
        }
        this.animacionesList.clear();
        if (nombres != null) {
            for (String nombre : nombres) {
                this.animacionesList.add(new AvatarAnimacion(nombre, this));
            }
        }
    }

    // Relación muchos a muchos con Usuario
    // Se usa @JsonIgnore para evitar recursión infinita con avatarActivo
    @ManyToMany(mappedBy = "avatares", fetch = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<Usuario> usuarios = new HashSet<>();

    private static final long serialVersionUID = 1L;
}
