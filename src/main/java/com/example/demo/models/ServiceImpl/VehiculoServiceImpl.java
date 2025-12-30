package com.example.demo.models.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IVehiculoDao;
import com.example.demo.models.entity.Vehiculo;
import com.example.demo.models.service.IVehiculoService;

@Service
public class VehiculoServiceImpl implements IVehiculoService {

    @Autowired
    private IVehiculoDao vehiculoDao;

    @Override
    @Transactional(readOnly = true)
    public List<Vehiculo> findAll() {
        return (List<Vehiculo>) vehiculoDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Vehiculo findById(Long id) {
        return vehiculoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Vehiculo save(Vehiculo vehiculo) {
        return vehiculoDao.save(vehiculo);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        vehiculoDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehiculo> findByUsuarioId(Long usuarioId) {
        return vehiculoDao.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<java.util.Map<String, Object>> getConteoPorTipo() {
        List<Object[]> resultados = vehiculoDao.countVehiculosGroupByTipo();

        // Transformar List<Object[]> a List<Map<String, Object>> para JSON
        List<java.util.Map<String, Object>> response = new java.util.ArrayList<>();

        for (Object[] fila : resultados) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("tipo", fila[0]); // Nombre del tipo
            map.put("cantidad", fila[1]); // Conteo
            response.add(map);
        }

        return response;
    }
}
