package com.example.demo.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.entity.TipoParada;

public interface ITipoParadaDao extends CrudRepository<TipoParada, Long> {
}
