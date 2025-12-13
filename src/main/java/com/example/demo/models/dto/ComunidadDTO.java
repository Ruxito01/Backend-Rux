package com.example.demo.models.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ComunidadDTO implements Serializable {
    private Long id;
    private String nombre;
    private String descripcion;
    private String nivelPrivacidad;
    private String urlImagen;
    private LocalDateTime fechaCreacion;
    private Long creadorId;
    private List<Long> miembrosIds;
}
