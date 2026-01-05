package com.example.demo.models.service;

import com.example.demo.models.entity.CatalogoAvatar;
import java.util.List;

public interface ICatalogoAvatarService {
    List<CatalogoAvatar> findAll();

    CatalogoAvatar findById(Long id);

    CatalogoAvatar save(CatalogoAvatar entity);

    void deleteById(Long id);

    List<CatalogoAvatar> findByUsuarioId(Long usuarioId); // Obtener avatares de un usuario
}
