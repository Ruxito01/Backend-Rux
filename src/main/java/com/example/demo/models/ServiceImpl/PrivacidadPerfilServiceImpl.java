package com.example.demo.models.ServiceImpl;

import com.example.demo.models.dao.IPrivacidadPerfilDao;
import com.example.demo.models.entity.PrivacidadPerfil;
import com.example.demo.models.service.IPrivacidadPerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * Implementacion del servicio de privacidad del perfil.
 */
@Service
public class PrivacidadPerfilServiceImpl implements IPrivacidadPerfilService {

    @Autowired
    private IPrivacidadPerfilDao privacidadPerfilDao;

    @Override
    @Transactional(readOnly = true)
    public Optional<PrivacidadPerfil> findByUsuarioId(Long usuarioId) {
        return privacidadPerfilDao.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional
    public PrivacidadPerfil save(PrivacidadPerfil privacidad) {
        return privacidadPerfilDao.save(privacidad);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsuarioId(Long usuarioId) {
        return privacidadPerfilDao.existsByUsuarioId(usuarioId);
    }
}
