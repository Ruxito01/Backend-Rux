package com.example.demo.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Catálogo de marcas de vehículos.
 * Cada marca puede tener múltiples modelos asociados.
 * Ej: Hyundai, Toyota, Yamaha, Jeep
 */
@Entity
@Table(name = "marcas")
@Getter
@Setter
public class Marca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la marca.
     * Ej: "Hyundai", "Toyota", "Yamaha"
     */
    @Column(nullable = false, unique = true)
    private String nombre;

    /**
     * URL del logo de la marca (opcional)
     */
    @Column(name = "url_logo")
    private String urlLogo;

    /**
     * Lista de modelos asociados a esta marca.
     * Se ignora en JSON para evitar recursión - usar endpoint
     * /api/modelo/por-marca/{id}
     */
    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Modelo> modelos;
}
