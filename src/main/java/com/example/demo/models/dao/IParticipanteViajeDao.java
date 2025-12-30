package com.example.demo.models.dao;

import com.example.demo.models.entity.ParticipanteViaje;
import com.example.demo.models.entity.ParticipanteViajeId;
import com.example.demo.models.entity.EstadoParticipante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface IParticipanteViajeDao extends JpaRepository<ParticipanteViaje, ParticipanteViajeId> {

    /**
     * Verifica si existe un participante para un viaje y usuario específicos.
     * 
     * @param viajeId   ID del viaje
     * @param usuarioId ID del usuario
     * @return true si existe, false si no
     */
    boolean existsByViajeIdAndUsuarioId(Long viajeId, Long usuarioId);

    @Query("SELECT p.usuario.id FROM ParticipanteViaje p WHERE p.estado = :estado")
    List<Long> findUsuarioIdsByEstado(@Param("estado") EstadoParticipante estado);
}
