package com.example.demo.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.ITipoParadaDao;
import com.example.demo.models.entity.TipoParada;

@Service
public class TipoParadaServiceImpl implements ITipoParadaService {

    @Autowired
    private ITipoParadaDao tipoParadaDao;

    @Override
    @Transactional(readOnly = true)
    public List<TipoParada> findAll() {
        return (List<TipoParada>) tipoParadaDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoParada findById(Long id) {
        return tipoParadaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public TipoParada save(TipoParada tipoParada) {
        return tipoParadaDao.save(tipoParada);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        tipoParadaDao.deleteById(id);
    }
}
