package com.example.demo.models.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "tipos_vehiculo")
@Data
public class TipoVehiculo implements Serializable {

	private static final long serialVersionUID = 1L;
	 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "foto")
    private byte foto;

   
}
