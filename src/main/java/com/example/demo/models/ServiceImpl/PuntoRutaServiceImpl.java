package com.example.demo.models.ServiceImpl;

import com.example.demo.models.dao.IPuntoRutaDao;
import com.example.demo.models.entity.PuntoRuta;
import com.example.demo.models.service.IPuntoRutaService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PuntoRutaServiceImpl implements IPuntoRutaService {

    @Autowired
    private IPuntoRutaDao dao;

    @Override
    @Transactional(readOnly = true)
    public List<PuntoRuta> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PuntoRuta findById(@NonNull Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public PuntoRuta save(@NonNull PuntoRuta entity) {
        return dao.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(@NonNull Long id) {
        dao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PuntoRuta> findByRutaId(@NonNull Long rutaId) {
        return dao.findByRuta_IdOrderByOrdenSecuenciaAsc(rutaId);
    }
}
