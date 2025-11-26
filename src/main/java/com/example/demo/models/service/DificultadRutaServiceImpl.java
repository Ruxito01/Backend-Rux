package com.example.demo.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IDificultadRutaDao;
import com.example.demo.models.entity.DificultadRuta;

@Service
public class DificultadRutaServiceImpl implements IDificultadRutaService {

    @Autowired
    private IDificultadRutaDao dificultadRutaDao;

    @Override
    @Transactional(readOnly = true)
    public List<DificultadRuta> findAll() {
        return (List<DificultadRuta>) dificultadRutaDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public DificultadRuta findById(Long id) {
        return dificultadRutaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public DificultadRuta save(DificultadRuta dificultadRuta) {
        return dificultadRutaDao.save(dificultadRuta);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        dificultadRutaDao.deleteById(id);
    }
}
