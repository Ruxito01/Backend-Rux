package com.example.demo.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.ITipoVehiculoDao;
import com.example.demo.models.entity.TipoVehiculo;

@Service
public class TipoVehiculoServiceImpl implements ITipoVehiculoService {

    @Autowired
    private ITipoVehiculoDao tipoVehiculoDao;

    @Override
    @Transactional(readOnly = true)
    public List<TipoVehiculo> findAll() {
        return (List<TipoVehiculo>) tipoVehiculoDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoVehiculo findById(Long id) {
        return tipoVehiculoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public TipoVehiculo save(TipoVehiculo tipoVehiculo) {
        return tipoVehiculoDao.save(tipoVehiculo);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        tipoVehiculoDao.deleteById(id);
    }
}
