package com.example.demo.models.ServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IRutaDao;
import com.example.demo.models.dao.ITipoVehiculoDao;
import com.example.demo.models.entity.Ruta;
import com.example.demo.models.entity.TipoVehiculo;
import com.example.demo.models.service.IRutaService;

@Service
public class RutaServiceImpl implements IRutaService {

    @Autowired
    private IRutaDao rutaDao;

    @Autowired
    private ITipoVehiculoDao tipoVehiculoDao;

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
        // Resolver referencias de TipoVehiculo para que JPA pueda guardar la relación
        // ManyToMany
        if (ruta.getTiposVehiculo() != null && !ruta.getTiposVehiculo().isEmpty()) {
            Set<TipoVehiculo> tiposResueltos = new HashSet<>();
            for (TipoVehiculo tipo : ruta.getTiposVehiculo()) {
                if (tipo.getId() != null) {
                    TipoVehiculo tipoExistente = tipoVehiculoDao.findById(tipo.getId()).orElse(null);
                    if (tipoExistente != null) {
                        tiposResueltos.add(tipoExistente);
                    }
                }
            }
            ruta.setTiposVehiculo(tiposResueltos);
        }
        return rutaDao.save(ruta);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        rutaDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ruta> findByComunidadId(Long comunidadId) {
        return rutaDao.findByComunidad_Id(comunidadId);
    }
}
