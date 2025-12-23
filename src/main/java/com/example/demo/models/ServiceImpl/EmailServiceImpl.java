package com.example.demo.models.ServiceImpl;

import com.example.demo.models.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de email usando JavaMailSender
 */
@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public boolean enviarCodigoRecuperacion(String email, String codigo) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(email);
            mensaje.setSubject("🔐 Código de Recuperación - RÜX");
            mensaje.setText(
                    "¡Hola!\n\n" +
                            "Recibimos una solicitud para restablecer tu contraseña en RÜX.\n\n" +
                            "Tu código de verificación es:\n\n" +
                            "    " + codigo + "\n\n" +
                            "Este código expira en 15 minutos.\n\n" +
                            "Si no solicitaste este código, ignora este mensaje.\n\n" +
                            "Saludos,\n" +
                            "El equipo de RÜX 🏍️");

            mailSender.send(mensaje);
            return true;
        } catch (Exception e) {
            System.err.println("Error enviando email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
