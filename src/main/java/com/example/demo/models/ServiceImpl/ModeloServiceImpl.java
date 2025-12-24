package com.example.demo.models.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IModeloDao;
import com.example.demo.models.entity.Modelo;
import com.example.demo.models.service.IModeloService;

/**
 * Implementación del servicio para Modelo
 */
@Service
public class ModeloServiceImpl implements IModeloService {

    @Autowired
    private IModeloDao modeloDao;

    @Override
    @Transactional(readOnly = true)
    public List<Modelo> findAll() {
        return modeloDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Modelo findById(Long id) {
        return modeloDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Modelo> findByMarcaId(Long marcaId) {
        return modeloDao.findByMarca_Id(marcaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Modelo> buscarPorNombre(String nombre) {
        return modeloDao.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    @Transactional
    public Modelo save(Modelo modelo) {
        return modeloDao.save(modelo);
    }

    @Override
    @Transactional(readOnly = true)
    public Modelo findByNombreAndMarcaId(String nombre, Long marcaId) {
        return modeloDao.findByNombreAndMarca_Id(nombre, marcaId);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        modeloDao.deleteById(id);
    }
}
