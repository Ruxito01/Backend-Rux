package com.example.demo.models.dao;

import com.example.demo.models.entity.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITipoVehiculoDao extends JpaRepository<TipoVehiculo, Long> {
    TipoVehiculo findByNombre(String nombre);

    /**
     * Obtiene los tipos de vehículo distintos de los vehículos de un usuario
     */
    @Query("SELECT DISTINCT v.tipoVehiculo FROM Vehiculo v WHERE v.usuario.id = :usuarioId AND v.tipoVehiculo IS NOT NULL")
    List<TipoVehiculo> findDistinctByVehiculos_Usuario_Id(@Param("usuarioId") Long usuarioId);
}
