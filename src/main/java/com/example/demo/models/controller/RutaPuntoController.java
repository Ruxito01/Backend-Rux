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

import com.example.demo.models.entity.RutaPunto;
import com.example.demo.models.service.IRutaPuntoService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class RutaPuntoController {

    @Autowired
    private IRutaPuntoService rutaPuntoService;

    @GetMapping("/ruta-puntos")
    public List<RutaPunto> index() {
        return rutaPuntoService.findAll();
    }

    @GetMapping("/ruta-puntos/{id}")
    public RutaPunto show(@PathVariable Long id) {
        return rutaPuntoService.findById(id);
    }

    @PostMapping("/ruta-puntos")
    @ResponseStatus(HttpStatus.CREATED)
    public RutaPunto create(@RequestBody RutaPunto rutaPunto) {
        return rutaPuntoService.save(rutaPunto);
    }

    @PutMapping("/ruta-puntos/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public RutaPunto update(@RequestBody RutaPunto rutaPunto, @PathVariable Long id) {
        RutaPunto rutaPuntoActual = rutaPuntoService.findById(id);

        rutaPuntoActual.setRuta(rutaPunto.getRuta());
        rutaPuntoActual.setPuntoInteres(rutaPunto.getPuntoInteres());
        rutaPuntoActual.setOrden(rutaPunto.getOrden());

        return rutaPuntoService.save(rutaPuntoActual);
    }

    @DeleteMapping("/ruta-puntos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rutaPuntoService.delete(id);
    }
}
