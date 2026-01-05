package com.example.demo.models.dao;

import com.example.demo.models.entity.CatalogoAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ICatalogoAvatarDao extends JpaRepository<CatalogoAvatar, Long> {

    // Obtener avatares de un usuario directamente desde la tabla de unión
    @Query("SELECT a FROM CatalogoAvatar a JOIN a.usuarios u WHERE u.id = :usuarioId")
    List<CatalogoAvatar> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
