package com.example.demo.models.service;

import com.example.demo.models.entity.Vehiculo;
import java.util.List;

public interface IVehiculoService {
    List<Vehiculo> findAll();

    Vehiculo findById(Long id);

    Vehiculo save(Vehiculo entity);

    void deleteById(Long id);

    List<Vehiculo> findByUsuarioId(Long usuarioId);

    /**
     * Obtiene el conteo de vehiculos agrupados por tipo.
     * 
     * @return Lista de mapas con {tipo: "Nombre", cantidad: 123}
     */
    List<java.util.Map<String, Object>> getConteoPorTipo();
}
