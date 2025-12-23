package com.example.demo.models.ServiceImpl;

import com.example.demo.models.dao.IMensajeComunidadDao;
import com.example.demo.models.dao.IMiembroComunidadDao;
import com.example.demo.models.dao.IUsuarioDao;
import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.entity.MensajeComunidad;
import com.example.demo.models.entity.MiembroComunidad;
import com.example.demo.models.entity.UbicacionUsuario;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.service.FirebaseMessagingService;
import com.example.demo.models.service.IComunidadService;
import com.example.demo.models.service.IRuxSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementación del servicio WebSocket
 * Maneja la lógica de broadcasting para chat y GPS
 * Ahora también envía push notifications a los miembros
 */
@Service
public class RuxSocketServiceImpl implements IRuxSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private IMensajeComunidadDao mensajeComunidadDao;

    @Autowired
    private IComunidadService comunidadService;

    @Autowired
    private IMiembroComunidadDao miembroComunidadDao;

    @Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    @Override
    @Transactional
    public void enviarMensaje(MensajeComunidad mensaje) {
        // 1. Asegurar que tiene fecha
        if (mensaje.getFechaEnvio() == null) {
            mensaje.setFechaEnvio(LocalDateTime.now());
        }

        // 2. Guardar en base de datos
        MensajeComunidad mensajeGuardado = mensajeComunidadDao.save(mensaje);

        // 3. Hacer broadcast a todos los suscritos a esta comunidad
        Long comunidadId = mensajeGuardado.getComunidad().getId();
        String destino = "/topic/comunidad/" + comunidadId;
        messagingTemplate.convertAndSend(destino, mensajeGuardado);

        System.out.println("💬 Mensaje enviado a " + destino);

        // 4. Enviar push notifications a los miembros (en hilo separado para no
        // bloquear)
        new Thread(() -> enviarPushAMiembros(mensajeGuardado)).start();
    }

    /**
     * Envía push notifications a todos los miembros de la comunidad
     * excepto al que envió el mensaje
     */
    private void enviarPushAMiembros(MensajeComunidad mensaje) {
        try {
            Long comunidadId = mensaje.getComunidad().getId();
            Long remitenteId = mensaje.getUsuario().getId();
            String contenido = mensaje.getContenido();

            // Cargar usuario completo desde la BD para obtener nombre/alias
            Usuario remitente = usuarioDao.findById(remitenteId).orElse(null);
            String nombreRemitente = "Usuario";
            if (remitente != null) {
                // Preferir alias sobre nombre
                nombreRemitente = (remitente.getAlias() != null && !remitente.getAlias().isEmpty())
                        ? remitente.getAlias()
                        : (remitente.getNombre() != null ? remitente.getNombre() : "Usuario");
            }

            System.out.println("🔍 Remitente: " + nombreRemitente + " (ID: " + remitenteId + ")");

            // Obtener la comunidad para el nombre
            Comunidad comunidad = comunidadService.findById(comunidadId);
            if (comunidad == null)
                return;

            // Obtener miembros ACTIVOS usando MiembroComunidadDao
            List<MiembroComunidad> membresias = miembroComunidadDao.findByComunidadId(comunidadId);

            // Filtrar tokens: solo miembros ACTIVOS con token FCM que NO son el remitente
            List<String> tokens = membresias.stream()
                    .filter(m -> m.getEstado() == null || "activo".equals(m.getEstado()))
                    .map(MiembroComunidad::getUsuario)
                    .filter(u -> !u.getId().equals(remitenteId))
                    .map(Usuario::getFcmToken)
                    .filter(token -> token != null && !token.isEmpty())
                    .collect(Collectors.toList());

            System.out.println("🔍 Miembros encontrados: " + membresias.size() + ", tokens válidos: " + tokens.size());

            if (!tokens.isEmpty()) {
                String titulo = comunidad.getNombre();
                String cuerpo = nombreRemitente + ": " +
                        (contenido.length() > 50 ? contenido.substring(0, 47) + "..." : contenido);

                System.out.println("📤 Enviando push a " + tokens.size() + " miembros de " + titulo);
                firebaseMessagingService.enviarNotificacionMultiple(tokens, titulo, cuerpo, comunidadId);
            } else {
                System.out.println("⚠️ No hay tokens válidos para enviar notificaciones");
            }
        } catch (Exception e) {
            System.err.println("❌ Error enviando push notifications: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void actualizarUbicacion(UbicacionUsuario ubicacion) {
        // NO guardar en DB - solo broadcast para performance
        // Enviar inmediatamente a todos los participantes del viaje
        String destino = "/topic/viaje/" + ubicacion.getViajeId();
        messagingTemplate.convertAndSend(destino, ubicacion);

        System.out.println("📍 Ubicación actualizada en " + destino +
                " - Lat: " + ubicacion.getLatitud() + ", Lng: " + ubicacion.getLongitud());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MensajeComunidad> obtenerHistorialMensajes(Long comunidadId) {
        // Retornar últimos 50 mensajes ordenados por fecha descendente
        return mensajeComunidadDao.findTop50ByComunidadIdOrderByFechaEnvioDesc(comunidadId);
    }
}
