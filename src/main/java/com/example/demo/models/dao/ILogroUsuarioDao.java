package com.example.demo.models.dao;

import com.example.demo.models.entity.LogroUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ILogroUsuarioDao extends JpaRepository<LogroUsuario, Long> {

    /**
     * Verifica si un usuario ya tiene un logro específico.
     */
    @Query("SELECT COUNT(lu) > 0 FROM LogroUsuario lu WHERE lu.usuario.id = :usuarioId AND lu.logro.id = :logroId")
    boolean existsByUsuarioIdAndLogroId(@Param("usuarioId") Long usuarioId, @Param("logroId") Long logroId);

    /**
     * Busca todos los logros obtenidos por un usuario.
     */
    List<LogroUsuario> findByUsuarioId(Long usuarioId);

    /**
     * Busca logros obtenidos pero NO celebrados por el usuario.
     * Útil para que la app móvil sepa qué animaciones mostrar.
     */
    @Query("SELECT lu FROM LogroUsuario lu WHERE lu.usuario.id = :usuarioId AND lu.celebrado = false")
    List<LogroUsuario> findByUsuarioIdAndCelebradoFalse(@Param("usuarioId") Long usuarioId);

    /**
     * Busca un registro específico de logro obtenido.
     */
    @Query("SELECT lu FROM LogroUsuario lu WHERE lu.usuario.id = :usuarioId AND lu.logro.id = :logroId")
    LogroUsuario findByUsuarioIdAndLogroId(@Param("usuarioId") Long usuarioId, @Param("logroId") Long logroId);
}
