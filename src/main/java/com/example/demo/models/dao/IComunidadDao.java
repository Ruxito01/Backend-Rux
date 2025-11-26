package com.example.demo.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.entity.Comunidad;

public interface IComunidadDao extends CrudRepository<Comunidad, Long> {
}
