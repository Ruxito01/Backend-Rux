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

import com.example.demo.models.entity.GaleriaRuta;
import com.example.demo.models.service.IGaleriaRutaService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class GaleriaRutaController {

    @Autowired
    private IGaleriaRutaService galeriaRutaService;

    @GetMapping("/galeria-rutas")
    public List<GaleriaRuta> index() {
        return galeriaRutaService.findAll();
    }

    @GetMapping("/galeria-rutas/{id}")
    public GaleriaRuta show(@PathVariable Long id) {
        return galeriaRutaService.findById(id);
    }

    @PostMapping("/galeria-rutas")
    @ResponseStatus(HttpStatus.CREATED)
    public GaleriaRuta create(@RequestBody GaleriaRuta galeriaRuta) {
        return galeriaRutaService.save(galeriaRuta);
    }

    @PutMapping("/galeria-rutas/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public GaleriaRuta update(@RequestBody GaleriaRuta galeriaRuta, @PathVariable Long id) {
        GaleriaRuta galeriaRutaActual = galeriaRutaService.findById(id);

        galeriaRutaActual.setRuta(galeriaRuta.getRuta());
        galeriaRutaActual.setUsuario(galeriaRuta.getUsuario());
        galeriaRutaActual.setUrlFoto(galeriaRuta.getUrlFoto());

        return galeriaRutaService.save(galeriaRutaActual);
    }

    @DeleteMapping("/galeria-rutas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        galeriaRutaService.delete(id);
    }
}
