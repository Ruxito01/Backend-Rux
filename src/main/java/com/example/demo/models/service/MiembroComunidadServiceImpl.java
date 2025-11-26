package com.example.demo.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IMiembroComunidadDao;
import com.example.demo.models.entity.MiembroComunidad;

@Service
public class MiembroComunidadServiceImpl implements IMiembroComunidadService {

    @Autowired
    private IMiembroComunidadDao miembroComunidadDao;

    @Override
    @Transactional(readOnly = true)
    public List<MiembroComunidad> findAll() {
        return (List<MiembroComunidad>) miembroComunidadDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public MiembroComunidad findById(Long id) {
        return miembroComunidadDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public MiembroComunidad save(MiembroComunidad miembroComunidad) {
        return miembroComunidadDao.save(miembroComunidad);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        miembroComunidadDao.deleteById(id);
    }
}
