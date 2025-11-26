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

import com.example.demo.models.entity.SalidaGrupal;
import com.example.demo.models.service.ISalidaGrupalService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class SalidaGrupalController {

    @Autowired
    private ISalidaGrupalService salidaGrupalService;

    @GetMapping("/salida-grupales")
    public List<SalidaGrupal> index() {
        return salidaGrupalService.findAll();
    }

    @GetMapping("/salida-grupales/{id}")
    public SalidaGrupal show(@PathVariable Long id) {
        return salidaGrupalService.findById(id);
    }

    @PostMapping("/salida-grupales")
    @ResponseStatus(HttpStatus.CREATED)
    public SalidaGrupal create(@RequestBody SalidaGrupal salidaGrupal) {
        return salidaGrupalService.save(salidaGrupal);
    }

    @PutMapping("/salida-grupales/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public SalidaGrupal update(@RequestBody SalidaGrupal salidaGrupal, @PathVariable Long id) {
        SalidaGrupal salidaGrupalActual = salidaGrupalService.findById(id);

        salidaGrupalActual.setRuta(salidaGrupal.getRuta());
        salidaGrupalActual.setOrganizador(salidaGrupal.getOrganizador());
        salidaGrupalActual.setFecha(salidaGrupal.getFecha());

        return salidaGrupalService.save(salidaGrupalActual);
    }

    @DeleteMapping("/salida-grupales/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        salidaGrupalService.delete(id);
    }
}
