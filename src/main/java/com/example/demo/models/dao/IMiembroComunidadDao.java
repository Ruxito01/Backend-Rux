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
}
