package com.example.demo.models.controller;

import com.example.demo.models.entity.CatalogoAvatar;
import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.entity.Logro;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.service.ICatalogoAvatarService;
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

    @Autowired
    private ICatalogoAvatarService avatarService;

    @Autowired
    private com.example.demo.models.service.ILogroService logroService;

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
            // Solo actualizar campos que vienen con valor (no null)
            // Esto evita borrar datos existentes cuando se hace update parcial
            if (usuario.getNombre() != null) {
                usuarioDB.setNombre(usuario.getNombre());
            }
            if (usuario.getApellido() != null) {
                usuarioDB.setApellido(usuario.getApellido());
            }
            if (usuario.getFechaNacimiento() != null) {
                usuarioDB.setFechaNacimiento(usuario.getFechaNacimiento());
            }
            if (usuario.getCelular() != null) {
                usuarioDB.setCelular(usuario.getCelular());
            }
            if (usuario.getCedula() != null) {
                usuarioDB.setCedula(usuario.getCedula());
            }
            if (usuario.getEmail() != null) {
                usuarioDB.setEmail(usuario.getEmail());
            }
            if (usuario.getContrasena() != null) {
                usuarioDB.setContrasena(usuario.getContrasena());
            }
            if (usuario.getFoto() != null) {
                usuarioDB.setFoto(usuario.getFoto());
            }
            if (usuario.getGenero() != null) {
                usuarioDB.setGenero(usuario.getGenero());
            }
            if (usuario.getAlias() != null) {
                usuarioDB.setAlias(usuario.getAlias());
            }
            if (usuario.getRol() != null) {
                usuarioDB.setRol(usuario.getRol());
            }
            if (usuario.getFcmToken() != null) {
                usuarioDB.setFcmToken(usuario.getFcmToken());
            }
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
    public ResponseEntity<?> getLogros(@PathVariable Long id) {
        // Usa query directa para evitar problemas de lazy loading
        java.util.List<com.example.demo.models.entity.Logro> logros = logroService.findByUsuarioId(id);
        if (logros == null) {
            return new ResponseEntity<>(java.util.Collections.emptyList(), HttpStatus.OK);
        }
        // Convertir a lista de Maps simples para evitar problemas de serialización
        java.util.List<java.util.Map<String, Object>> listaLogros = new java.util.ArrayList<>();
        for (com.example.demo.models.entity.Logro logro : logros) {
            java.util.Map<String, Object> logroMap = new java.util.HashMap<>();
            logroMap.put("id", logro.getId());
            logroMap.put("nombre", logro.getNombre());
            logroMap.put("descripcion", logro.getDescripcion());
            logroMap.put("urlIcono", logro.getUrlIcono());
            logroMap.put("criterioDesbloqueo", logro.getCriterioDesbloqueo());
            listaLogros.add(logroMap);
        }
        return new ResponseEntity<>(listaLogros, HttpStatus.OK);
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

    @Operation(summary = "Actualizar token FCM para notificaciones push")
    @PatchMapping("/{id}/fcm-token")
    public ResponseEntity<Usuario> actualizarFcmToken(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String fcmToken = body.get("fcmToken");
        return usuarioService.findById(id).map(usuario -> {
            usuario.setFcmToken(fcmToken);
            return new ResponseEntity<>(usuarioService.save(usuario), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ========== ENDPOINTS DE AVATARES ==========

    @Operation(summary = "Obtener la colección de avatares del usuario")
    @GetMapping("/{id}/avatares")
    public ResponseEntity<?> getAvatares(@PathVariable Long id) {
        // Usa query directa para evitar problemas de lazy loading
        List<CatalogoAvatar> avatares = avatarService.findByUsuarioId(id);
        if (avatares == null || avatares.isEmpty()) {
            return new ResponseEntity<>(java.util.Collections.emptyList(), HttpStatus.OK);
        }
        // Convertir a lista de Maps simples para evitar problemas de serialización
        java.util.List<java.util.Map<String, Object>> listaAvatares = new java.util.ArrayList<>();
        for (CatalogoAvatar avatar : avatares) {
            java.util.Map<String, Object> avatarMap = new java.util.HashMap<>();
            avatarMap.put("id", avatar.getId());
            avatarMap.put("nombre", avatar.getNombre());
            avatarMap.put("descripcion", avatar.getDescripcion());
            avatarMap.put("urlModelo3d", avatar.getUrlModelo3d());
            avatarMap.put("urlPreview", avatar.getUrlPreview());
            avatarMap.put("esPremium", avatar.getEsPremium());
            listaAvatares.add(avatarMap);
        }
        return new ResponseEntity<>(listaAvatares, HttpStatus.OK);
    }

    @Operation(summary = "Agregar un avatar gratuito a la colección del usuario")
    @PostMapping("/{usuarioId}/avatares/{avatarId}")
    public ResponseEntity<?> agregarAvatar(
            @PathVariable Long usuarioId,
            @PathVariable Long avatarId) {
        // Verificar que el avatar no sea premium
        CatalogoAvatar avatar = avatarService.findById(avatarId);
        if (avatar == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (Boolean.TRUE.equals(avatar.getEsPremium())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // No se pueden obtener avatares premium
        }
        // Usar query nativa para evitar problemas de lazy loading
        avatarService.insertarEnColeccion(usuarioId, avatarId);
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("mensaje", "Avatar agregado a la colección");
        response.put("usuarioId", usuarioId);
        response.put("avatarId", avatarId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Establecer el avatar activo del usuario")
    @PutMapping("/{usuarioId}/avatar-activo/{avatarId}")
    public ResponseEntity<?> establecerAvatarActivo(
            @PathVariable Long usuarioId,
            @PathVariable Long avatarId) {
        return usuarioService.establecerAvatarActivo(usuarioId, avatarId)
                .map(usuario -> {
                    java.util.Map<String, Object> response = new java.util.HashMap<>();
                    response.put("mensaje", "Avatar activo actualizado");
                    response.put("usuarioId", usuario.getId());
                    response.put("avatarId", avatarId);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
