package com.example.demo.models.dao;

import com.example.demo.models.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IVehiculoDao extends JpaRepository<Vehiculo, Long> {
    List<Vehiculo> findByUsuarioId(Long usuarioId);

    /**
     * Cuenta vehiculos por modelo (para validar eliminacion de modelos)
     */
    long countByModeloEntidad_Id(Long modeloId);

    /**
     * Cuenta vehiculos por tipo de vehiculo (para validar eliminacion de tipos)
     */
    long countByTipoVehiculo_Id(Long tipoVehiculoId);
}
