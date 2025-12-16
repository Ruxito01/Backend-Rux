package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuración de WebSocket para RÜX
 * Habilita comunicación en tiempo real para:
 * - Chat de comunidades
 * - Tracking GPS en viajes
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefijo para envíos desde el cliente al servidor
        config.setApplicationDestinationPrefixes("/app");

        // Prefijo para suscripciones (server -> client)
        // /topic/comunidad/{id} - Para chat de comunidad
        // /topic/viaje/{id} - Para GPS tracking
        config.enableSimpleBroker("/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint para conectarse al WebSocket
        registry.addEndpoint("/rux-websocket")
                .setAllowedOriginPatterns("*"); // Permitir todas las IP (ajustar en producción)
    }
}
