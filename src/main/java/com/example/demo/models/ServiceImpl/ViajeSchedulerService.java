package com.example.demo.models.ServiceImpl;

import com.example.demo.models.dao.IViajeDao;
import com.example.demo.models.entity.Viaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Servicio de tareas programadas para gestión automática de viajes.
 * Cancela automáticamente viajes programados que han excedido el tiempo límite
 * sin que ningún participante haya ingresado.
 * Configurado para zona horaria de Ecuador (America/Guayaquil)
 */
@Service
public class ViajeSchedulerService {

    @Autowired
    private IViajeDao viajeDao;

    // Zona horaria de Ecuador
    private static final ZoneId ECUADOR_ZONE = ZoneId.of("America/Guayaquil");

    /**
     * Tarea programada que se ejecuta cada 5 minutos.
     * Cancela viajes programados que:
     * 1. Tienen estado 'programado'
     * 2. Han pasado más de 30 minutos desde su fecha programada
     * 3. Ningún participante tiene estado 'ingresa'
     */
    @Scheduled(fixedRate = 300000) // Cada 5 minutos (300,000 ms)
    @Transactional
    public void cancelarViajesExpirados() {
        System.out.println("⏰ Ejecutando tarea: Cancelar viajes expirados...");

        // Usar zona horaria de Ecuador
        ZonedDateTime ahoraEcuador = ZonedDateTime.now(ECUADOR_ZONE);
        LocalDateTime ahora = ahoraEcuador.toLocalDateTime();
        LocalDateTime limiteExpiracion = ahora.minusMinutes(30);

        System.out.println("🕐 Hora actual Ecuador: " + ahora);
        System.out.println("⏱️ Límite de expiración: " + limiteExpiracion);

        // Buscar todos los viajes en estado 'programado'
        List<Viaje> viajesProgramados = viajeDao.findAll().stream()
                .filter(v -> "programado".equals(v.getEstado()))
                .filter(v -> v.getFechaProgramada() != null)
                .filter(v -> v.getFechaProgramada().isBefore(limiteExpiracion))
                .toList();

        System.out.println("🔍 Viajes programados expirados encontrados: " + viajesProgramados.size());

        for (Viaje viaje : viajesProgramados) {
            // Verificar si algún participante ya ingresó
            boolean hayIngresado = viaje.getParticipantes().stream()
                    .anyMatch(p -> p.getEstado() == com.example.demo.models.entity.EstadoParticipante.ingresa);

            if (!hayIngresado) {
                // Cancelar el viaje y guardar fecha fin
                viaje.setEstado("cancelado");
                viaje.setFechaFinReal(ahora);

                // Cambiar estado de todos los participantes que no ingresaron a 'cancela'
                viaje.getParticipantes().forEach(p -> {
                    if (p.getEstado() == com.example.demo.models.entity.EstadoParticipante.registrado) {
                        p.setEstado(com.example.demo.models.entity.EstadoParticipante.cancela);
                    }
                });

                viajeDao.save(viaje);

                System.out.println("🚫 Viaje " + viaje.getId() + " cancelado automáticamente. " +
                        "Fecha programada: " + viaje.getFechaProgramada() +
                        ", Límite: " + limiteExpiracion);
            }
        }

        System.out.println("✅ Tarea completada. Viajes verificados: " + viajesProgramados.size());
    }
}
