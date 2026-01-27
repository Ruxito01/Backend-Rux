package com.example.demo.models.service;

import com.example.demo.models.entity.ParticipanteViaje;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.entity.Viaje;
import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.dao.IViajeDao;
import com.example.demo.models.dao.IComunidadDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para enviar notificaciones push relacionadas con viajes
 */
@Service
public class ViajeNotificationService {

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    @Autowired
    private IViajeDao viajeDao;

    @Autowired
    private IComunidadDao comunidadDao;

    /**
     * Enviar recordatorio de viaje a todos los participantes
     * 
     * @param viaje Viaje a recordar
     * @param tipo  "dia_anterior" o "1_hora_antes"
     */
    public void enviarRecordatorioViaje(Viaje viaje, String tipo) {
        if (viaje == null || viaje.getParticipantes() == null) {
            System.out.println("⚠️ Viaje sin participantes");
            return;
        }

        String titulo;
        String cuerpo;
        String rutaNombre = viaje.getRuta() != null ? viaje.getRuta().getNombre() : "tu viaje";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'a las' HH:mm");
        String fechaFormateada = viaje.getFechaProgramada() != null
                ? viaje.getFechaProgramada().format(formatter)
                : "próximamente";

        if ("dia_anterior".equals(tipo)) {
            titulo = "🗓️ Viaje mañana: " + rutaNombre;
            cuerpo = "Recuerda que mañana tienes programado el viaje \"" + rutaNombre + "\" a las " +
                    (viaje.getFechaProgramada() != null ? viaje.getFechaProgramada().toLocalTime() : "hora programada")
                    +
                    ". ¡Prepárate!";
        } else if ("1_hora_antes".equals(tipo)) {
            titulo = "⏰ ¡Tu viaje comienza en 1 hora!";
            cuerpo = "El viaje \"" + rutaNombre + "\" comenzará en 1 hora. Es momento de prepararte.";
        } else {
            titulo = "📍 Recordatorio de viaje";
            cuerpo = "Tienes el viaje \"" + rutaNombre + "\" programado para el " + fechaFormateada;
        }

        int notificacionesEnviadas = 0;
        for (ParticipanteViaje participante : viaje.getParticipantes()) {
            // No enviar a participantes que cancelaron
            if ("cancela".equals(participante.getEstado())) {
                continue;
            }

            Usuario usuario = participante.getUsuario();
            if (usuario == null || usuario.getFcmToken() == null || usuario.getFcmToken().isEmpty()) {
                System.out.println("⚠️ Usuario sin FCM token: " + (usuario != null ? usuario.getId() : "null"));
                continue;
            }

            String resultado = firebaseMessagingService.enviarNotificacionViaje(
                    usuario.getFcmToken(),
                    titulo,
                    cuerpo,
                    viaje.getId(),
                    tipo);

            if (resultado != null) {
                notificacionesEnviadas++;
            }
        }

        System.out.println("✅ Enviadas " + notificacionesEnviadas + " notificaciones para viaje #" + viaje.getId()
                + " (tipo: " + tipo + ")");
    }

