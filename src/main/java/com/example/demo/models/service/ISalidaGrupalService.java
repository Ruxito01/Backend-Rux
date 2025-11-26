package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.SalidaGrupal;

public interface ISalidaGrupalService {
    public List<SalidaGrupal> findAll();

    public SalidaGrupal findById(Long id);

    public SalidaGrupal save(SalidaGrupal salidaGrupal);

    public void delete(Long id);
}
