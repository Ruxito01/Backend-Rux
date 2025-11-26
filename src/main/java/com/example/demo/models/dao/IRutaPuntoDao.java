package com.example.demo.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.entity.RutaPunto;

public interface IRutaPuntoDao extends CrudRepository<RutaPunto, Long> {
}
