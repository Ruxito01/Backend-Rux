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
    private com.example.demo.models.dao.IViajeDao viajeDao; // Para verificar VIAJES y DISTANCIA

    @Autowired
    private com.example.demo.models.dao.IRutaDao rutaDao; // Para verificar RUTAS_CREADAS

    @Autowired
    private com.example.demo.models.dao.ILogroUsuarioDao logroUsuarioDao;

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
        // Usamos el DAO de la tabla intermedia para mayor eficiencia
        Set<Long> logrosDesbloqueadosIds = logroUsuarioDao.findByUsuarioId(usuario.getId())
                .stream()
                .map(lu -> lu.getLogro().getId())
                .collect(Collectors.toSet());

        for (Logro logro : todosLosLogros) {
            // Si ya lo tiene, saltar
            if (logrosDesbloqueadosIds.contains(logro.getId())) {
                continue;
            }

            // Verificar si cumple el criterio
            if (cumpleCriterio(usuario, logro.getCriterioDesbloqueo())) {
                // Asignar logro mediante la entidad intermedia
                com.example.demo.models.entity.LogroUsuario nuevoLogroUsuario = new com.example.demo.models.entity.LogroUsuario();
                nuevoLogroUsuario.setUsuario(usuario);
                nuevoLogroUsuario.setLogro(logro);
                nuevoLogroUsuario.setFechaObtencion(java.time.LocalDateTime.now());
                nuevoLogroUsuario.setCelebrado(false); // Por defecto no celebrado

                logroUsuarioDao.save(nuevoLogroUsuario);

                System.out.println("LOGRO DESBLOQUEADO: " + logro.getNombre() + " para usuario " + usuario.getEmail());
            }
        }

        // No necesitamos usuarioService.save(usuario) porque guardamos directamente en
        // logroUsuarioDao
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
                    // Verificar cantidad de viajes completados (estado='finalizado') de forma
                    // estricta
                    long viajes = viajeDao.countViajesFinalizadosByUser(usuario.getId());
                    return viajes >= valorRequerido;

                case "DISTANCIA":
                    // Verificar distancia acumulada (estado='finalizado') de forma estricta
                    java.math.BigDecimal distancia = viajeDao.sumDistanciaViajesFinalizadosByUser(usuario.getId());
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

    /**
     * Verifica un logro específico para TODOS los usuarios.
     * Útil cuando se crea un nuevo logro y se quiere aplicar retroactivamente.
     */
    @Transactional
    public void verificarLogroRetroactivo(Logro nuevoLogro) {
        if (nuevoLogro == null || nuevoLogro.getCriterioDesbloqueo() == null) {
            System.err.println("⚠️ LogroRetroactivo: Logro nulo o criterio nulo");
            return;
        }

        System.out.println("🔍 Iniciando verificacion retroactiva para: " + nuevoLogro.getNombre() + " ("
                + nuevoLogro.getCriterioDesbloqueo() + ")");

        List<Usuario> usuarios = usuarioService.findAll();
        int asignados = 0;
        int verificados = 0;

        for (Usuario usuario : usuarios) {
            verificados++;
            try {
                // Verificar si ya tiene el logro usando el DAO
                boolean yaLoTiene = logroUsuarioDao.existsByUsuarioIdAndLogroId(usuario.getId(), nuevoLogro.getId());

                if (yaLoTiene) {
                    continue;
                }

                if (cumpleCriterio(usuario, nuevoLogro.getCriterioDesbloqueo())) {
                    com.example.demo.models.entity.LogroUsuario nuevoLogroUsuario = new com.example.demo.models.entity.LogroUsuario();
                    nuevoLogroUsuario.setUsuario(usuario);
                    nuevoLogroUsuario.setLogro(nuevoLogro);
                    nuevoLogroUsuario.setFechaObtencion(java.time.LocalDateTime.now());
                    nuevoLogroUsuario.setCelebrado(false);

                    logroUsuarioDao.save(nuevoLogroUsuario);

                    asignados++;
                    System.out.println("   -> Asignado a usuario ID: " + usuario.getId());
                }
            } catch (Exception e) {
                System.err.println("⚠️ Error verificando usuario " + usuario.getId() + ": " + e.getMessage());
            }
        }

        System.out.println("✅ Logro retroactivo verificado: " + nuevoLogro.getNombre() + ". Verificados: " + verificados
                + ". Asignados: " + asignados);
    }
}
