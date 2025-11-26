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

import com.example.demo.models.entity.PuntoInteres;
import com.example.demo.models.service.IPuntoInteresService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class PuntoInteresController {

    @Autowired
    private IPuntoInteresService puntoInteresService;

    @GetMapping("/punto-intereses")
    public List<PuntoInteres> index() {
        return puntoInteresService.findAll();
    }

    @GetMapping("/punto-intereses/{id}")
    public PuntoInteres show(@PathVariable Long id) {
        return puntoInteresService.findById(id);
    }

    @PostMapping("/punto-intereses")
    @ResponseStatus(HttpStatus.CREATED)
    public PuntoInteres create(@RequestBody PuntoInteres puntoInteres) {
        return puntoInteresService.save(puntoInteres);
    }

    @PutMapping("/punto-intereses/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public PuntoInteres update(@RequestBody PuntoInteres puntoInteres, @PathVariable Long id) {
        PuntoInteres puntoInteresActual = puntoInteresService.findById(id);

        puntoInteresActual.setTipoParada(puntoInteres.getTipoParada());
        puntoInteresActual.setLat(puntoInteres.getLat());
        puntoInteresActual.setLng(puntoInteres.getLng());

        return puntoInteresService.save(puntoInteresActual);
    }

    @DeleteMapping("/punto-intereses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        puntoInteresService.delete(id);
    }
}
