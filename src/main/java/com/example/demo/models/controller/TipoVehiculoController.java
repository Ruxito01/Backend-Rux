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

import com.example.demo.models.entity.TipoVehiculo;
import com.example.demo.models.service.ITipoVehiculoService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class TipoVehiculoController {

    @Autowired
    private ITipoVehiculoService tipoVehiculoService;

    @GetMapping("/tipo-vehiculos")
    public List<TipoVehiculo> index() {
        return tipoVehiculoService.findAll();
    }

    @GetMapping("/tipo-vehiculos/{id}")
    public TipoVehiculo show(@PathVariable Long id) {
        return tipoVehiculoService.findById(id);
    }

    @PostMapping("/tipo-vehiculos")
    @ResponseStatus(HttpStatus.CREATED)
    public TipoVehiculo create(@RequestBody TipoVehiculo tipoVehiculo) {
        return tipoVehiculoService.save(tipoVehiculo);
    }

    @PutMapping("/tipo-vehiculos/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public TipoVehiculo update(@RequestBody TipoVehiculo tipoVehiculo, @PathVariable Long id) {
        TipoVehiculo tipoVehiculoActual = tipoVehiculoService.findById(id);

        tipoVehiculoActual.setNombre(tipoVehiculo.getNombre());
        tipoVehiculoActual.setIconoUrl(tipoVehiculo.getIconoUrl());

        return tipoVehiculoService.save(tipoVehiculoActual);
    }

    @DeleteMapping("/tipo-vehiculos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tipoVehiculoService.delete(id);
    }
}
