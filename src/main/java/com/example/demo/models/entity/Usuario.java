package com.example.demo.models.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "foto")
    private String foto;

    
    @Column(name = "contrasena")
    private String contrasena;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Relación muchos a muchos con Logros
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "logro_usuario", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "logro_id"))
    @JsonIgnoreProperties("usuarios")
    private Set<Logro> logros = new HashSet<>();

    // Relación muchos a muchos con Avatares
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "usuario_avatar", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "avatar_id"))
    @JsonIgnoreProperties("usuarios")
    private Set<CatalogoAvatar> avatares = new HashSet<>();

    // Relación muchos a muchos con Comunidades (como miembro)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "miembros_comunidad", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "comunidad_id"))
    @JsonIgnoreProperties("miembros")
    private Set<Comunidad> comunidades = new HashSet<>();

    // Relación muchos a muchos con Viajes (como participante)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "participantes_viaje", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "viaje_id"))
    @JsonIgnoreProperties("participantes")
    private Set<Viaje> viajes = new HashSet<>();

}
