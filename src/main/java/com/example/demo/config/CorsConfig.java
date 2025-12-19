package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración global de CORS para permitir peticiones desde orígenes
 * específicos.
 * Configurado para permitir peticiones desde:
 * - Frontend React local (http://localhost:5173)
 * - Despliegue en Vercel
 * (https://react-r-31yvs8yxj-ruxs-projects-4a5cb3ee.vercel.app)
 * - Dominio personalizado de Vercel (https://react-r-x.vercel.app)
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplicar a todos los endpoints
                        .allowedOrigins(
                                "http://localhost:5173", // Frontend React local
                                "https://react-r-31yvs8yxj-ruxs-projects-4a5cb3ee.vercel.app", // Vercel deployment
                                "https://react-r-x.vercel.app" // Dominio personalizado de Vercel (si aplica)
                )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedHeaders("*")
                        .exposedHeaders("*")
                        .allowCredentials(true) // Permitir cookies y autenticación
                        .maxAge(3600); // Cache de preflight durante 1 hora
            }
        };
    }
}
