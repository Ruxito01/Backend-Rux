package com.example.demo.models.ServiceImpl;

import com.example.demo.models.dao.IAlertaViajeDao;
import com.example.demo.models.dao.IViajeDao;
import com.example.demo.models.dao.IUsuarioDao;
import com.example.demo.models.entity.AlertaViaje;
import com.example.demo.models.entity.Viaje;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.service.IAlertaViajeService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaViajeServiceImpl implements IAlertaViajeService {

    @Autowired
    private IAlertaViajeDao dao;

    @Autowired
    private IViajeDao viajeDao;

    @Autowired
    private IUsuarioDao usuarioDao;

    @Override
    @Transactional(readOnly = true)
    public List<AlertaViaje> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public AlertaViaje findById(@NonNull Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public AlertaViaje save(@NonNull AlertaViaje entity) {
        return dao.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(@NonNull Long id) {
        dao.deleteById(id);
    }

    @Override
    @Transactional
    public AlertaViaje crearAlerta(Long viajeId, Long usuarioId, String tipoAlerta,
            BigDecimal latitud, BigDecimal longitud, String mensaje) {
        // Buscar entidades
        Viaje viaje = viajeDao.findById(viajeId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado: " + viajeId));
        Usuario usuario = usuarioDao.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

        // Crear alerta
        AlertaViaje alerta = new AlertaViaje();
        alerta.setViaje(viaje);
        alerta.setUsuarioReporta(usuario);
        alerta.setTipoAlerta(tipoAlerta);
        alerta.setLatitud(latitud);
        alerta.setLongitud(longitud);
        alerta.setMensaje(mensaje);
        alerta.setEstaActiva(true);
        alerta.setFechaReporte(LocalDateTime.now());

        return dao.save(alerta);
    }
}
