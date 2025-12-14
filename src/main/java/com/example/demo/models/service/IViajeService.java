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
    List<Viaje> findByParticipanteId(Long usuarioId);
}
