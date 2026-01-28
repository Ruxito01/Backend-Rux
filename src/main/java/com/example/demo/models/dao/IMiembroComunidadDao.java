package com.example.demo.models.dao;

import com.example.demo.models.entity.MiembroComunidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMiembroComunidadDao extends JpaRepository<MiembroComunidad, Long> {

    /**
     * Busca todos los miembros de una comunidad con su fecha de unión
     */
    @Query("SELECT m FROM MiembroComunidad m WHERE m.comunidad.id = :comunidadId")
    List<MiembroComunidad> findByComunidadId(@Param("comunidadId") Long comunidadId);

    /**
     * Busca si existe la relación entre usuario y comunidad
     */
    @Query("SELECT m FROM MiembroComunidad m WHERE m.usuario.id = :usuarioId AND m.comunidad.id = :comunidadId")
    MiembroComunidad findByUsuarioAndComunidad(
            @Param("usuarioId") Long usuarioId,
            @Param("comunidadId") Long comunidadId);

    /**
     * Busca todas las membresías de un usuario
     */
    @Query("SELECT m FROM MiembroComunidad m WHERE m.usuario.id = :usuarioId")
    List<MiembroComunidad> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Cuenta los miembros activos de una comunidad
     */
    @Query("SELECT COUNT(m) FROM MiembroComunidad m WHERE m.comunidad.id = :comunidadId AND (m.estado = 'activo' OR m.estado IS NULL)")
    Long countMiembros(@Param("comunidadId") Long comunidadId);

    @Query("SELECT COUNT(m) FROM MiembroComunidad m WHERE m.usuario.id = :usuarioId AND (m.estado = 'activo' OR m.estado IS NULL)")
    long countComunidadesByUsuarioId(@Param("usuarioId") Long usuarioId);
}
