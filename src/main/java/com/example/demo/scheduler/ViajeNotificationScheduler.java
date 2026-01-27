package com.example.demo.scheduler;

import com.example.demo.models.entity.Viaje;
import com.example.demo.models.service.ViajeNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Scheduler para enviar recordatorios automáticos de viajes programados
 * Configurado para zona horaria de Ecuador (America/Guayaquil - ECT/UTC-5)
 */
@Component
public class ViajeNotificationScheduler {

    @Autowired
    private ViajeNotificationService viajeNotificationService;

    // Zona horaria de Ecuador
    private static final ZoneId ECUADOR_ZONE = ZoneId.of("America/Guayaquil");

    // Cache de viajes que ya recibieron notificación (evitar duplicados)
    private final Set<String> notificacionesEnviadas = new HashSet<>();

    /**
     * Ejecuta cada 10 minutos para buscar viajes que empiezan en 1 hora
     * y enviar recordatorio.
     */
    @Scheduled(cron = "0 */10 * * * *") // Cada 10 minutos
    @Transactional(readOnly = true)
    public void enviarRecordatorio1HoraAntes() {
        System.out.println("=== INICIO SCHEDULER ===");
        try {
            System.out.println("LOG 1: Obteniendo zona horaria");
            ZonedDateTime ahoraEcuador = ZonedDateTime.now(ECUADOR_ZONE);

            System.out.println("LOG 2: Convirtiendo a LocalDateTime");
            LocalDateTime ahora = ahoraEcuador.toLocalDateTime();

            System.out.println("LOG 3: Calculando rangos");
            LocalDateTime en50Minutos = ahora.plusMinutes(50);
            LocalDateTime en70Minutos = ahora.plusMinutes(70);

            System.out.println("LOG 4: Creando formatter");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            System.out.println("LOG 5: Hora actual: " + ahora.format(formatter));

            System.out.println("LOG 6: Antes de buscar viajes");
            List<Viaje> viajes = viajeNotificationService.buscarViajesEnRango(en50Minutos, en70Minutos);

            System.out.println("LOG 7: Después de buscar. Encontrados: " + (viajes != null ? viajes.size() : "NULL"));

            if (viajes != null && !viajes.isEmpty()) {
                System.out.println("LOG 8: Procesando " + viajes.size() + " viajes");

                for (Viaje viaje : viajes) {
                    System.out.println("LOG 9: Viaje ID=" + viaje.getId());
                    String key = viaje.getId() + "_1h";

                    if (!notificacionesEnviadas.contains(key)) {
                        viajeNotificationService.enviarRecordatorioViaje(viaje, "1_hora_antes");
                        notificacionesEnviadas.add(key);
                        System.out.println("✅ Notificación enviada para viaje #" + viaje.getId());
                    }
                }
            }

            System.out.println("=== FIN SCHEDULER EXITOSO ===");
        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ejecuta diariamente a las 8:00 PM (zona horaria Ecuador) para recordatorios
     * del día siguiente
     * IMPORTANTE: zone="America/Guayaquil" asegura que se ejecute a las 8PM hora
     * Ecuador
     * independientemente de la zona horaria del servidor (us-central1 = UTC-6)
     */
    @Scheduled(cron = "0 0 20 * * *", zone = "America/Guayaquil") // 8 PM hora Ecuador
    @Transactional(readOnly = true)
    public void enviarRecordatorioDiaAnterior() {
        // Usar zona horaria de Ecuador
        ZonedDateTime ahoraEcuador = ZonedDateTime.now(ECUADOR_ZONE);
        LocalDateTime mananaInicio = ahoraEcuador.plusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime mananaFin = mananaInicio.plusDays(1).minusSeconds(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("========================================");
        System.out.println("🔍 [Scheduler 1d] Hora actual Ecuador: " + ahoraEcuador.format(formatter));
        System.out.println("🔍 Buscando viajes de mañana entre: " + mananaInicio.format(formatter) + " y "
                + mananaFin.format(formatter));

        // Buscar viajes programados para mañana
        List<Viaje> viajes = viajeNotificationService.buscarViajesEnRango(mananaInicio, mananaFin);

        System.out.println("🔍 Viajes encontrados: " + viajes.size());
        System.out.println("========================================");

        for (Viaje viaje : viajes) {
            String key = viaje.getId() + "_1d";

            if (notificacionesEnviadas.contains(key)) {
                continue;
            }

            try {
                viajeNotificationService.enviarRecordatorioViaje(viaje, "dia_anterior");
                notificacionesEnviadas.add(key);
                System.out.println("✅ Recordatorio 1 día enviado para viaje #" + viaje.getId());
            } catch (Exception e) {
                System.err.println(
                        "❌ Error enviando recordatorio 1 día para viaje #" + viaje.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Limpiar cache de notificaciones enviadas cada domingo a las 3 AM hora Ecuador
     */
    @Scheduled(cron = "0 0 3 * * SUN", zone = "America/Guayaquil")
    public void limpiarCacheNotificaciones() {
        int size = notificacionesEnviadas.size();
        notificacionesEnviadas.clear();
        System.out.println("🧹 Cache semanal limpiado. Entradas eliminadas: " + size);
    }
}
