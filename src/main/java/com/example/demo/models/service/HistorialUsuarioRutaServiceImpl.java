package com.example.demo.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IHistorialUsuarioRutaDao;
import com.example.demo.models.entity.HistorialUsuarioRuta;

@Service
public class HistorialUsuarioRutaServiceImpl implements IHistorialUsuarioRutaService {

    @Autowired
    private IHistorialUsuarioRutaDao historialUsuarioRutaDao;

    @Override
    @Transactional(readOnly = true)
    public List<HistorialUsuarioRuta> findAll() {
        return (List<HistorialUsuarioRuta>) historialUsuarioRutaDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public HistorialUsuarioRuta findById(Long id) {
        return historialUsuarioRutaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public HistorialUsuarioRuta save(HistorialUsuarioRuta historialUsuarioRuta) {
        return historialUsuarioRutaDao.save(historialUsuarioRuta);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        historialUsuarioRutaDao.deleteById(id);
    }
}
