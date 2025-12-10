package com.example.demo.models.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IRutaDao;
import com.example.demo.models.entity.Ruta;
import com.example.demo.models.service.IRutaService;

@Service
public class RutaServiceImpl implements IRutaService {

    @Autowired
    private IRutaDao rutaDao;

    @Override
    @Transactional(readOnly = true)
    public List<Ruta> findAll() {
        return (List<Ruta>) rutaDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Ruta findById(Long id) {
        return rutaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Ruta save(Ruta ruta) {
        return rutaDao.save(ruta);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        rutaDao.deleteById(id);
    }
}
