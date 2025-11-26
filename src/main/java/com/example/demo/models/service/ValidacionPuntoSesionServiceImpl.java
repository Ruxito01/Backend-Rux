package com.example.demo.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IValidacionPuntoSesionDao;
import com.example.demo.models.entity.ValidacionPuntoSesion;

@Service
public class ValidacionPuntoSesionServiceImpl implements IValidacionPuntoSesionService {

    @Autowired
    private IValidacionPuntoSesionDao validacionPuntoSesionDao;

    @Override
    @Transactional(readOnly = true)
    public List<ValidacionPuntoSesion> findAll() {
        return (List<ValidacionPuntoSesion>) validacionPuntoSesionDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ValidacionPuntoSesion findById(Long id) {
        return validacionPuntoSesionDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public ValidacionPuntoSesion save(ValidacionPuntoSesion validacionPuntoSesion) {
        return validacionPuntoSesionDao.save(validacionPuntoSesion);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        validacionPuntoSesionDao.deleteById(id);
    }
}
