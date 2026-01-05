package com.example.demo.models.service;

import com.example.demo.models.entity.Logro;
import java.util.List;

public interface ILogroService {
    List<Logro> findAll();

    Logro findById(Long id);

    Logro save(Logro entity);

    void deleteById(Long id);

    List<Logro> findByUsuarioId(Long usuarioId); // Obtener logros de un usuario
}
