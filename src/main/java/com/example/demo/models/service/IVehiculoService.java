package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.Vehiculo;

public interface IVehiculoService {
    public List<Vehiculo> findAll();

    public Vehiculo findById(Long id);

    public Vehiculo save(Vehiculo vehiculo);

    public void delete(Long id);
}
