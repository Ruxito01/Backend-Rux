package com.example.demo.models.service;

/**
 * Servicio para envío de correos electrónicos
 */
public interface IEmailService {

    /**
     * Envía un correo con el código de recuperación de contraseña
     * 
     * @param email  Correo del destinatario
     * @param codigo Código de 6 dígitos
     * @return true si se envió correctamente, false en caso de error
     */
    boolean enviarCodigoRecuperacion(String email, String codigo);

    /**
     * Envía un correo con el código de verificación para registro
     * 
     * @param email  Correo del destinatario
     * @param codigo Código de 6 dígitos
     * @return true si se envió correctamente, false en caso de error
     */
    boolean enviarCodigoVerificacionRegistro(String email, String codigo);
}
