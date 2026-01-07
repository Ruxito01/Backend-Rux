package com.example.demo.scheduler;

import com.example.demo.models.entity.Viaje;
import com.example.demo.models.service.ViajeNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Scheduler para enviar recordatorios automáticos de viajes programados
 */
@Component
public class ViajeNotificationScheduler {

    @Autowired
    private ViajeNotificationService viajeNotificationService;

    // Cache de viajes que ya recibieron notificación (evitar duplicados)
    private final Set<String> notificacionesEnviadas = new HashSet<>();

    /**
     * Ejecuta cada 10 minutos para buscar viajes que empiezan en 1 hora
     * y enviar recordatorio.
     */
    @Scheduled(cron = "0 */10 * * * *") // Cada 10 minutos
    public void enviarRecordatorio1HoraAntes() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime en50Minutos = ahora.plusMinutes(50);
        LocalDateTime en70Minutos = ahora.plusMinutes(70);

        // Buscar viajes programados que empiezan entre 50 y 70 minutos
        List<Viaje> viajes = viajeNotificationService.buscarViajesEnRango(en50Minutos, en70Minutos);

        System.out.println("🔍 [Scheduler 1h] Verificando viajes en 1 hora. Encontrados: " + viajes.size());

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
            }
        }

        // Limpiar cache cada 1000 entradas para evitar memory leak
        if (notificacionesEnviadas.size() > 1000) {
            notificacionesEnviadas.clear();
            System.out.println("🧹 Cache de notificaciones limpiado");
        }
    }

    /**
     * Ejecuta diariamente a las 8:00 PM para recordatorios del día siguiente
     */
    @Scheduled(cron = "0 0 20 * * *") // Todos los días a las 8 PM
    public void enviarRecordatorioDiaAnterior() {
        LocalDateTime mananaInicio = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime mananaFin = mananaInicio.plusDays(1).minusSeconds(1);

        // Buscar viajes programados para mañana
        List<Viaje> viajes = viajeNotificationService.buscarViajesEnRango(mananaInicio, mananaFin);

        System.out.println("🔍 [Scheduler 1d] Verificando viajes de mañana. Encontrados: " + viajes.size());

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
