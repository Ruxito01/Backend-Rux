package com.example.demo.models.controller;

import com.example.demo.models.entity.FotoUsuario;
import com.example.demo.models.service.IFotoUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la entidad FotoUsuario.
 * Expone endpoints CRUD para gestionar fotos de usuarios.
 */
@RestController
@RequestMapping("/api/foto-usuario")
@Tag(name = "FotoUsuario", description = "API para gestión de fotos del carrusel de usuarios")
public class FotoUsuarioController {

    @Autowired
    private IFotoUsuarioService fotoUsuarioService;

    @Operation(summary = "Obtener todas las fotos de usuarios")
    @GetMapping
    public List<FotoUsuario> findAll() {
        return fotoUsuarioService.findAll();
    }

    @Operation(summary = "Obtener foto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<FotoUsuario> findById(@PathVariable Long id) {
        return fotoUsuarioService.findById(id)
                .map(foto -> new ResponseEntity<>(foto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Obtener todas las fotos de un usuario específico")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<FotoUsuario>> findByUsuarioId(@PathVariable Long usuarioId) {
        List<FotoUsuario> fotos = fotoUsuarioService.findByUsuarioId(usuarioId);
        return new ResponseEntity<>(fotos, HttpStatus.OK);
    }

    @Operation(summary = "Crear una nueva foto de usuario")
    @PostMapping
    public ResponseEntity<FotoUsuario> save(@RequestBody FotoUsuario fotoUsuario) {
        FotoUsuario nuevaFoto = fotoUsuarioService.save(fotoUsuario);
        return new ResponseEntity<>(nuevaFoto, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una foto de usuario existente")
    @PutMapping("/{id}")
    public ResponseEntity<FotoUsuario> update(@PathVariable Long id, @RequestBody FotoUsuario fotoUsuario) {
        return fotoUsuarioService.findById(id).map(fotoDB -> {
            fotoDB.setUrlFoto(fotoUsuario.getUrlFoto());
            fotoDB.setFechaSubida(fotoUsuario.getFechaSubida());
            fotoDB.setUsuario(fotoUsuario.getUsuario());
            return new ResponseEntity<>(fotoUsuarioService.save(fotoDB), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Eliminar una foto de usuario por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (fotoUsuarioService.findById(id).isPresent()) {
            fotoUsuarioService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
