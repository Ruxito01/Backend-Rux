package com.example.demo.models.ServiceImpl;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IComunidadDao;
import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.service.IComunidadService;

@Service
public class ComunidadServiceImpl implements IComunidadService {

    @Autowired
    private IComunidadDao comunidadDao;

    @Override
    @Transactional(readOnly = true)
    public List<Comunidad> findAll() {
        return comunidadDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Comunidad findById(Long id) {
        return comunidadDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Comunidad save(Comunidad comunidad) {
        return comunidadDao.save(comunidad);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        comunidadDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Usuario> getMiembrosByComunidadId(Long comunidadId) {
        // Usar la query con JOIN FETCH para cargar los miembros de forma eager
        return comunidadDao.findByIdWithMiembros(comunidadId)
                .map(Comunidad::getMiembros)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comunidad> findByMiembroId(Long usuarioId) {
        return comunidadDao.findByMiembroId(usuarioId);
    }
}
