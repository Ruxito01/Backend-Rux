package com.example.demo.models.service;

import java.util.Map;

/**
 * Servicio para gestionar códigos de recuperación de contraseña
 */
public interface ICodigoRecuperacionService {

    /**
     * Genera un código de 6 dígitos y lo envía al email
     * 
     * @param email Email del usuario
     * @return Map con "success" (boolean) y "message" (String)
     */
    Map<String, Object> generarYEnviarCodigo(String email);

    /**
     * Verifica si el código ingresado es válido
     * 
     * @param email  Email del usuario
     * @param codigo Código de 6 dígitos
     * @return Map con "success" (boolean) y "message" (String)
     */
    Map<String, Object> verificarCodigo(String email, String codigo);

    /**
     * Cambia la contraseña del usuario
     * 
     * @param email           Email del usuario
     * @param nuevaContrasena Nueva contraseña
     * @return Map con "success" (boolean) y "message" (String)
     */
    Map<String, Object> cambiarContrasena(String email, String nuevaContrasena);
}
