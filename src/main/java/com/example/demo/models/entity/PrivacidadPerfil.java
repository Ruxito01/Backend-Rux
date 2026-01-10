package com.example.demo.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

/**
 * Entidad que representa la configuracion de privacidad del perfil de un
 * usuario.
 * Relacion: Un usuario tiene una configuracion de privacidad.
 */
@Entity
@Table(name = "privacidad_perfil")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivacidadPerfil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mostrar correo electronico
    @Column(name = "mostrar_correo", nullable = false)
    private Boolean mostrarCorreo = true;

    // Mostrar estadisticas (km, viajes, tiempo)
    @Column(name = "mostrar_estadisticas", nullable = false)
    private Boolean mostrarEstadisticas = true;

    // Mostrar fotos del carrusel
    @Column(name = "mostrar_fotos", nullable = false)
    private Boolean mostrarFotos = true;

    // Mostrar avatar 3D
    @Column(name = "mostrar_avatar", nullable = false)
    private Boolean mostrarAvatar = true;

    // Mostrar logros obtenidos
    @Column(name = "mostrar_logros", nullable = false)
    private Boolean mostrarLogros = true;

    // Mostrar garage (vehiculos)
    @Column(name = "mostrar_garage", nullable = false)
    private Boolean mostrarGarage = true;

    // Mostrar comunidades (clubs)
    @Column(name = "mostrar_comunidades", nullable = false)
    private Boolean mostrarComunidades = true;

    // Relacion: Una configuracion pertenece a un usuario
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", unique = true)
    @JsonIgnoreProperties({ "fotosCarrusel", "logros", "avatares", "comunidades", "viajes", "contrasena", "vehiculos" })
    private Usuario usuario;
}
