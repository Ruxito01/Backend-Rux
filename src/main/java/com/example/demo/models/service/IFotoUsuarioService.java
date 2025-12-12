package com.example.demo.models.service;

import com.example.demo.models.entity.FotoUsuario;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para la entidad FotoUsuario.
 * Define las operaciones CRUD disponibles.
 */
public interface IFotoUsuarioService {

    /**
     * Obtiene todas las fotos de usuarios
     * 
     * @return Lista de todas las fotos
     */
    List<FotoUsuario> findAll();

    /**
     * Busca una foto por su ID
     * 
     * @param id ID de la foto
     * @return Optional con la foto si existe
     */
    Optional<FotoUsuario> findById(Long id);

    /**
     * Busca todas las fotos de un usuario específico
     * 
     * @param usuarioId ID del usuario
     * @return Lista de fotos del usuario
     */
    List<FotoUsuario> findByUsuarioId(Long usuarioId);

    /**
     * Guarda una nueva foto o actualiza una existente
     * 
     * @param fotoUsuario Foto a guardar
     * @return Foto guardada
     */
    FotoUsuario save(FotoUsuario fotoUsuario);

    /**
     * Elimina una foto por su ID
     * 
     * @param id ID de la foto a eliminar
     */
    void deleteById(Long id);
}
