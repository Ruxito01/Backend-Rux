package com.example.demo.models.service;

import com.example.demo.models.entity.FotoViaje;
import java.util.List;

public interface IFotoViajeService {
    List<FotoViaje> findAll();

    FotoViaje findById(Long id);

    List<FotoViaje> findByViajeId(Long viajeId);

    FotoViaje save(FotoViaje entity);

    void deleteById(Long id);
}
