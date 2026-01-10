package com.example.demo.models.controller;

import com.example.demo.models.entity.PrivacidadPerfil;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.service.IPrivacidadPerfilService;
import com.example.demo.models.service.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestion de privacidad del perfil.
 * Permite a usuarios configurar que secciones son visibles para otros.
 */
@RestController
@RequestMapping("/api/privacidad")
@Tag(name = "PrivacidadPerfil", description = "API para gestion de privacidad del perfil")
public class PrivacidadPerfilController {

    @Autowired
    private IPrivacidadPerfilService privacidadService;

    @Autowired
    private IUsuarioService usuarioService;

    @Operation(summary = "Obtener configuracion de privacidad de un usuario")
    @GetMapping("/{usuarioId}")
    public ResponseEntity<PrivacidadPerfil> getByUsuarioId(@PathVariable Long usuarioId) {
        return privacidadService.findByUsuarioId(usuarioId)
                .map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseGet(() -> {
                    // Si no existe, crear configuracion por defecto
                    return usuarioService.findById(usuarioId)
                            .map(usuario -> {
                                PrivacidadPerfil nueva = new PrivacidadPerfil();
                                nueva.setUsuario(usuario);
                                PrivacidadPerfil guardada = privacidadService.save(nueva);
                                return new ResponseEntity<>(guardada, HttpStatus.CREATED);
                            })
                            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                });
    }

    @Operation(summary = "Actualizar configuracion de privacidad")
    @PutMapping("/{usuarioId}")
    public ResponseEntity<PrivacidadPerfil> update(
            @PathVariable Long usuarioId,
            @RequestBody PrivacidadPerfil privacidad) {

        return privacidadService.findByUsuarioId(usuarioId)
                .map(existente -> {
                    existente.setMostrarCorreo(privacidad.getMostrarCorreo());
                    existente.setMostrarEstadisticas(privacidad.getMostrarEstadisticas());
                    existente.setMostrarFotos(privacidad.getMostrarFotos());
                    existente.setMostrarAvatar(privacidad.getMostrarAvatar());
                    existente.setMostrarLogros(privacidad.getMostrarLogros());
                    existente.setMostrarGarage(privacidad.getMostrarGarage());
                    existente.setMostrarComunidades(privacidad.getMostrarComunidades());
                    return new ResponseEntity<>(privacidadService.save(existente), HttpStatus.OK);
                })
                .orElseGet(() -> {
                    // Si no existe, crear con los valores enviados
                    return usuarioService.findById(usuarioId)
                            .map(usuario -> {
                                privacidad.setUsuario(usuario);
                                privacidad.setId(null); // Asegurar que sea nueva
                                return new ResponseEntity<>(privacidadService.save(privacidad), HttpStatus.CREATED);
                            })
                            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                });
    }
}
