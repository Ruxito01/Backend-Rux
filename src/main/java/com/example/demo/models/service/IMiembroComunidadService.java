package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.MiembroComunidad;

public interface IMiembroComunidadService {
    public List<MiembroComunidad> findAll();

    public MiembroComunidad findById(Long id);

    public MiembroComunidad save(MiembroComunidad miembroComunidad);

    public void delete(Long id);
}
