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

import com.example.demo.models.entity.Ruta;
import com.example.demo.models.service.IRutaService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class RutaController {

    @Autowired
    private IRutaService rutaService;

    @GetMapping("/rutas")
    public List<Ruta> index() {
        return rutaService.findAll();
    }

    @GetMapping("/rutas/{id}")
    public Ruta show(@PathVariable Long id) {
        return rutaService.findById(id);
    }

    @PostMapping("/rutas")
    @ResponseStatus(HttpStatus.CREATED)
    public Ruta create(@RequestBody Ruta ruta) {
        return rutaService.save(ruta);
    }

    @PutMapping("/rutas/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Ruta update(@RequestBody Ruta ruta, @PathVariable Long id) {
        Ruta rutaActual = rutaService.findById(id);

        rutaActual.setAutor(ruta.getAutor());
        rutaActual.setComunidad(ruta.getComunidad());
        rutaActual.setNombre(ruta.getNombre());
        rutaActual.setDescripcion(ruta.getDescripcion());
        rutaActual.setDistanciaKm(ruta.getDistanciaKm());
        rutaActual.setTiempoEstimadoMinutos(ruta.getTiempoEstimadoMinutos());
        rutaActual.setDificultadId(ruta.getDificultadId());
        rutaActual.setEsPublica(ruta.getEsPublica());
        rutaActual.setGeometriaRuta(ruta.getGeometriaRuta());
        rutaActual.setLatInicio(ruta.getLatInicio());
        rutaActual.setLngInicio(ruta.getLngInicio());
        rutaActual.setLatFin(ruta.getLatFin());
        rutaActual.setLngFin(ruta.getLngFin());

        return rutaService.save(rutaActual);
    }

    @DeleteMapping("/rutas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rutaService.delete(id);
    }
}
