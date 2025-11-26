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

import com.example.demo.models.entity.SesionRuta;
import com.example.demo.models.service.ISesionRutaService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class SesionRutaController {

    @Autowired
    private ISesionRutaService sesionRutaService;

    @GetMapping("/sesion-rutas")
    public List<SesionRuta> index() {
        return sesionRutaService.findAll();
    }

    @GetMapping("/sesion-rutas/{id}")
    public SesionRuta show(@PathVariable Long id) {
        return sesionRutaService.findById(id);
    }

    @PostMapping("/sesion-rutas")
    @ResponseStatus(HttpStatus.CREATED)
    public SesionRuta create(@RequestBody SesionRuta sesionRuta) {
        return sesionRutaService.save(sesionRuta);
    }

    @PutMapping("/sesion-rutas/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public SesionRuta update(@RequestBody SesionRuta sesionRuta, @PathVariable Long id) {
        SesionRuta sesionRutaActual = sesionRutaService.findById(id);

        sesionRutaActual.setUsuario(sesionRuta.getUsuario());
        sesionRutaActual.setRuta(sesionRuta.getRuta());
        sesionRutaActual.setVehiculo(sesionRuta.getVehiculo());
        sesionRutaActual.setEstado(sesionRuta.getEstado());
        sesionRutaActual.setCheckInInicio(sesionRuta.getCheckInInicio());
        sesionRutaActual.setCheckInFin(sesionRuta.getCheckInFin());
        sesionRutaActual.setPorcentajeCompletado(sesionRuta.getPorcentajeCompletado());

        return sesionRutaService.save(sesionRutaActual);
    }

    @DeleteMapping("/sesion-rutas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        sesionRutaService.delete(id);
    }
}
