package com.example.demo.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.ISalidaGrupalDao;
import com.example.demo.models.entity.SalidaGrupal;

@Service
public class SalidaGrupalServiceImpl implements ISalidaGrupalService {

    @Autowired
    private ISalidaGrupalDao salidaGrupalDao;

    @Override
    @Transactional(readOnly = true)
    public List<SalidaGrupal> findAll() {
        return (List<SalidaGrupal>) salidaGrupalDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public SalidaGrupal findById(Long id) {
        return salidaGrupalDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public SalidaGrupal save(SalidaGrupal salidaGrupal) {
        return salidaGrupalDao.save(salidaGrupal);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        salidaGrupalDao.deleteById(id);
    }
}
