package com.example.demo.models.dao;

import com.example.demo.models.entity.ParticipanteViaje;
import com.example.demo.models.entity.ParticipanteViajeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IParticipanteViajeDao extends JpaRepository<ParticipanteViaje, ParticipanteViajeId> {

    /**
     * Cuenta participantes en viajes organizados por un usuario, excluyendo al
     * organizador.
     * Usado para el logro INVITAR_AMIGO.
     */
    @Query("SELECT COUNT(pv) FROM ParticipanteViaje pv " +
            "WHERE pv.viaje.organizador.id = :organizadorId " +
            "AND pv.usuario.id != :organizadorId")
    long countParticipantesExternosByOrganizador(@Param("organizadorId") Long organizadorId);

    /**
     * Verifica si existe un participante para un viaje y usuario específicos.
     * 
     * @param viajeId   ID del viaje
     * @param usuarioId ID del usuario
     * @return true si existe, false si no
     */
    boolean existsByViajeIdAndUsuarioId(Long viajeId, Long usuarioId);
}
