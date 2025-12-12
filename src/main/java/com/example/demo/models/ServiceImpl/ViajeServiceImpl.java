package com.example.demo.models.ServiceImpl;

import com.example.demo.models.dao.IViajeDao;
import com.example.demo.models.entity.Viaje;
import com.example.demo.models.service.IViajeService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ViajeServiceImpl implements IViajeService {

    @Autowired
    private IViajeDao dao;

    // Caracteres permitidos para el código (alfanumérico)
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 8; // Longitud solicitada: 8 caracteres
    private final java.security.SecureRandom random = new java.security.SecureRandom();

    @Override
    @Transactional(readOnly = true)
    public List<Viaje> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Viaje findById(@NonNull Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Viaje save(@NonNull Viaje entity) {
        // Generar código de invitación si no existe o está vacío, SOLO para nuevos
        // registros
        if (entity.getId() == null
                && (entity.getCodigoInvitacion() == null || entity.getCodigoInvitacion().trim().isEmpty())) {
            String codigoGenerado;
            do {
                codigoGenerado = generateRandomCode();
            } while (dao.existsByCodigoInvitacion(codigoGenerado));

            entity.setCodigoInvitacion(codigoGenerado);
        }

        return dao.save(entity);
    }

    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    @Override
    @Transactional
    public void deleteById(@NonNull Long id) {
        dao.deleteById(id);
    }
}
