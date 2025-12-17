package com.example.demo.models.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para enviar notificaciones push via Firebase Cloud Messaging
 */
@Service
public class FirebaseMessagingService {

    /**
     * Enviar notificación push a un dispositivo específico
     * 
     * @param token           Token FCM del dispositivo
     * @param titulo          Título de la notificación
     * @param cuerpo          Cuerpo/mensaje de la notificación
     * @param dataComunidadId ID de la comunidad para navegación
     * @return ID del mensaje si fue exitoso, null si falló
     */
    public String enviarNotificacion(String token, String titulo, String cuerpo, Long comunidadId) {
        if (token == null || token.isEmpty()) {
            System.out.println("⚠️ Token FCM vacío, no se puede enviar notificación");
            return null;
        }

        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(titulo)
                            .setBody(cuerpo)
                            .build())
                    .putData("comunidadId", comunidadId != null ? comunidadId.toString() : "")
                    .putData("type", "chat_message")
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ Notificación enviada: " + response);
            return response;
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ Error enviando notificación: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Error general FCM: " + e.getMessage());
            return null;
        }
    }

    /**
     * Enviar notificación a múltiples dispositivos
     * 
     * @param tokens      Lista de tokens FCM
     * @param titulo      Título de la notificación
     * @param cuerpo      Cuerpo del mensaje
     * @param comunidadId ID de la comunidad
     */
    public void enviarNotificacionMultiple(List<String> tokens, String titulo, String cuerpo, Long comunidadId) {
        if (tokens == null || tokens.isEmpty()) {
            System.out.println("⚠️ Lista de tokens vacía");
            return;
        }

        // Filtrar tokens nulos o vacíos
        List<String> tokensValidos = tokens.stream()
                .filter(t -> t != null && !t.isEmpty())
                .toList();

        System.out.println("📤 Enviando notificación a " + tokensValidos.size() + " dispositivos");

        for (String token : tokensValidos) {
            enviarNotificacion(token, titulo, cuerpo, comunidadId);
        }
    }
}
