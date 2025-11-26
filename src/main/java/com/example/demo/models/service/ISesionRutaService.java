package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.SesionRuta;

public interface ISesionRutaService {
    public List<SesionRuta> findAll();

    public SesionRuta findById(Long id);

    public SesionRuta save(SesionRuta sesionRuta);

    public void delete(Long id);
}
