package com.example.demo.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad que representa las fotos del carrusel de un usuario.
 * Relación: Muchas fotos pertenecen a un usuario.
 */
@Entity
@Table(name = "fotos_usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FotoUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_foto", nullable = false)
    private String urlFoto; // Aquí va el link que devuelve Supabase

    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida = LocalDateTime.now();

    // Relación: Muchas fotos pertenecen a Un usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({ "fotosCarrusel", "logros", "avatares", "comunidades", "viajes", "contrasena" })
    private Usuario usuario;
}
