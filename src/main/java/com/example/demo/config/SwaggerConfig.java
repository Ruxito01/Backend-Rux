package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.forward-headers-strategy:framework}")
    private String forwardHeadersStrategy;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Backend-Rux API")
                        .version("1.0")
                        .description("API para la aplicación RÜX - Navegación social off-road"))
                .servers(List.of(
                        new Server()
                                .url("https://backend-rux-626914317382.us-central1.run.app")
                                .description("Servidor de Producción (Cloud Run)"),
                        new Server()
                                .url("http://localhost:8090")
                                .description("Servidor Local de Desarrollo")));
    }
}
