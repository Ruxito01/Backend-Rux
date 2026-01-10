package com.example.demo.models.dao;

import com.example.demo.models.entity.PrivacidadPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * DAO para la entidad PrivacidadPerfil.
 * Proporciona operaciones CRUD y busqueda por usuario.
 */
public interface IPrivacidadPerfilDao extends JpaRepository<PrivacidadPerfil, Long> {

    /**
     * Busca la configuracion de privacidad por ID de usuario.
     * 
     * @param usuarioId ID del usuario
     * @return Optional con la configuracion si existe
     */
    Optional<PrivacidadPerfil> findByUsuarioId(Long usuarioId);

    /**
     * Verifica si existe configuracion para un usuario.
     * 
     * @param usuarioId ID del usuario
     * @return true si existe configuracion
     */
    boolean existsByUsuarioId(Long usuarioId);
}
