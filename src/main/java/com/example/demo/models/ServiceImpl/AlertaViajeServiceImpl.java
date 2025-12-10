package com.example.demo.models.ServiceImpl;

import com.example.demo.models.dao.IAlertaViajeDao;
import com.example.demo.models.entity.AlertaViaje;
import com.example.demo.models.service.IAlertaViajeService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlertaViajeServiceImpl implements IAlertaViajeService {
    
    @Autowired
    private IAlertaViajeDao dao;
    
    @Override
    @Transactional(readOnly = true)
    public List<AlertaViaje> findAll() {
        return dao.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public AlertaViaje findById(@NonNull Long id) {
        return dao.findById(id).orElse(null);
    }
    
    @Override
    @Transactional
    public AlertaViaje save(@NonNull AlertaViaje entity) {
        return dao.save(entity);
    }
    
    @Override
    @Transactional
    public void deleteById(@NonNull Long id) {
        dao.deleteById(id);
    }
}
