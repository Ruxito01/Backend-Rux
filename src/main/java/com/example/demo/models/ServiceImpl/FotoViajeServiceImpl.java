package com.example.demo.models.ServiceImpl;

import com.example.demo.models.dao.IFotoViajeDao;
import com.example.demo.models.entity.FotoViaje;
import com.example.demo.models.service.IFotoViajeService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FotoViajeServiceImpl implements IFotoViajeService {

    @Autowired
    private IFotoViajeDao dao;

    @Override
    @Transactional(readOnly = true)
    public List<FotoViaje> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public FotoViaje findById(@NonNull Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FotoViaje> findByViajeId(@NonNull Long viajeId) {
        return dao.findByViajeId(viajeId);
    }

    @Override
    @Transactional
    public FotoViaje save(@NonNull FotoViaje entity) {
        return dao.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(@NonNull Long id) {
        dao.deleteById(id);
    }
}
