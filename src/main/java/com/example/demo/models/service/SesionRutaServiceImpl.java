package com.example.demo.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.ISesionRutaDao;
import com.example.demo.models.entity.SesionRuta;

@Service
public class SesionRutaServiceImpl implements ISesionRutaService {

    @Autowired
    private ISesionRutaDao sesionRutaDao;

    @Override
    @Transactional(readOnly = true)
    public List<SesionRuta> findAll() {
        return (List<SesionRuta>) sesionRutaDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public SesionRuta findById(Long id) {
        return sesionRutaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public SesionRuta save(SesionRuta sesionRuta) {
        return sesionRutaDao.save(sesionRuta);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        sesionRutaDao.deleteById(id);
    }
}
