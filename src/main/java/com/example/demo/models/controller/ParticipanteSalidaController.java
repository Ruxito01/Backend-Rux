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

import com.example.demo.models.entity.ParticipanteSalida;
import com.example.demo.models.service.IParticipanteSalidaService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ParticipanteSalidaController {

    @Autowired
    private IParticipanteSalidaService participanteSalidaService;

    @GetMapping("/participante-salidas")
    public List<ParticipanteSalida> index() {
        return participanteSalidaService.findAll();
    }

    @GetMapping("/participante-salidas/{id}")
    public ParticipanteSalida show(@PathVariable Long id) {
        return participanteSalidaService.findById(id);
    }

    @PostMapping("/participante-salidas")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipanteSalida create(@RequestBody ParticipanteSalida participanteSalida) {
        return participanteSalidaService.save(participanteSalida);
    }

    @PutMapping("/participante-salidas/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipanteSalida update(@RequestBody ParticipanteSalida participanteSalida, @PathVariable Long id) {
        ParticipanteSalida participanteSalidaActual = participanteSalidaService.findById(id);

        participanteSalidaActual.setSalida(participanteSalida.getSalida());
        participanteSalidaActual.setUsuario(participanteSalida.getUsuario());

        return participanteSalidaService.save(participanteSalidaActual);
    }

    @DeleteMapping("/participante-salidas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        participanteSalidaService.delete(id);
    }
}
