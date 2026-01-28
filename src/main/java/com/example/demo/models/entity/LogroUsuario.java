package com.example.demo.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "logro_usuario", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "logro_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogroUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties({"logrosObtenidos", "hibernateLazyInitializer", "handler"}) 
    // Evitamos ciclo infinito al serializar usuario -> logroUsuario -> usuario
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER) // Eager porque queremos ver el logro al consultar
    @JoinColumn(name = "logro_id", nullable = false)
    @JsonIgnoreProperties({"usuariosQueLoTienen", "hibernateLazyInitializer", "handler"})
    private Logro logro;

    @Column(name = "fecha_obtencion", nullable = false)
    private LocalDateTime fechaObtencion;

    @Column(name = "celebrado", nullable = false)
    private boolean celebrado = false;

    @PrePersist
    public void prePersist() {
        if (fechaObtencion == null) {
            fechaObtencion = LocalDateTime.now();
        }
    }
}
