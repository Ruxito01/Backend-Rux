package com.example.demo.models.ServiceImpl;

import com.example.demo.models.dao.IUsuarioDao;
import com.example.demo.models.dao.IViajeDao;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.entity.Viaje;
import com.example.demo.models.service.IViajeService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ViajeServiceImpl implements IViajeService {

    @Autowired
    private IViajeDao dao;

    @Autowired
    private IUsuarioDao usuarioDao;

    // Caracteres permitidos para el código (alfanumérico)
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 8; // Longitud solicitada: 8 caracteres
    private final java.security.SecureRandom random = new java.security.SecureRandom();

    @Override
    @Transactional(readOnly = true)
    public List<Viaje> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Viaje findById(@NonNull Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Viaje save(@NonNull Viaje entity) {
        // Generar código de invitación si no existe o está vacío, SOLO para nuevos
        // registros
        boolean esNuevo = entity.getId() == null;

        if (esNuevo
                && (entity.getCodigoInvitacion() == null || entity.getCodigoInvitacion().trim().isEmpty())) {
            String codigoGenerado;
            do {
                codigoGenerado = generateRandomCode();
            } while (dao.existsByCodigoInvitacion(codigoGenerado));

            entity.setCodigoInvitacion(codigoGenerado);
        }

        Viaje viajeGuardado = dao.save(entity);

        // Si es un viaje nuevo, agregar al organizador como participante
        // automáticamente
        if (esNuevo && viajeGuardado.getOrganizador() != null) {
            Usuario organizador = usuarioDao.findById(viajeGuardado.getOrganizador().getId()).orElse(null);

            if (organizador != null) {
                // Crear la relación con estado inicial REGISTRADO
                com.example.demo.models.entity.ParticipanteViaje participante = new com.example.demo.models.entity.ParticipanteViaje(
                        organizador, viajeGuardado, com.example.demo.models.entity.EstadoParticipante.registrado);

                // Agregar a las colecciones (ambos lados para consistencia en memoria y
                // cascada)
                organizador.getViajesParticipados().add(participante);
                viajeGuardado.getParticipantes().add(participante);

                // Guardamos el USUARIO para persistir la relación por cascada
                usuarioDao.save(organizador);
            }
        }

        return viajeGuardado;
    }

    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    @Override
    @Transactional
    public void deleteById(@NonNull Long id) {
        dao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Viaje findByCodigoInvitacion(@NonNull String codigoInvitacion) {
        return dao.findByCodigoInvitacion(codigoInvitacion).orElse(null);
    }

    @Override
    @Transactional
    public boolean agregarParticipante(@NonNull Long viajeId, @NonNull Long usuarioId) {
        // Buscar el viaje
        Viaje viaje = dao.findById(viajeId).orElse(null);
        if (viaje == null) {
            return false;
        }

        // Buscar el usuario
        Usuario usuario = usuarioDao.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return false;
        }

        // Verificar si ya es participante para no duplicar (aunque la primary key
        // compuesta protege)
        boolean yaEsParticipante = usuario.getViajesParticipados().stream()
                .anyMatch(p -> p.getViaje().getId().equals(viajeId));

        if (yaEsParticipante) {
            return true; // Ya estaba agregado
        }

        // Crear la relación con estado inicial REGISTRADO
        com.example.demo.models.entity.ParticipanteViaje participante = new com.example.demo.models.entity.ParticipanteViaje(
                usuario, viaje, com.example.demo.models.entity.EstadoParticipante.registrado);

        // Agregar a las colecciones
        usuario.getViajesParticipados().add(participante);
        viaje.getParticipantes().add(participante);

        // Guardar el usuario para persistir la relación por cascada
        usuarioDao.save(usuario);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Viaje> findByParticipanteId(Long usuarioId) {
        return dao.findByParticipantes_Usuario_Id(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Viaje> findByParticipanteId(Long usuarioId, String estado) {
        if (estado != null && !estado.isEmpty()) {
            return dao.findByParticipantes_Usuario_IdAndEstado(usuarioId, estado);
        }
        return dao.findByParticipantes_Usuario_Id(usuarioId);
    }

    @Override
    @Transactional
    public boolean updateEstadoParticipante(Long viajeId, Long usuarioId, String nuevoEstado) {
        // Validar estado
        com.example.demo.models.entity.EstadoParticipante estadoEnum;
        try {
            estadoEnum = com.example.demo.models.entity.EstadoParticipante.valueOf(nuevoEstado);
        } catch (IllegalArgumentException e) {
            return false;
        }

        // Buscar el usuario con sus viajes participados
        Usuario usuario = usuarioDao.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return false;
        }

        // Buscar la relación específica
        com.example.demo.models.entity.ParticipanteViaje participante = usuario.getViajesParticipados().stream()
                .filter(p -> p.getViaje().getId().equals(viajeId))
                .findFirst()
                .orElse(null);

        if (participante == null) {
            return false;
        }

        // Actualizar estado
        participante.setEstado(estadoEnum);
        usuarioDao.save(usuario); // Cascada guarda la actualización

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Viaje> findByRutaId(Long rutaId) {
        return dao.findByRutaId(rutaId);
    }
}
