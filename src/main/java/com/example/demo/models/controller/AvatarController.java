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

import com.example.demo.models.entity.Avatar;
import com.example.demo.models.service.IAvatarService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class AvatarController {

    @Autowired
    private IAvatarService avatarService;

    @GetMapping("/avatars")
    public List<Avatar> index() {
        return avatarService.findAll();
    }

    @GetMapping("/avatars/{id}")
    public Avatar show(@PathVariable Long id) {
        return avatarService.findById(id);
    }

    @PostMapping("/avatars")
    @ResponseStatus(HttpStatus.CREATED)
    public Avatar create(@RequestBody Avatar avatar) {
        return avatarService.save(avatar);
    }

    @PutMapping("/avatars/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Avatar update(@RequestBody Avatar avatar, @PathVariable Long id) {
        Avatar avatarActual = avatarService.findById(id);

        avatarActual.setNombre(avatar.getNombre());
        avatarActual.setUrlImagen(avatar.getUrlImagen());
        avatarActual.setTipo(avatar.getTipo());
        avatarActual.setNivelRequerido(avatar.getNivelRequerido());

        return avatarService.save(avatarActual);
    }

    @DeleteMapping("/avatars/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        avatarService.delete(id);
    }
}
