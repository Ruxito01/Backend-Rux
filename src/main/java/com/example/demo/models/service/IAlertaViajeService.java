package com.example.demo.models.service;

import com.example.demo.models.entity.AlertaViaje;
import java.util.List;
import java.math.BigDecimal;

public interface IAlertaViajeService {
    List<AlertaViaje> findAll();

    AlertaViaje findById(Long id);

    AlertaViaje save(AlertaViaje entity);

    void deleteById(Long id);

    // Método simplificado para crear alertas desde Flutter
    AlertaViaje crearAlerta(Long viajeId, Long usuarioId, String tipoAlerta,
            BigDecimal latitud, BigDecimal longitud, String mensaje);
}
