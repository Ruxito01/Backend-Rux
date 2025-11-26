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

import com.example.demo.models.entity.Rol;
import com.example.demo.models.service.IRolService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class RolController {

    @Autowired
    private IRolService rolService;

    @GetMapping("/roles")
    public List<Rol> index() {
        return rolService.findAll();
    }

    @GetMapping("/roles/{id}")
    public Rol show(@PathVariable Long id) {
        return rolService.findById(id);
    }

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public Rol create(@RequestBody Rol rol) {
        return rolService.save(rol);
    }

    @PutMapping("/roles/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Rol update(@RequestBody Rol rol, @PathVariable Long id) {
        Rol rolActual = rolService.findById(id);

        rolActual.setNombre(rol.getNombre());

        return rolService.save(rolActual);
    }

    @DeleteMapping("/roles/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rolService.delete(id);
    }
}
