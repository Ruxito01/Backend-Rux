package com.example.demo.models.service;

import com.example.demo.models.entity.AlertaViaje;
import java.util.List;

public interface IAlertaViajeService {
    List<AlertaViaje> findAll();
    AlertaViaje findById(Long id);
    AlertaViaje save(AlertaViaje entity);
    void deleteById(Long id);
}
