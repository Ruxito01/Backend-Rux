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

import com.example.demo.models.entity.MiembroComunidad;
import com.example.demo.models.service.IMiembroComunidadService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class MiembroComunidadController {

    @Autowired
    private IMiembroComunidadService miembroComunidadService;

    @GetMapping("/miembro-comunidades")
    public List<MiembroComunidad> index() {
        return miembroComunidadService.findAll();
    }

    @GetMapping("/miembro-comunidades/{id}")
    public MiembroComunidad show(@PathVariable Long id) {
        return miembroComunidadService.findById(id);
    }

    @PostMapping("/miembro-comunidades")
    @ResponseStatus(HttpStatus.CREATED)
    public MiembroComunidad create(@RequestBody MiembroComunidad miembroComunidad) {
        return miembroComunidadService.save(miembroComunidad);
    }

    @PutMapping("/miembro-comunidades/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public MiembroComunidad update(@RequestBody MiembroComunidad miembroComunidad, @PathVariable Long id) {
        MiembroComunidad miembroComunidadActual = miembroComunidadService.findById(id);

        miembroComunidadActual.setComunidad(miembroComunidad.getComunidad());
        miembroComunidadActual.setUsuario(miembroComunidad.getUsuario());
        miembroComunidadActual.setRol(miembroComunidad.getRol());
        miembroComunidadActual.setEstado(miembroComunidad.getEstado());
        miembroComunidadActual.setFechaUnion(miembroComunidad.getFechaUnion());

        return miembroComunidadService.save(miembroComunidadActual);
    }

    @DeleteMapping("/miembro-comunidades/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        miembroComunidadService.delete(id);
    }
}
