package com.example.demo.models.controller;

import com.example.demo.models.service.ICodigoRecuperacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador para autenticación y recuperación de contraseña
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Endpoints de autenticación y recuperación de contraseña")
public class AuthController {

    @Autowired
    private ICodigoRecuperacionService codigoRecuperacionService;

    /**
     * Solicita un código de recuperación de contraseña
     * El código se envía al email proporcionado
     */
    @Operation(summary = "Solicitar código de recuperación de contraseña")
    @PostMapping("/solicitar-codigo")
    public ResponseEntity<Map<String, Object>> solicitarCodigo(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "El email es requerido"));
        }

        Map<String, Object> response = codigoRecuperacionService.generarYEnviarCodigo(email);
        return ResponseEntity.ok(response);
    }

    /**
     * Verifica si el código ingresado es válido
     */
    @Operation(summary = "Verificar código de recuperación")
    @PostMapping("/verificar-codigo")
    public ResponseEntity<Map<String, Object>> verificarCodigo(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String codigo = request.get("codigo");

        if (email == null || email.trim().isEmpty() || codigo == null || codigo.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email y código son requeridos"));
        }

        Map<String, Object> response = codigoRecuperacionService.verificarCodigo(email, codigo);
        return ResponseEntity.ok(response);
    }

    /**
     * Cambia la contraseña del usuario
     */
    @Operation(summary = "Cambiar contraseña después de verificar código")
    @PostMapping("/cambiar-contrasena")
    public ResponseEntity<Map<String, Object>> cambiarContrasena(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String nuevaContrasena = request.get("nuevaContrasena");

        if (email == null || email.trim().isEmpty() || nuevaContrasena == null || nuevaContrasena.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email y nueva contraseña son requeridos"));
        }

        if (nuevaContrasena.length() < 6) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "La contraseña debe tener al menos 6 caracteres"));
        }

        Map<String, Object> response = codigoRecuperacionService.cambiarContrasena(email, nuevaContrasena);
        return ResponseEntity.ok(response);
    }
}
