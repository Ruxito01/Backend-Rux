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
@Table(name = "dificultad_ruta")
public class DificultadRuta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private Integer nivel;

    private String color;
}
