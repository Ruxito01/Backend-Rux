package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.TipoParada;

public interface ITipoParadaService {
    public List<TipoParada> findAll();

    public TipoParada findById(Long id);

    public TipoParada save(TipoParada tipoParada);

    public void delete(Long id);
}
