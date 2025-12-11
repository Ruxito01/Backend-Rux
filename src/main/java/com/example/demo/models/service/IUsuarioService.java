package com.example.demo.models.service;

import com.example.demo.models.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<Usuario> findAll();

    Optional<Usuario> findById(Long id);

    Usuario save(Usuario usuario);

    void deleteById(Long id);

    boolean existsByEmail(String email);

    Optional<Usuario> updatePartial(Long id, Usuario usuarioParcial);

}
