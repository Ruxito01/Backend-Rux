package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.RutaPunto;

public interface IRutaPuntoService {
    public List<RutaPunto> findAll();

    public RutaPunto findById(Long id);

    public RutaPunto save(RutaPunto rutaPunto);

    public void delete(Long id);
}
