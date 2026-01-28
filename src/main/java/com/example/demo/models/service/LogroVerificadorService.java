package com.example.demo.models.service;

import com.example.demo.models.entity.Logro;
import com.example.demo.models.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LogroVerificadorService {

    @Autowired
    private ILogroService logroService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private com.example.demo.models.dao.IVehiculoDao vehiculoDao;

    @Autowired
    private com.example.demo.models.dao.IMiembroComunidadDao miembroComunidadDao;

    @Autowired
    private com.example.demo.models.dao.IRutaDao rutaDao; // Para verificar RUTAS_CREADAS

    /**
     * Verifica si el usuario cumple con algún logro pendiente y lo asigna.
     * Se debe llamar después de cualquier acción relevante (finalizar viaje,
     * agregar vehiculo, unirse a comunidad).
     */
    @Transactional
    public void verificarLogros(Usuario usuario) {
        if (usuario == null)
            return;

        // 1. Obtener todos los logros existentes
        List<Logro> todosLosLogros = logroService.findAll();

        // 2. Obtener IDs de logros que el usuario YA tiene
        // (Usamos el servicio de usuario o cargamos la colección si es Lazy, aquí
        // asumimos acceso transactional)
        Set<Long> logrosDesbloqueadosIds = usuario.getLogros().stream()
                .map(Logro::getId)
                .collect(Collectors.toSet());

        boolean nuevosLogros = false;

        for (Logro logro : todosLosLogros) {
            // Si ya lo tiene, saltar
            if (logrosDesbloqueadosIds.contains(logro.getId())) {
                continue;
            }

            // Verificar si cumple el criterio
            if (cumpleCriterio(usuario, logro.getCriterioDesbloqueo())) {
                usuario.getLogros().add(logro);
                nuevosLogros = true;
                // Opcional: Notificar al usuario aquí (Push Notification)
                System.out.println("LOGRO DESBLOQUEADO: " + logro.getNombre() + " para usuario " + usuario.getEmail());
            }
        }

        if (nuevosLogros) {
            usuarioService.save(usuario);
        }
    }

    private boolean cumpleCriterio(Usuario usuario, String criterioRaw) {
        if (criterioRaw == null || criterioRaw.isEmpty())
            return false;

        try {
            String[] partes = criterioRaw.split(":");
            if (partes.length < 2)
                return false;

            String tipo = partes[0].toUpperCase();
            double valorRequerido = Double.parseDouble(partes[1]);

            switch (tipo) {
                case "VIAJES":
                    // Verificar cantidad de viajes completados
                    Integer viajes = usuario.getViajesTotalesCompletados();
                    return viajes != null && viajes >= valorRequerido;

                case "DISTANCIA":
                    // Verificar distancia acumulada (en la BD suele estar en KM)
                    java.math.BigDecimal distancia = usuario.getKmTotalesAcumulados();
                    return distancia != null && distancia.doubleValue() >= valorRequerido;

                case "VEHICULOS":
                    // Verificar cantidad de vehículos registrados
                    long cantidadVehiculos = vehiculoDao.countByUsuarioId(usuario.getId());
                    return cantidadVehiculos >= valorRequerido;

                case "COMUNIDADES":
                    // Verificar membresías activas en comunidades
                    long cantidadComunidades = miembroComunidadDao.countComunidadesByUsuarioId(usuario.getId());
                    return cantidadComunidades >= valorRequerido;

                case "RUTAS_CREADAS":
                    // Verificar rutas públicas creadas por el usuario
                    long rutasCreadas = rutaDao.countByUsuarioId(usuario.getId());
                    return rutasCreadas >= valorRequerido;

                default:
                    return false;
            }
        } catch (Exception e) {
            // Si el criterio está mal formado, ignorar
            System.err.println("Error parseando criterio logro: " + criterioRaw + " - " + e.getMessage());
            return false;
        }
    }
}
