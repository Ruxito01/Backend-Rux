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

import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.service.IComunidadService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ComunidadController {

    @Autowired
    private IComunidadService comunidadService;

    @GetMapping("/comunidades")
    public List<Comunidad> index() {
        return comunidadService.findAll();
    }

    @GetMapping("/comunidades/{id}")
    public Comunidad show(@PathVariable Long id) {
        return comunidadService.findById(id);
    }

    @PostMapping("/comunidades")
    @ResponseStatus(HttpStatus.CREATED)
    public Comunidad create(@RequestBody Comunidad comunidad) {
        return comunidadService.save(comunidad);
    }

    @PutMapping("/comunidades/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Comunidad update(@RequestBody Comunidad comunidad, @PathVariable Long id) {
        Comunidad comunidadActual = comunidadService.findById(id);

        comunidadActual.setNombre(comunidad.getNombre());
        comunidadActual.setLema(comunidad.getLema());
        comunidadActual.setDescripcion(comunidad.getDescripcion());
        comunidadActual.setCiudad(comunidad.getCiudad());
        comunidadActual.setLogoUrl(comunidad.getLogoUrl());
        comunidadActual.setFotoPortadaUrl(comunidad.getFotoPortadaUrl());
        comunidadActual.setTipoPrivacidad(comunidad.getTipoPrivacidad());
        comunidadActual.setCreador(comunidad.getCreador());

        return comunidadService.save(comunidadActual);
    }

    @DeleteMapping("/comunidades/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        comunidadService.delete(id);
    }
}
