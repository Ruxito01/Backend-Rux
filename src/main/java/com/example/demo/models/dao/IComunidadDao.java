package com.example.demo.models.dao;

import com.example.demo.models.entity.Comunidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IComunidadDao extends JpaRepository<Comunidad, Long> {
}
