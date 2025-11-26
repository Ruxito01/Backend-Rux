package com.example.demo.models.service;

import java.util.List;
import com.example.demo.models.entity.UsuarioLogro;

public interface IUsuarioLogroService {
    public List<UsuarioLogro> findAll();

    public UsuarioLogro findById(Long id);

    public UsuarioLogro save(UsuarioLogro usuarioLogro);

    public void delete(Long id);
}
