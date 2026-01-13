package com.example.demo.models.service;

import com.example.demo.models.entity.Viaje;
import java.util.List;

public interface IViajeService {
    List<Viaje> findAll();

    Viaje findById(Long id);

    Viaje save(Viaje entity);

    void deleteById(Long id);

    // Buscar viaje por código de invitación
    Viaje findByCodigoInvitacion(String codigoInvitacion);

    // Agregar participante a un viaje
    boolean agregarParticipante(Long viajeId, Long usuarioId);

    // Obtener viajes donde el usuario es participante
    // Obtener viajes donde el usuario es participante
    List<Viaje> findByParticipanteId(Long usuarioId);

    // Obtener viajes donde el usuario es participante, filtrando por estado
    List<Viaje> findByParticipanteId(Long usuarioId, String estado);

    /**
     * Obtiene todos los viajes asociados a una ruta específica.
     * 
     * @param rutaId ID de la ruta
     * @return Lista de viajes encontrados
     */
    List<Viaje> findByRutaId(Long rutaId);

    /**
     * Busca el viaje activo de un usuario con validación doble:
     * - El viaje debe estar en un estado específico (ej: "en_curso")
     * - El participante debe tener un estado específico (ej: "ingresa")
     * 
     * @param usuarioId          ID del usuario
     * @param estadoViaje        Estado del viaje
     * @param estadoParticipante Estado del participante
     * @return Viaje que cumple ambas condiciones, o null
     */
    Viaje findActiveViajeByUsuarioAndEstados(Long usuarioId, String estadoViaje, String estadoParticipante);

    // Actualizar estado de un participante en un viaje
    boolean updateEstadoParticipante(Long viajeId, Long usuarioId, String nuevoEstado,
            java.math.BigDecimal kmRecorridos);

    /**
     * Verifica si el usuario tiene conflicto de fechas con un viaje específico.
     * Retorna la lista de viajes existentes que tienen solapamiento de fechas.
     * 
     * @param usuarioId ID del usuario
     * @param viajeId   ID del viaje destino
     * @return Lista de viajes en conflicto (vacía si no hay conflicto)
     */
    List<Viaje> verificarConflictoFechas(Long usuarioId, Long viajeId);

    /**
     * Busca viajes activos del usuario en una fecha específica (mismo día).
     * 
     * @param usuarioId ID del usuario
     * @param fecha     Fecha a buscar
     * @return Lista de viajes del usuario en ese día
     */
    List<Viaje> findViajesByUsuarioAndFecha(Long usuarioId, java.time.LocalDateTime fecha);

    /**
     * Obtiene todos los viajes asociados a una comunidad específica.
     * 
     * @param comunidadId ID de la comunidad
     * @return Lista de viajes de la comunidad
     */
    List<Viaje> findByComunidadId(Long comunidadId);

    /**
     * Busca viajes programados o creados en los últimos X días.
     * 
     * @param dias Cantidad de días hacia atrás
     * @return Lista de viajes recientes
     */
    List<Viaje> findRecientes(int dias);

    /**
     * Obtiene todos los viajes optimizados para dashboard.
     */
    List<Viaje> findAllWithRelations();

    /**
     * Cancela un viaje completo: cambia el estado del viaje a "cancelado"
     * y el estado de todos los participantes a "cancela".
     * 
     * @param viajeId ID del viaje a cancelar
     * @return true si se canceló exitosamente
     */
    boolean cancelarViajeCompleto(Long viajeId);
}
