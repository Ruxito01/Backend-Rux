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
    boolean updateEstadoParticipante(Long viajeId, Long usuarioId, String nuevoEstado);
}
