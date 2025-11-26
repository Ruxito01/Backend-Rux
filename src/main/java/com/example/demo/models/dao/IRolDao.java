package com.example.demo.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.entity.Rol;

public interface IRolDao extends CrudRepository<Rol, Long> {
}
