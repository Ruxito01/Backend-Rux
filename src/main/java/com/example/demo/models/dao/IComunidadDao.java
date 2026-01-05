package com.example.demo.models.dao;

import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IComunidadDao extends JpaRepository<Comunidad, Long> {

    // Query personalizada para cargar comunidad con sus miembros (EAGER)
    @Query("SELECT c FROM Comunidad c LEFT JOIN FETCH c.miembros WHERE c.id = :id")
    Optional<Comunidad> findByIdWithMiembros(@Param("id") Long id);

    // Buscar comunidades creadas por un usuario
    List<Comunidad> findByCreador(Usuario creador);

    // Obtener comunidades a las que pertenece un usuario (miembro O creador)
    @Query("SELECT DISTINCT c FROM Comunidad c LEFT JOIN c.miembros m WHERE m.id = :usuarioId OR c.creador.id = :usuarioId")
    List<Comunidad> findByMiembroId(@Param("usuarioId") Long usuarioId);
}
