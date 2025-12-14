package com.example.demo.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements Serializable {

        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "nombre", nullable = false)
        private String nombre;

        @Column(name = "apellido", nullable = false)
        private String apellido;

        @Column(name = "fecha_nacimiento")
        private Date fechaNacimiento;

        @Column(name = "celular")
        private String celular;

        @Column(name = "cedula")
        private String cedula;

        @Column(unique = true, nullable = false)
        private String email;

        @Column(name = "foto")
        private String foto;

        @Column(name = "contrasena")
        private String contrasena;

        @Column(name = "genero")
        private String genero; // Masculino, Femenino

        @Column(name = "rol")
        private String rol = "USER"; // USER o ADMIN

        @Column(name = "fecha_creacion", nullable = false)
        private LocalDateTime fechaCreacion = LocalDateTime.now();

        // Relación muchos a muchos con Logros
        @ManyToMany(fetch = FetchType.LAZY, cascade = {
                        CascadeType.PERSIST,
                        CascadeType.MERGE
        })
        @JoinTable(name = "logro_usuario", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "logro_id"))
        // Se usa @JsonIgnore para evitar LazyInitializationException
        // Los logros se obtienen por endpoint: GET /api/usuario/{id}/logros
        @JsonIgnore
        @ToString.Exclude
        @EqualsAndHashCode.Exclude
        private Set<Logro> logros = new HashSet<>();

        // Relación muchos a muchos con Avatares
        @ManyToMany(fetch = FetchType.LAZY, cascade = {
                        CascadeType.PERSIST,
                        CascadeType.MERGE
        })
        @JoinTable(name = "usuario_avatar", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "avatar_id"))
        // Se usa @JsonIgnore para evitar LazyInitializationException
        // Los avatares se obtienen por endpoint separado
        @JsonIgnore
        @ToString.Exclude
        @EqualsAndHashCode.Exclude
        private Set<CatalogoAvatar> avatares = new HashSet<>();

        // Relación muchos a muchos con Comunidades (como miembro)
        // Se usa @JsonIgnore para evitar LazyInitializationException
        // Las comunidades del usuario se obtienen por endpoint: GET
        // /api/usuario/{id}/comunidades
        @ManyToMany(fetch = FetchType.LAZY, cascade = {
                        CascadeType.PERSIST,
                        CascadeType.MERGE
        })
        @JoinTable(name = "miembros_comunidad", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "comunidad_id"))
        @JsonIgnore
        @ToString.Exclude
        @EqualsAndHashCode.Exclude
        private Set<Comunidad> comunidades = new HashSet<>();

        // Relación muchos a muchos con Viajes (como participante)
        @ManyToMany(fetch = FetchType.LAZY, cascade = {
                        CascadeType.PERSIST,
                        CascadeType.MERGE
        })
        @JoinTable(name = "participantes_viaje", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "viaje_id"))
        // Se usa @JsonIgnore para evitar LazyInitializationException
        // Los viajes se obtienen por endpoint separado
        @JsonIgnore
        @ToString.Exclude
        @EqualsAndHashCode.Exclude
        private Set<Viaje> viajes = new HashSet<>();

        // Relación uno a muchos con Fotos del carrusel
        // Se usa @JsonIgnore para excluir de la serialización JSON y evitar
        // LazyInitializationException
        // Las fotos se obtienen por separado mediante el endpoint
        // /api/foto-usuario/usuario/{id}
        @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JsonIgnore
        @ToString.Exclude
        @EqualsAndHashCode.Exclude
        private Set<FotoUsuario> fotosCarrusel = new HashSet<>();

}
