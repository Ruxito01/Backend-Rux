package com.example.demo.models.ServiceImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.models.dao.IFotoUsuarioDao;
import com.example.demo.models.entity.FotoUsuario;
import com.example.demo.models.service.IFotoUsuarioService;

/**
 * Implementación del servicio para FotoUsuario.
 * Maneja la lógica de negocio y transacciones.
 */
@Service
public class FotoUsuarioServiceImpl implements IFotoUsuarioService {

    @Autowired
    private IFotoUsuarioDao fotoUsuarioDao;

    @Override
    @Transactional(readOnly = true)
    public List<FotoUsuario> findAll() {
        return fotoUsuarioDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FotoUsuario> findById(Long id) {
        return fotoUsuarioDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FotoUsuario> findByUsuarioId(Long usuarioId) {
        return fotoUsuarioDao.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional
    public FotoUsuario save(FotoUsuario fotoUsuario) {
        return fotoUsuarioDao.save(fotoUsuario);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        fotoUsuarioDao.deleteById(id);
    }
}
