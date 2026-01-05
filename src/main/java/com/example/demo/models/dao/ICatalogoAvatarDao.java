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

    // Insertar directamente en la tabla de unión (query nativa)
    @org.springframework.data.jpa.repository.Modifying
    @Query(value = "INSERT INTO usuario_avatar (usuario_id, avatar_id) VALUES (:usuarioId, :avatarId) ON CONFLICT DO NOTHING", nativeQuery = true)
    void insertarEnColeccion(@Param("usuarioId") Long usuarioId, @Param("avatarId") Long avatarId);
}
