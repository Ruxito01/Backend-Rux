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
        try {
            // Usar zona horaria de Ecuador explícitamente
            ZonedDateTime ahoraEcuador = ZonedDateTime.now(ECUADOR_ZONE);
            LocalDateTime ahora = ahoraEcuador.toLocalDateTime();
            LocalDateTime en50Minutos = ahora.plusMinutes(50);
            LocalDateTime en70Minutos = ahora.plusMinutes(70);

            // Log detallado para debugging
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            System.out.println("========================================");
            System.out.println("🔍 [Scheduler 1h] Hora actual Ecuador: " + ahora.format(formatter));
            System.out.println(
                    "🔍 Buscando viajes entre: " + en50Minutos.format(formatter) + " y "
                            + en70Minutos.format(formatter));

            // Buscar viajes programados que empiezan entre 50 y 70 minutos
            List<Viaje> viajes = viajeNotificationService.buscarViajesEnRango(en50Minutos, en70Minutos);

            System.out.println("🔍 Viajes encontrados: " + viajes.size());

            // Mostrar detalles de cada viaje encontrado
            for (Viaje viaje : viajes) {
                if (viaje.getFechaProgramada() != null) {
                    System.out.println("   📍 Viaje #" + viaje.getId() + " - " +
                            (viaje.getRuta() != null ? viaje.getRuta().getNombre() : "Sin nombre") +
                            " - Programado: " + viaje.getFechaProgramada().format(formatter));
                }
            }
            System.out.println("========================================");

            for (Viaje viaje : viajes) {
                String key = viaje.getId() + "_1h";

                // Evitar notificaciones duplicadas
                if (notificacionesEnviadas.contains(key)) {
                    continue;
                }

                try {
                    viajeNotificationService.enviarRecordatorioViaje(viaje, "1_hora_antes");
                    notificacionesEnviadas.add(key);
                    System.out.println("✅ Recordatorio 1h enviado para viaje #" + viaje.getId());
                } catch (Exception e) {
                    System.err.println(
                            "❌ Error enviando recordatorio 1h para viaje #" + viaje.getId() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // Limpiar cache cada 1000 entradas para evitar memory leak
            if (notificacionesEnviadas.size() > 1000) {
                notificacionesEnviadas.clear();
                System.out.println("🧹 Cache de notificaciones limpiado");
            }
        } catch (Exception e) {
            System.err.println("❌ ERROR CRÍTICO en scheduler 1h: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ejecuta diariamente a las 8:00 PM (zona horaria Ecuador) para recordatorios
     * del día siguiente
     */
    @Scheduled(cron = "0 0 20 * * *") // Todos los días a las 8 PM
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
     * Limpiar cache de notificaciones enviadas cada domingo a las 3 AM
     */
    @Scheduled(cron = "0 0 3 * * SUN")
    public void limpiarCacheNotificaciones() {
        int size = notificacionesEnviadas.size();
        notificacionesEnviadas.clear();
        System.out.println("🧹 Cache semanal limpiado. Entradas eliminadas: " + size);
    }
}
