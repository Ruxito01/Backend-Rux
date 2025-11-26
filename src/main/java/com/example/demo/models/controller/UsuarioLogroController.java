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

import com.example.demo.models.entity.UsuarioLogro;
import com.example.demo.models.service.IUsuarioLogroService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class UsuarioLogroController {

    @Autowired
    private IUsuarioLogroService usuarioLogroService;

    @GetMapping("/usuario-logros")
    public List<UsuarioLogro> index() {
        return usuarioLogroService.findAll();
    }

    @GetMapping("/usuario-logros/{id}")
    public UsuarioLogro show(@PathVariable Long id) {
        return usuarioLogroService.findById(id);
    }

    @PostMapping("/usuario-logros")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioLogro create(@RequestBody UsuarioLogro usuarioLogro) {
        return usuarioLogroService.save(usuarioLogro);
    }

    @PutMapping("/usuario-logros/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioLogro update(@RequestBody UsuarioLogro usuarioLogro, @PathVariable Long id) {
        UsuarioLogro usuarioLogroActual = usuarioLogroService.findById(id);

        usuarioLogroActual.setUsuario(usuarioLogro.getUsuario());
        usuarioLogroActual.setLogro(usuarioLogro.getLogro());
        usuarioLogroActual.setFechaObtencion(usuarioLogro.getFechaObtencion());

        return usuarioLogroService.save(usuarioLogroActual);
    }

    @DeleteMapping("/usuario-logros/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        usuarioLogroService.delete(id);
    }
}
