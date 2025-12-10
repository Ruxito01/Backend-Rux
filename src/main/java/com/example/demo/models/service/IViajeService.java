package com.example.demo.models.service;

import com.example.demo.models.entity.Viaje;
import java.util.List;

public interface IViajeService {
    List<Viaje> findAll();
    Viaje findById(Long id);
    Viaje save(Viaje entity);
    void deleteById(Long id);
}
