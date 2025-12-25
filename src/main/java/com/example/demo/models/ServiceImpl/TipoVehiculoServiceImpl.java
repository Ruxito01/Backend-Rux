package com.example.demo.models.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.ITipoVehiculoDao;
import com.example.demo.models.dao.IVehiculoDao;
import com.example.demo.models.entity.TipoVehiculo;
import com.example.demo.models.service.ITipoVehiculoService;

@Service
public class TipoVehiculoServiceImpl implements ITipoVehiculoService {

    @Autowired
    private ITipoVehiculoDao tipoVehiculoDao;

    @Autowired
    private IVehiculoDao vehiculoDao;

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
    @Transactional(readOnly = true)
    public TipoVehiculo findByNombre(String nombre) {
        return tipoVehiculoDao.findByNombre(nombre);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        tipoVehiculoDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countVehiculosByTipoId(Long tipoId) {
        return vehiculoDao.countByTipoVehiculo_Id(tipoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoVehiculo> findByUsuarioId(Long usuarioId) {
        return tipoVehiculoDao.findDistinctByVehiculos_Usuario_Id(usuarioId);
    }
}
