package com.example.demo.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IGaleriaRutaDao;
import com.example.demo.models.entity.GaleriaRuta;

@Service
public class GaleriaRutaServiceImpl implements IGaleriaRutaService {

    @Autowired
    private IGaleriaRutaDao galeriaRutaDao;

    @Override
    @Transactional(readOnly = true)
    public List<GaleriaRuta> findAll() {
        return (List<GaleriaRuta>) galeriaRutaDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public GaleriaRuta findById(Long id) {
        return galeriaRutaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public GaleriaRuta save(GaleriaRuta galeriaRuta) {
        return galeriaRutaDao.save(galeriaRuta);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        galeriaRutaDao.deleteById(id);
    }
}
