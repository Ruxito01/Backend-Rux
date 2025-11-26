package com.example.demo.models.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.entity.Vehiculo;
import com.example.demo.models.service.IVehiculoService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class VehiculoController {

    @Autowired
    private IVehiculoService vehiculoService;

    @GetMapping("/vehiculos")
    public List<Vehiculo> index() {
        return vehiculoService.findAll();
    }

    @GetMapping("/vehiculos/{id}")
    public Vehiculo show(@PathVariable Long id) {
        return vehiculoService.findById(id);
    }

    @PostMapping("/vehiculos")
    @ResponseStatus(HttpStatus.CREATED)
    public Vehiculo create(@RequestBody Vehiculo vehiculo) {
        return vehiculoService.save(vehiculo);
    }

    @PutMapping("/vehiculos/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Vehiculo update(@RequestBody Vehiculo vehiculo, @PathVariable Long id) {
        Vehiculo vehiculoActual = vehiculoService.findById(id);

        vehiculoActual.setUsuario(vehiculo.getUsuario());
        vehiculoActual.setTipoVehiculo(vehiculo.getTipoVehiculo());
        vehiculoActual.setAlias(vehiculo.getAlias());
        vehiculoActual.setMarca(vehiculo.getMarca());
        vehiculoActual.setModelo(vehiculo.getModelo());
        vehiculoActual.setFotoVehiculo(vehiculo.getFotoVehiculo());

        return vehiculoService.save(vehiculoActual);
    }

    @DeleteMapping("/vehiculos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vehiculoService.delete(id);
    }
}
