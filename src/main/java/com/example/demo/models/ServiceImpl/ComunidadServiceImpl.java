package com.example.demo.models.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IComunidadDao;
import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.service.IComunidadService;

@Service
public class ComunidadServiceImpl implements IComunidadService {

    @Autowired
    private IComunidadDao comunidadDao;

    @Override
    @Transactional(readOnly = true)
    public List<Comunidad> findAll() {
        return comunidadDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Comunidad findById(Long id) {
        return comunidadDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Comunidad save(Comunidad comunidad) {
        return comunidadDao.save(comunidad);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        comunidadDao.deleteById(id);
    }
}
