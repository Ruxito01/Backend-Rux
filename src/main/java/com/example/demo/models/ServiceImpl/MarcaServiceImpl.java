package com.example.demo.models.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IMarcaDao;
import com.example.demo.models.entity.Marca;
import com.example.demo.models.service.IMarcaService;

/**
 * Implementación del servicio para Marca
 */
@Service
public class MarcaServiceImpl implements IMarcaService {

    @Autowired
    private IMarcaDao marcaDao;

    @Override
    @Transactional(readOnly = true)
    public List<Marca> findAll() {
        return marcaDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Marca findById(Long id) {
        return marcaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Marca findByNombre(String nombre) {
        return marcaDao.findByNombreIgnoreCase(nombre);
    }

    @Override
    @Transactional
    public Marca save(Marca marca) {
        return marcaDao.save(marca);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        marcaDao.deleteById(id);
    }
}