    /**
     * Buscar viajes programados en el rango de tiempo especificado
     */
    public List<Viaje> buscarViajesEnRango(LocalDateTime desde, LocalDateTime hasta) {
        try {
            System.out.println("🔎 Buscando en DAO viajes entre " + desde + " y " + hasta);
            List<Viaje> viajes = viajeDao.findByEstadoAndFechaProgramadaBetweenWithParticipantes("programado", desde,
                    hasta);
            System.out.println("✅ Query exitosa. Viajes encontrados: " + (viajes != null ? viajes.size() : "null"));
            return viajes != null ? viajes : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("❌ ERROR en buscarViajesEnRango: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * NUEVO: Buscar viajes "en curso" que comenzaron hace poco (ventana de 2 horas)
     * para enviar recordatorios a invitados que no ingresaron
     */
    public List<Viaje> buscarViajesEnCursoRecientes() {
        LocalDateTime hace2Horas = LocalDateTime.now().minusHours(2);
        LocalDateTime ahora = LocalDateTime.now();
        return viajeDao.findByEstadoAndFechaProgramadaBetween("en_curso", hace2Horas, ahora);
    }

    /**
     * Notificar a todos los participantes que el viaje ha sido cancelado por el
     * organizador
     * 
     * @param viaje             Viaje que fue cancelado
     * @param nombreOrganizador Nombre del organizador que canceló
     * @param organizadorId     ID del organizador (para no enviarle notificación a
     *                          él mismo)
     */
    public void notificarViajeCancelado(Viaje viaje, String nombreOrganizador, Long organizadorId) {
        if (viaje == null || viaje.getParticipantes() == null) {
            System.out.println("⚠️ Viaje sin participantes para notificar cancelación");
            return;
        }

        String rutaNombre = viaje.getRuta() != null ? viaje.getRuta().getNombre() : "el viaje";
        String titulo = "⚠️ Viaje cancelado";
        String cuerpo = nombreOrganizador + " ha cancelado el viaje \"" + rutaNombre + "\"";

        int notificacionesEnviadas = 0;
        for (ParticipanteViaje participante : viaje.getParticipantes()) {
            Usuario usuario = participante.getUsuario();

            // No enviar al organizador (quien canceló)
            if (usuario == null || usuario.getId().equals(organizadorId)) {
                continue;
            }

            if (usuario.getFcmToken() == null || usuario.getFcmToken().isEmpty()) {
                System.out.println("⚠️ Usuario sin FCM token: " + usuario.getId());
                continue;
            }

            String resultado = firebaseMessagingService.enviarNotificacionViaje(
                    usuario.getFcmToken(),
                    titulo,
                    cuerpo,
                    viaje.getId(),
                    "viaje_cancelado");

            if (resultado != null) {
                notificacionesEnviadas++;
            }
        }

        System.out.println(
                "✅ Enviadas " + notificacionesEnviadas + " notificaciones de cancelación para viaje #" + viaje.getId());
    }

    /**
     * Notificar a todos los miembros de una comunidad que se ha compartido una
     * nueva ruta
     * 
     * @param comunidadId   ID de la comunidad
     * @param nombreUsuario Nombre del usuario que compartió
     * @param nombreRuta    Nombre de la ruta compartida
     * @param organizadorId ID del organizador (para no enviarle notificación a él
     *                      mismo)
     */
    public void notificarRutaCompartida(Long comunidadId, String nombreUsuario, String nombreRuta, Long organizadorId) {
        if (comunidadId == null) {
            System.out.println("⚠️ comunidadId es null, no se puede notificar");
            return;
        }

        try {
            // Obtener comunidad con sus miembros
            Comunidad comunidad = comunidadDao.findByIdWithMiembros(comunidadId).orElse(null);
            if (comunidad == null) {
                System.out.println("⚠️ Comunidad no encontrada: " + comunidadId);
                return;
            }

            String titulo = "🗺️ Nueva ruta en " + (comunidad.getNombre() != null ? comunidad.getNombre() : "tu grupo");
            String cuerpo = nombreUsuario + " ha agregado la ruta \"" + nombreRuta + "\" al grupo";

            int notificacionesEnviadas = 0;

            // Notificar a todos los miembros
            for (Usuario miembro : comunidad.getMiembros()) {
                // No enviar al organizador
                if (miembro.getId().equals(organizadorId)) {
                    continue;
                }

                if (miembro.getFcmToken() == null || miembro.getFcmToken().isEmpty()) {
                    System.out.println("⚠️ Miembro sin FCM token: " + miembro.getId());
                    continue;
                }

                String resultado = firebaseMessagingService.enviarNotificacion(
                        miembro.getFcmToken(),
                        titulo,
                        cuerpo,
                        comunidadId);

                if (resultado != null) {
                    notificacionesEnviadas++;
                }
            }

            // También notificar al creador de la comunidad (si no es el organizador)
            Usuario creador = comunidad.getCreador();
            if (creador != null && !creador.getId().equals(organizadorId)) {
                if (creador.getFcmToken() != null && !creador.getFcmToken().isEmpty()) {
                    String resultado = firebaseMessagingService.enviarNotificacion(
                            creador.getFcmToken(),
                            titulo,
                            cuerpo,
                            comunidadId);
                    if (resultado != null) {
                        notificacionesEnviadas++;
                    }
                }
            }

            System.out.println("✅ Enviadas " + notificacionesEnviadas +
                    " notificaciones de ruta compartida para comunidad #" + comunidadId);
        } catch (Exception e) {
            System.err.println("❌ Error al notificar ruta compartida: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
