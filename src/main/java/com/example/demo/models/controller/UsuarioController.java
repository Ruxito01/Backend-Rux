package com.example.demo.models.controller;

import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.entity.Logro;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.service.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/usuario")
@Tag(name = "Usuario", description = "API para gestión de Usuario")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping
    public List<Usuario> findAll() {
        return usuarioService.findAll();
    }

    @Operation(summary = "Obtener usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        return usuarioService.findById(id)
                .map(usuario -> new ResponseEntity<>(usuario, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Verificar si email existe y obtener datos del usuario")
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> findByEmail(@PathVariable String email) {
        return usuarioService.findByEmail(email)
                .map(usuario -> new ResponseEntity<>(usuario, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Obtener las comunidades a las que pertenece un usuario")
    @GetMapping("/{id}/comunidades")
    public ResponseEntity<Set<Comunidad>> getComunidades(@PathVariable Long id) {
        Set<Comunidad> comunidades = usuarioService.getComunidadesByUsuarioId(id);
        return comunidades != null
                ? new ResponseEntity<>(comunidades, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Login de usuario")
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String contrasena = credentials.get("contrasena");

        return usuarioService.findByEmail(email)
                .filter(usuario -> usuario.getContrasena().equals(contrasena))
                .map(usuario -> new ResponseEntity<>(usuario, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @Operation(summary = "Crea un nuevo usuario")
    @PostMapping
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.save(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualiza la información de un usuario existente")
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.findById(id).map(usuarioDB -> {
            usuarioDB.setNombre(usuario.getNombre());
            usuarioDB.setApellido(usuario.getApellido());
            usuarioDB.setFechaNacimiento(usuario.getFechaNacimiento());
            usuarioDB.setCelular(usuario.getCelular());
            usuarioDB.setCedula(usuario.getCedula());
            usuarioDB.setEmail(usuario.getEmail());
            usuarioDB.setContrasena(usuario.getContrasena());
            usuarioDB.setFoto(usuario.getFoto());
            usuarioDB.setGenero(usuario.getGenero());
            usuarioDB.setAlias(usuario.getAlias()); // Actualizar alias
            return new ResponseEntity<>(usuarioService.save(usuarioDB), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Elimina un usuario por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (usuarioService.findById(id).isPresent()) {
            usuarioService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Asignar un logro a un usuario")
    @PostMapping("/{usuarioId}/logros/{logroId}")
    public ResponseEntity<Usuario> asignarLogro(@PathVariable Long usuarioId, @PathVariable Long logroId) {
        return usuarioService.asignarLogro(usuarioId, logroId)
                .map(usuario -> new ResponseEntity<>(usuario, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Obtener los logros de un usuario")
    @GetMapping("/{id}/logros")
    public ResponseEntity<Set<Logro>> getLogros(@PathVariable Long id) {
        Set<Logro> logros = usuarioService.getLogrosByUsuarioId(id);
        return logros != null
                ? new ResponseEntity<>(logros, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Actualizar el alias de un usuario")
    @PatchMapping("/{id}/alias")
    public ResponseEntity<Usuario> actualizarAlias(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        String alias = body.get("alias");
        return usuarioService.actualizarAlias(id, alias)
                .map(usuario -> new ResponseEntity<>(usuario, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Actualizar última actividad del usuario")
    @PutMapping("/{id}/actividad")
    public ResponseEntity<Void> actualizarActividad(@PathVariable Long id) {
        usuarioService.actualizarUltimaActividad(id);
        return ResponseEntity.ok().build();
    }
}
