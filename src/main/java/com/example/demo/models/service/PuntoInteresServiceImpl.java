package com.example.demo.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IPuntoInteresDao;
import com.example.demo.models.entity.PuntoInteres;

@Service
public class PuntoInteresServiceImpl implements IPuntoInteresService {

    @Autowired
    private IPuntoInteresDao puntoInteresDao;

    @Override
    @Transactional(readOnly = true)
    public List<PuntoInteres> findAll() {
        return (List<PuntoInteres>) puntoInteresDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PuntoInteres findById(Long id) {
        return puntoInteresDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public PuntoInteres save(PuntoInteres puntoInteres) {
        return puntoInteresDao.save(puntoInteres);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        puntoInteresDao.deleteById(id);
    }
}
