package com.example.demo.models.dao;

import com.example.demo.models.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioDao extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);
}
