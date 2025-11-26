package com.example.demo.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IUsuarioLogroDao;
import com.example.demo.models.entity.UsuarioLogro;

@Service
public class UsuarioLogroServiceImpl implements IUsuarioLogroService {

    @Autowired
    private IUsuarioLogroDao usuarioLogroDao;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioLogro> findAll() {
        return (List<UsuarioLogro>) usuarioLogroDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioLogro findById(Long id) {
        return usuarioLogroDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public UsuarioLogro save(UsuarioLogro usuarioLogro) {
        return usuarioLogroDao.save(usuarioLogro);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        usuarioLogroDao.deleteById(id);
    }
}
