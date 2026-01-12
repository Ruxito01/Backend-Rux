package com.example.demo.models.ServiceImpl;

import com.example.demo.models.dao.ICodigoRecuperacionDao;
import com.example.demo.models.dao.IUsuarioDao;
import com.example.demo.models.entity.CodigoRecuperacion;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.service.ICodigoRecuperacionService;
import com.example.demo.models.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación del servicio de códigos de recuperación
 */
@Service
public class CodigoRecuperacionServiceImpl implements ICodigoRecuperacionService {

    @Autowired
    private ICodigoRecuperacionDao codigoRecuperacionDao;

    @Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private IEmailService emailService;

    private final SecureRandom random = new SecureRandom();

    @Override
    @Transactional
    public Map<String, Object> generarYEnviarCodigo(String email) {
        Map<String, Object> response = new HashMap<>();

        // Verificar que el email existe en el sistema
        Optional<Usuario> usuarioOpt = usuarioDao.findByEmail(email.toLowerCase().trim());
        if (usuarioOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "No existe una cuenta con este correo electrónico");
            return response;
        }

        // Eliminar códigos anteriores del mismo email
        codigoRecuperacionDao.deleteByEmail(email.toLowerCase().trim());

        // Generar código aleatorio de 6 dígitos
        String codigo = String.format("%06d", random.nextInt(1000000));

        // Guardar el código en la base de datos
        CodigoRecuperacion codigoRecuperacion = new CodigoRecuperacion();
        codigoRecuperacion.setEmail(email.toLowerCase().trim());
        codigoRecuperacion.setCodigo(codigo);
        codigoRecuperacion.setFechaCreacion(LocalDateTime.now());
        codigoRecuperacion.setUsado(false);
        codigoRecuperacionDao.save(codigoRecuperacion);

        // Enviar el código por email
        boolean enviado = emailService.enviarCodigoRecuperacion(email, codigo);

        if (enviado) {
            response.put("success", true);
            response.put("message", "Código de verificación enviado a tu correo");
        } else {
            response.put("success", false);
            response.put("message", "Error al enviar el correo. Intenta de nuevo más tarde.");
        }

        return response;
    }

    /**
     * Genera y envía código SIN validar que el usuario exista
     * Usado para verificar email durante el registro
     */
    @Override
    @Transactional
    public Map<String, Object> generarCodigoParaRegistro(String email) {
        Map<String, Object> response = new HashMap<>();

        // Validar formato de email básico
        if (!email.contains("@") || !email.contains(".")) {
            response.put("success", false);
            response.put("message", "Formato de email inválido");
            return response;
        }

        // Eliminar códigos anteriores del mismo email
        codigoRecuperacionDao.deleteByEmail(email.toLowerCase().trim());

        // Generar código aleatorio de 6 dígitos
        String codigo = String.format("%06d", random.nextInt(1000000));

        // Guardar el código en la base de datos
        CodigoRecuperacion codigoRecuperacion = new CodigoRecuperacion();
        codigoRecuperacion.setEmail(email.toLowerCase().trim());
        codigoRecuperacion.setCodigo(codigo);
        codigoRecuperacion.setFechaCreacion(LocalDateTime.now());
        codigoRecuperacion.setUsado(false);
        codigoRecuperacionDao.save(codigoRecuperacion);

        // Enviar el código por email (con mensaje de registro)
        boolean enviado = emailService.enviarCodigoVerificacionRegistro(email, codigo);

        if (enviado) {
            response.put("success", true);
            response.put("message", "Código de verificación enviado a tu correo");
        } else {
            response.put("success", false);
            response.put("message", "Error al enviar el correo. Intenta de nuevo más tarde.");
        }

        return response;
    }

    @Override
    @Transactional
    public Map<String, Object> verificarCodigo(String email, String codigo) {
        Map<String, Object> response = new HashMap<>();

        Optional<CodigoRecuperacion> codigoOpt = codigoRecuperacionDao
                .findByEmailAndCodigoAndUsadoFalse(email.toLowerCase().trim(), codigo);

        if (codigoOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Código inválido o ya fue utilizado");
            return response;
        }

        CodigoRecuperacion codigoRecuperacion = codigoOpt.get();

        // Verificar si el código ha expirado (15 minutos)
        if (codigoRecuperacion.haExpirado()) {
            response.put("success", false);
            response.put("message", "El código ha expirado. Solicita uno nuevo.");
            return response;
        }

        // Marcar el código como usado
        codigoRecuperacion.setUsado(true);
        codigoRecuperacionDao.save(codigoRecuperacion);

        response.put("success", true);
        response.put("message", "Código verificado correctamente");
        return response;
    }

    @Override
    @Transactional
    public Map<String, Object> cambiarContrasena(String email, String nuevaContrasena) {
        Map<String, Object> response = new HashMap<>();

        // Buscar el usuario por email
        Optional<Usuario> usuarioOpt = usuarioDao.findByEmail(email.toLowerCase().trim());
        if (usuarioOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Usuario no encontrado");
            return response;
        }

        Usuario usuario = usuarioOpt.get();

        // Actualizar la contraseña (sin encriptar por ahora - mantener consistencia con
        // el sistema actual)
        usuario.setContrasena(nuevaContrasena);
        usuarioDao.save(usuario);

        // Limpiar todos los códigos de recuperación del email
        codigoRecuperacionDao.deleteByEmail(email.toLowerCase().trim());

        response.put("success", true);
        response.put("message", "Contraseña actualizada correctamente");
        return response;
    }
}
