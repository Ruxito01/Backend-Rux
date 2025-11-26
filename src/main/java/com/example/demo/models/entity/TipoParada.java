package com.example.demo.models.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tipo_parada")
public class TipoParada implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private String nombre;

    private String icono;
}
