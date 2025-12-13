package com.example.demo.models.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.hibernate.Hibernate;
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
        List<Comunidad> comunidades = (List<Comunidad>) comunidadDao.findAll();
        for (Comunidad c : comunidades) {
            Hibernate.initialize(c.getMiembros());
            Hibernate.initialize(c.getCreador());
        }
        return comunidades;
    }

    @Override
    @Transactional(readOnly = true)
    public Comunidad findById(Long id) {
        Comunidad c = comunidadDao.findById(id).orElse(null);
        if (c != null) {
            Hibernate.initialize(c.getMiembros());
            Hibernate.initialize(c.getCreador());
        }
        return c;
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
