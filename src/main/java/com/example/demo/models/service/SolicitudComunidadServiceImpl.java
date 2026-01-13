package com.example.demo.models.service;

import com.example.demo.models.dao.ISolicitudComunidadDao;
import com.example.demo.models.dao.IUsuarioDao;
import com.example.demo.models.dao.IMiembroComunidadDao;
import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.entity.SolicitudComunidad;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.entity.MiembroComunidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitudComunidadServiceImpl implements ISolicitudComunidadService {

    @Autowired
    private ISolicitudComunidadDao solicitudDao;

    @Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private IMiembroComunidadDao miembroComunidadDao;

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudComunidad> findAll() {
        return solicitudDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudComunidad findById(Long id) {
        return solicitudDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public SolicitudComunidad save(SolicitudComunidad solicitud) {
        return solicitudDao.save(solicitud);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        solicitudDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudComunidad> findPendientesByComunidadId(Long comunidadId) {
        return solicitudDao.findPendientesByComunidadId(comunidadId);
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudComunidad findPendienteByUsuarioAndComunidad(Long usuarioId, Long comunidadId) {
        return solicitudDao.findPendienteByUsuarioAndComunidad(usuarioId, comunidadId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudComunidad> findByUsuarioId(Long usuarioId) {
        return solicitudDao.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudComunidad> findByComunidadId(Long comunidadId) {
        return solicitudDao.findByComunidadId(comunidadId);
    }

    @Override
    @Transactional
    public SolicitudComunidad aprobarSolicitud(Long solicitudId, Long respondidoPorId) {
        SolicitudComunidad solicitud = findById(solicitudId);
        if (solicitud == null) {
            throw new RuntimeException("Solicitud no encontrada");
        }

        if (!"pendiente".equals(solicitud.getEstado())) {
            throw new RuntimeException("La solicitud ya fue procesada");
        }

        // Actualizar estado de la solicitud
        solicitud.setEstado("aprobada");
        solicitud.setFechaRespuesta(LocalDateTime.now());

        if (respondidoPorId != null) {
            Usuario respondidoPor = usuarioDao.findById(respondidoPorId).orElse(null);
            solicitud.setRespondidoPor(respondidoPor);
        }

        // Agregar usuario como miembro usando MiembroComunidadDao
        // IMPORTANTE: No usar la relación ManyToMany directamente porque la tabla tiene
        // columnas adicionales
        Comunidad comunidad = solicitud.getComunidad();
        Usuario usuario = solicitud.getUsuario();

        // Verificar si ya existe un registro (puede ser inactivo por salida anterior)
        MiembroComunidad existente = miembroComunidadDao.findByUsuarioAndComunidad(usuario.getId(),
                comunidad.getId());
        if (existente == null) {
            // Crear nuevo registro en la tabla miembro_comunidad
            MiembroComunidad nuevoMiembro = new MiembroComunidad();
            nuevoMiembro.setUsuario(usuario);
            nuevoMiembro.setComunidad(comunidad);
            nuevoMiembro.setEstado("activo");
            miembroComunidadDao.save(nuevoMiembro);
        } else {
            // Si existe pero estaba inactivo, reactivar
            existente.setEstado("activo");
            existente.setFechaUnion(LocalDateTime.now()); // Actualizar fecha de reingreso
            miembroComunidadDao.save(existente);
        }

        return solicitudDao.save(solicitud);
    }

    @Override
    @Transactional
    public SolicitudComunidad rechazarSolicitud(Long solicitudId, Long respondidoPorId) {
        SolicitudComunidad solicitud = findById(solicitudId);
        if (solicitud == null) {
            throw new RuntimeException("Solicitud no encontrada");
        }

        if (!"pendiente".equals(solicitud.getEstado())) {
            throw new RuntimeException("La solicitud ya fue procesada");
        }

        // Actualizar estado de la solicitud
        solicitud.setEstado("rechazada");
        solicitud.setFechaRespuesta(LocalDateTime.now());

        if (respondidoPorId != null) {
            Usuario respondidoPor = usuarioDao.findById(respondidoPorId).orElse(null);
            solicitud.setRespondidoPor(respondidoPor);
        }

        return solicitudDao.save(solicitud);
    }
}
