package com.example.demo.models.dao;

import com.example.demo.models.entity.Logro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ILogroDao extends JpaRepository<Logro, Long> {

    // Obtener logros de un usuario directamente desde la tabla de unión
    @Query("SELECT l FROM Logro l JOIN l.usuarios u WHERE u.id = :usuarioId")
    List<Logro> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
