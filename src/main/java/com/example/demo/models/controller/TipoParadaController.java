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

import com.example.demo.models.entity.TipoParada;
import com.example.demo.models.service.ITipoParadaService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class TipoParadaController {

    @Autowired
    private ITipoParadaService tipoParadaService;

    @GetMapping("/tipo-paradas")
    public List<TipoParada> index() {
        return tipoParadaService.findAll();
    }

    @GetMapping("/tipo-paradas/{id}")
    public TipoParada show(@PathVariable Long id) {
        return tipoParadaService.findById(id);
    }

    @PostMapping("/tipo-paradas")
    @ResponseStatus(HttpStatus.CREATED)
    public TipoParada create(@RequestBody TipoParada tipoParada) {
        return tipoParadaService.save(tipoParada);
    }

    @PutMapping("/tipo-paradas/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public TipoParada update(@RequestBody TipoParada tipoParada, @PathVariable Long id) {
        TipoParada tipoParadaActual = tipoParadaService.findById(id);

        tipoParadaActual.setNombre(tipoParada.getNombre());
        tipoParadaActual.setIcono(tipoParada.getIcono());

        return tipoParadaService.save(tipoParadaActual);
    }

    @DeleteMapping("/tipo-paradas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tipoParadaService.delete(id);
    }
}
