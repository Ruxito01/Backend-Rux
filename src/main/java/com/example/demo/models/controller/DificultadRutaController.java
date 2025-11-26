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

import com.example.demo.models.entity.DificultadRuta;
import com.example.demo.models.service.IDificultadRutaService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class DificultadRutaController {

    @Autowired
    private IDificultadRutaService dificultadRutaService;

    @GetMapping("/dificultad-rutas")
    public List<DificultadRuta> index() {
        return dificultadRutaService.findAll();
    }

    @GetMapping("/dificultad-rutas/{id}")
    public DificultadRuta show(@PathVariable Long id) {
        return dificultadRutaService.findById(id);
    }

    @PostMapping("/dificultad-rutas")
    @ResponseStatus(HttpStatus.CREATED)
    public DificultadRuta create(@RequestBody DificultadRuta dificultadRuta) {
        return dificultadRutaService.save(dificultadRuta);
    }

    @PutMapping("/dificultad-rutas/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public DificultadRuta update(@RequestBody DificultadRuta dificultadRuta, @PathVariable Long id) {
        DificultadRuta dificultadRutaActual = dificultadRutaService.findById(id);

        dificultadRutaActual.setNivel(dificultadRuta.getNivel());
        dificultadRutaActual.setColor(dificultadRuta.getColor());

        return dificultadRutaService.save(dificultadRutaActual);
    }

    @DeleteMapping("/dificultad-rutas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        dificultadRutaService.delete(id);
    }
}
