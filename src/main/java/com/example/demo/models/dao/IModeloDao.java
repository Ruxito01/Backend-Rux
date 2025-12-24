package com.example.demo.models.dao;

import com.example.demo.models.entity.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * DAO para la entidad Modelo
 */
public interface IModeloDao extends JpaRepository<Modelo, Long> {

    /**
     * Obtiene todos los modelos de una marca específica
     * Usa marca.id para navegar la relación
     */
    List<Modelo> findByMarca_Id(Long marcaId);

    /**
     * Busca modelos cuyo nombre contenga el texto dado
     */
    List<Modelo> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca un modelo por nombre exacto y marca
     */
    Modelo findByNombreAndMarca_Id(String nombre, Long marcaId);
}
