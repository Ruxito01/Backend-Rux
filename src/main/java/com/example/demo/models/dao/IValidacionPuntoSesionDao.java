package com.example.demo.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.entity.ValidacionPuntoSesion;

public interface IValidacionPuntoSesionDao extends CrudRepository<ValidacionPuntoSesion, Long> {
}
