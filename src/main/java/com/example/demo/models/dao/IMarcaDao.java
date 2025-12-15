package com.example.demo.models.dao;

import com.example.demo.models.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DAO para la entidad Marca
 */
public interface IMarcaDao extends JpaRepository<Marca, Long> {

    /**
     * Busca una marca por su nombre (case insensitive)
     */
    Marca findByNombreIgnoreCase(String nombre);
}
