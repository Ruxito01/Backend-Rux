package com.example.demo.models.ServiceImpl;

import com.example.demo.models.dao.ICatalogoAvatarDao;
import com.example.demo.models.entity.CatalogoAvatar;
import com.example.demo.models.service.ICatalogoAvatarService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogoAvatarServiceImpl implements ICatalogoAvatarService {

    @Autowired
    private ICatalogoAvatarDao dao;

    @Override
    @Transactional(readOnly = true)
    public List<CatalogoAvatar> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogoAvatar findById(@NonNull Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public CatalogoAvatar save(@NonNull CatalogoAvatar entity) {
        return dao.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(@NonNull Long id) {
        dao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogoAvatar> findByUsuarioId(Long usuarioId) {
        return dao.findByUsuarioId(usuarioId);
    }
}
