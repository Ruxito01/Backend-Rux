package com.example.demo.models.dao;

import com.example.demo.models.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuarioDao extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su email
     * 
     * @param email Email del usuario a buscar
     * @return Optional conteniendo el usuario si existe
     */
    Optional<Usuario> findByEmail(String email);
}
