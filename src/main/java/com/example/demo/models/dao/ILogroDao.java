package com.example.demo.models.dao;

import com.example.demo.models.entity.Logro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ILogroDao extends JpaRepository<Logro, Long> {

    // Obtener logros de un usuario directamente desde la tabla de unión
    // Obtener logros de un usuario directamente desde la tabla de unión
    // Obtener logros de un usuario directamente desde la tabla de unión
    @Query("SELECT lu.logro FROM LogroUsuario lu WHERE lu.usuario.id = :usuarioId")
    List<Logro> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    // Obtener todos los logros y la cantidad de usuarios que lo tienen
    @Query("SELECT l, COUNT(lu) FROM Logro l LEFT JOIN l.usuariosQueLoTienen lu GROUP BY l")
    List<Object[]> findAllWithUserCount();
}
