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

    /**
     * Busca todos los viajes donde un usuario específico es participante.
     * Utiliza la convención de nombres de JPA para relaciones ManyToMany
     * (participantes.id).
     * 
     * @param usuarioId ID del usuario participante
     * @return Lista de viajes encontrados
     */
    java.util.List<Viaje> findByParticipantes_Id(Long usuarioId);

    /**
     * Busca todos los viajes asociados a una ruta específica.
     * 
     * @param rutaId ID de la ruta
     * @return Lista de viajes encontrados
     */
    java.util.List<Viaje> findByRutaId(Long rutaId);
}
