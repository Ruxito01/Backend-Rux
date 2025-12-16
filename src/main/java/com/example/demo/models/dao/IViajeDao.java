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
     * Utiliza la convención de nombres de JPA para relaciones OneToMany ->
     * ManyToOne
     * (participantes.usuario.id).
     * 
     * @param usuarioId ID del usuario participante
     * @return Lista de viajes encontrados
     */
    java.util.List<Viaje> findByParticipantes_Usuario_Id(Long usuarioId);

    /**
     * Busca todos los viajes asociados a una ruta específica.
     * 
     * @param rutaId ID de la ruta
     * @return Lista de viajes encontrados
     */
    java.util.List<Viaje> findByRutaId(Long rutaId);

    /**
     * Busca viajes donde el usuario es participante y tienen un estado de VIAJE
     * específico.
     * 
     * @param usuarioId ID del usuario
     * @param estado    Estado del viaje (ej: "en_curso")
     * @return Lista de viajes filtrados
     */
    java.util.List<Viaje> findByParticipantes_Usuario_IdAndEstado(Long usuarioId, String estado);

    /**
     * Busca un viaje donde el usuario es participante con estado de VIAJE
     * específico
     * Y el estado del PARTICIPANTE específico.
     * Útil para verificar si un usuario tiene un viaje activo en el que ya ingresó.
     * 
     * @param usuarioId          ID del usuario
     * @param estadoViaje        Estado del viaje (ej: "en_curso")
     * @param estadoParticipante Estado del participante (ej: "ingresa")
     * @return Viaje que cumple ambas condiciones, o null si no existe
     */
    @org.springframework.data.jpa.repository.Query("SELECT v FROM Viaje v JOIN v.participantes p " +
            "WHERE p.usuario.id = :usuarioId " +
            "AND v.estado = :estadoViaje " +
            "AND p.estado = :estadoParticipante")
    Viaje findActiveViajeByUsuarioAndEstados(
            @org.springframework.data.repository.query.Param("usuarioId") Long usuarioId,
            @org.springframework.data.repository.query.Param("estadoViaje") String estadoViaje,
            @org.springframework.data.repository.query.Param("estadoParticipante") String estadoParticipante);
}
