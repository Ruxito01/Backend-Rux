package com.example.demo.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IRutaPuntoDao;
import com.example.demo.models.entity.RutaPunto;

@Service
public class RutaPuntoServiceImpl implements IRutaPuntoService {

    @Autowired
    private IRutaPuntoDao rutaPuntoDao;

    @Override
    @Transactional(readOnly = true)
    public List<RutaPunto> findAll() {
        return (List<RutaPunto>) rutaPuntoDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public RutaPunto findById(Long id) {
        return rutaPuntoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public RutaPunto save(RutaPunto rutaPunto) {
        return rutaPuntoDao.save(rutaPunto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        rutaPuntoDao.deleteById(id);
    }
}
