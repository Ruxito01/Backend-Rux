package com.example.demo.models.ServiceImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.models.dao.IUsuarioDao;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioDao usuarioDao;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return usuarioDao.findById(id);
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        return usuarioDao.save(usuario);
    }

    
    @Override
    @Transactional
    public void deleteById(Long id) {
        usuarioDao.deleteById(id);
    }
}
