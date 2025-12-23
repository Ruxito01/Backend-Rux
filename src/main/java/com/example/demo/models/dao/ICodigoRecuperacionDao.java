package com.example.demo.models.dao;

import com.example.demo.models.entity.CodigoRecuperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * DAO para la entidad CodigoRecuperacion
 */
@Repository
public interface ICodigoRecuperacionDao extends JpaRepository<CodigoRecuperacion, Long> {

    /**
     * Busca un código válido (no usado) por email y código
     */
    Optional<CodigoRecuperacion> findByEmailAndCodigoAndUsadoFalse(String email, String codigo);

    /**
     * Busca el código más reciente por email que no ha sido usado
     */
    Optional<CodigoRecuperacion> findTopByEmailAndUsadoFalseOrderByFechaCreacionDesc(String email);

    /**
     * Elimina todos los códigos de un email (para limpiar antes de generar uno
     * nuevo)
     */
    @Transactional
    void deleteByEmail(String email);
}
