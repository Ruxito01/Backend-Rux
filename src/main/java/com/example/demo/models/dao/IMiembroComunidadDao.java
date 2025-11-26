package com.example.demo.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.entity.MiembroComunidad;

public interface IMiembroComunidadDao extends CrudRepository<MiembroComunidad, Long> {
}
