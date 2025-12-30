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

    /**
     * Obtiene el conteo de vehiculos agrupados por nombre de tipo.
     * Retorna lista de arrays Object[] donde [0]=nombreTipo, [1]=cantidad.
     */
    @org.springframework.data.jpa.repository.Query("SELECT v.tipoVehiculo.nombre, COUNT(v) FROM Vehiculo v GROUP BY v.tipoVehiculo.nombre")
    List<Object[]> countVehiculosGroupByTipo();
}
