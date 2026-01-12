package com.example.demo.models.ServiceImpl;

import com.example.demo.models.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.mail.username:rux01rux@gmail.com}")
    private String fromEmail;

    @Override
    public boolean enviarCodigoRecuperacion(String email, String codigo) {
        try {
            System.out.println("📧 Intentando enviar email a: " + email);
            System.out.println("📧 Desde: " + fromEmail);

            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(fromEmail);
            mensaje.setTo(email);
            mensaje.setSubject("Codigo de Recuperacion - RUX");
            mensaje.setText(
                    "Hola!\n\n" +
                            "Recibimos una solicitud para restablecer tu contrasena en RUX.\n\n" +
                            "Tu codigo de verificacion es:\n\n" +
                            "    " + codigo + "\n\n" +
                            "Este codigo expira en 15 minutos.\n\n" +
                            "Si no solicitaste este codigo, ignora este mensaje.\n\n" +
                            "Saludos,\n" +
                            "El equipo de RUX");

            mailSender.send(mensaje);
            System.out.println("✅ Email enviado correctamente a: " + email);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error enviando email a " + email + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean enviarCodigoVerificacionRegistro(String email, String codigo) {
        try {
            System.out.println("📧 Enviando código de verificación de registro a: " + email);

            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(fromEmail);
            mensaje.setTo(email);
            mensaje.setSubject("Verifica tu Email - RÜX");
            mensaje.setText(
                    "¡Bienvenido a RÜX!\n\n" +
                            "Estás a un paso de completar tu registro.\n\n" +
                            "Tu código de verificación es:\n\n" +
                            "    " + codigo + "\n\n" +
                            "Este código expira en 15 minutos.\n\n" +
                            "Si no creaste una cuenta en RÜX, ignora este mensaje.\n\n" +
                            "¡Nos vemos en la comunidad!\n" +
                            "El equipo de RÜX");

            mailSender.send(mensaje);
            System.out.println("✅ Código de registro enviado a: " + email);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error enviando código de registro a " + email + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
