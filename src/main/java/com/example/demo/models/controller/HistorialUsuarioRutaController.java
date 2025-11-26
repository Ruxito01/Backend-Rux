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

import com.example.demo.models.entity.HistorialUsuarioRuta;
import com.example.demo.models.service.IHistorialUsuarioRutaService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class HistorialUsuarioRutaController {

    @Autowired
    private IHistorialUsuarioRutaService historialUsuarioRutaService;

    @GetMapping("/historial-usuario-rutas")
    public List<HistorialUsuarioRuta> index() {
        return historialUsuarioRutaService.findAll();
    }

    @GetMapping("/historial-usuario-rutas/{id}")
    public HistorialUsuarioRuta show(@PathVariable Long id) {
        return historialUsuarioRutaService.findById(id);
    }

    @PostMapping("/historial-usuario-rutas")
    @ResponseStatus(HttpStatus.CREATED)
    public HistorialUsuarioRuta create(@RequestBody HistorialUsuarioRuta historialUsuarioRuta) {
        return historialUsuarioRutaService.save(historialUsuarioRuta);
    }

    @PutMapping("/historial-usuario-rutas/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public HistorialUsuarioRuta update(@RequestBody HistorialUsuarioRuta historialUsuarioRuta, @PathVariable Long id) {
        HistorialUsuarioRuta historialUsuarioRutaActual = historialUsuarioRutaService.findById(id);

        historialUsuarioRutaActual.setUsuario(historialUsuarioRuta.getUsuario());
        historialUsuarioRutaActual.setRuta(historialUsuarioRuta.getRuta());
        historialUsuarioRutaActual.setSesionRuta(historialUsuarioRuta.getSesionRuta());
        historialUsuarioRutaActual.setFechaCompletado(historialUsuarioRuta.getFechaCompletado());
        historialUsuarioRutaActual.setTiempoTotalSegundos(historialUsuarioRuta.getTiempoTotalSegundos());
        historialUsuarioRutaActual.setDistanciaRecorrida(historialUsuarioRuta.getDistanciaRecorrida());
        historialUsuarioRutaActual.setVelocidadMedia(historialUsuarioRuta.getVelocidadMedia());

        return historialUsuarioRutaService.save(historialUsuarioRutaActual);
    }

    @DeleteMapping("/historial-usuario-rutas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        historialUsuarioRutaService.delete(id);
    }
}
