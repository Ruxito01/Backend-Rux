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

import com.example.demo.models.entity.ValidacionPuntoSesion;
import com.example.demo.models.service.IValidacionPuntoSesionService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ValidacionPuntoSesionController {

    @Autowired
    private IValidacionPuntoSesionService validacionPuntoSesionService;

    @GetMapping("/validacion-punto-sesiones")
    public List<ValidacionPuntoSesion> index() {
        return validacionPuntoSesionService.findAll();
    }

    @GetMapping("/validacion-punto-sesiones/{id}")
    public ValidacionPuntoSesion show(@PathVariable Long id) {
        return validacionPuntoSesionService.findById(id);
    }

    @PostMapping("/validacion-punto-sesiones")
    @ResponseStatus(HttpStatus.CREATED)
    public ValidacionPuntoSesion create(@RequestBody ValidacionPuntoSesion validacionPuntoSesion) {
        return validacionPuntoSesionService.save(validacionPuntoSesion);
    }

    @PutMapping("/validacion-punto-sesiones/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ValidacionPuntoSesion update(@RequestBody ValidacionPuntoSesion validacionPuntoSesion,
            @PathVariable Long id) {
        ValidacionPuntoSesion validacionPuntoSesionActual = validacionPuntoSesionService.findById(id);

        validacionPuntoSesionActual.setSesionRuta(validacionPuntoSesion.getSesionRuta());
        validacionPuntoSesionActual.setPuntoInteres(validacionPuntoSesion.getPuntoInteres());
        validacionPuntoSesionActual.setHora(validacionPuntoSesion.getHora());

        return validacionPuntoSesionService.save(validacionPuntoSesionActual);
    }

    @DeleteMapping("/validacion-punto-sesiones/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        validacionPuntoSesionService.delete(id);
    }
}
