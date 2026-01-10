package com.example.demo.models.service;

import com.example.demo.models.entity.PrivacidadPerfil;
import java.util.Optional;

/**
 * Servicio para gestion de privacidad del perfil.
 */
public interface IPrivacidadPerfilService {

    /**
     * Obtiene la configuracion de privacidad de un usuario.
     * 
     * @param usuarioId ID del usuario
     * @return Optional con la configuracion
     */
    Optional<PrivacidadPerfil> findByUsuarioId(Long usuarioId);

    /**
     * Guarda o actualiza la configuracion de privacidad.
     * 
     * @param privacidad Configuracion a guardar
     * @return Configuracion guardada
     */
    PrivacidadPerfil save(PrivacidadPerfil privacidad);

    /**
     * Verifica si existe configuracion para un usuario.
     * 
     * @param usuarioId ID del usuario
     * @return true si existe
     */
    boolean existsByUsuarioId(Long usuarioId);
}
