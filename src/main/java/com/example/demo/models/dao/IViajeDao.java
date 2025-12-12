package com.example.demo.models.dao;

import com.example.demo.models.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IViajeDao extends JpaRepository<Viaje, Long> {

    /**
     * Verifica si existe un viaje con el código de invitación dado.
     * 
     * @param codigoInvitacion Código a verificar
     * @return true si existe, false si no
     */
    boolean existsByCodigoInvitacion(String codigoInvitacion);
}
