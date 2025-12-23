package com.example.demo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * Configuración de Firebase Admin SDK
 * Inicializa Firebase usando las credenciales de la cuenta de servicio
 */
@Component
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                // Intentar cargar credenciales del archivo
                InputStream serviceAccount = getClass().getClassLoader()
                        .getResourceAsStream("rux-movil-firebase-adminsdk-fbsvc-98bafcab56.json");

                if (serviceAccount != null) {
                    FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                            .build();
                    FirebaseApp.initializeApp(options);
                    System.out.println("✅ Firebase Admin SDK inicializado correctamente");
                } else {
                    // Si no hay archivo, usar credenciales por defecto de GCP
                    // IMPORTANTE: necesita el Project ID explícitamente
                    String projectId = System.getenv("GOOGLE_CLOUD_PROJECT");
                    if (projectId == null) {
                        projectId = "rux-movil"; // Fallback
                    }

                    FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.getApplicationDefault())
                            .setProjectId(projectId)
                            .build();
                    FirebaseApp.initializeApp(options);
                    System.out.println(
                            "✅ Firebase inicializado con credenciales por defecto de GCP (Project: " + projectId + ")");
                }
            }
        } catch (IOException e) {
            System.err.println("⚠️ No se pudo inicializar Firebase: " + e.getMessage());
            System.err.println("Las notificaciones push no estarán disponibles");
        }
    }
}
