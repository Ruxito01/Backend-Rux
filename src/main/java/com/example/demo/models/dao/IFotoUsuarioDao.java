package com.example.demo.models.dao;

import com.example.demo.models.entity.FotoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO para la entidad FotoUsuario.
 * Proporciona operaciones CRUD básicas y consultas personalizadas.
 */
@Repository
public interface IFotoUsuarioDao extends JpaRepository<FotoUsuario, Long> {

    /**
     * Busca todas las fotos de un usuario por su ID
     * 
     * @param usuarioId ID del usuario
     * @return Lista de fotos del usuario
     */
    List<FotoUsuario> findByUsuarioId(Long usuarioId);
}
