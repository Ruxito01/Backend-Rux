package com.example.demo.models.dao;

import com.example.demo.models.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IViajeDao extends JpaRepository<Viaje, Long> {

    /**
     * Verifica si existe un viaje con el código de invitación dado.
     * 
     * @param codigoInvitacion Código a verificar
     * @return true si existe, false si no
     */
    boolean existsByCodigoInvitacion(String codigoInvitacion);

    /**
     * Busca un viaje por su código de invitación.
     * 
     * @param codigoInvitacion Código de invitación único
     * @return Optional del viaje encontrado
     */
    Optional<Viaje> findByCodigoInvitacion(String codigoInvitacion);
}
