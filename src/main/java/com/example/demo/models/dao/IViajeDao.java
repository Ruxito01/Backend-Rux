package com.example.demo.models.dao;

import com.example.demo.models.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IViajeDao extends JpaRepository<Viaje, Long> {

        /**
         * Busca viajes activos del usuario con fechas que se solapan con el rango dado.
         * Solo considera viajes en estado 'programado' o 'en_curso' y participantes
         * con estado 'registrado' o 'ingresa'.
         * 
         * @param usuarioId   ID del usuario
         * @param fechaInicio Fecha de inicio del rango a verificar
         * @param fechaFin    Fecha de fin del rango a verificar
         * @return Lista de viajes en conflicto
         */
        @org.springframework.data.jpa.repository.Query("SELECT v FROM Viaje v JOIN v.participantes p " +
                        "WHERE p.usuario.id = :usuarioId " +
                        "AND v.estado IN ('programado', 'en_curso') " +
                        "AND p.estado IN (com.example.demo.models.entity.EstadoParticipante.registrado, " +
                        "                 com.example.demo.models.entity.EstadoParticipante.ingresa) " +
                        "AND ((v.fechaProgramada <= :fechaFin) AND (COALESCE(v.fechaFinReal, v.fechaProgramada) >= :fechaInicio))")
        List<Viaje> findViajesConConflictoFechas(
                        @Param("usuarioId") Long usuarioId,
                        @Param("fechaInicio") LocalDateTime fechaInicio,
                        @Param("fechaFin") LocalDateTime fechaFin);

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

        /**
         * Busca viajes activos del usuario en una fecha específica (mismo día).
         * Incluye viajes en estado 'programado' o 'en_curso'.
         * 
         * @param usuarioId      ID del usuario
         * @param fechaInicioDia Inicio del día a buscar
         * @param fechaFinDia    Fin del día a buscar
         * @return Lista de viajes del usuario en ese día
         */
        @org.springframework.data.jpa.repository.Query("SELECT v FROM Viaje v JOIN v.participantes p " +
                        "WHERE p.usuario.id = :usuarioId " +
                        "AND v.estado IN ('programado', 'en_curso') " +
                        "AND p.estado IN (com.example.demo.models.entity.EstadoParticipante.registrado, " +
                        "                 com.example.demo.models.entity.EstadoParticipante.ingresa) " +
                        "AND v.fechaProgramada >= :fechaInicioDia " +
                        "AND v.fechaProgramada < :fechaFinDia")
        List<Viaje> findViajesByUsuarioAndFecha(
                        @Param("usuarioId") Long usuarioId,
                        @Param("fechaInicioDia") LocalDateTime fechaInicioDia,
                        @Param("fechaFinDia") LocalDateTime fechaFinDia);

        /**
         * Obtiene todos los viajes asociados a una comunidad específica.
         * 
         * @param comunidadId ID de la comunidad
         * @return Lista de viajes de la comunidad
         */
        List<Viaje> findByComunidad_Id(Long comunidadId);
}
