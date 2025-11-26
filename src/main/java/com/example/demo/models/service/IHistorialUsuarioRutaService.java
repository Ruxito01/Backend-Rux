package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.HistorialUsuarioRuta;

public interface IHistorialUsuarioRutaService {
    public List<HistorialUsuarioRuta> findAll();

    public HistorialUsuarioRuta findById(Long id);

    public HistorialUsuarioRuta save(HistorialUsuarioRuta historialUsuarioRuta);

    public void delete(Long id);
}
