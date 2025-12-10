package com.example.demo.models.controller;

import com.example.demo.models.entity.CatalogoAvatar;
import com.example.demo.models.service.ICatalogoAvatarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogoavatar")
@Tag(name = "CatalogoAvatar", description = "API para gestión de CatalogoAvatar")
public class CatalogoAvatarController {

    @Autowired
    private ICatalogoAvatarService service;

    @Operation(summary = "Obtener todos los avatares del catálogo", description = "Retorna una lista con todos los avatares disponibles en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de avatares obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<CatalogoAvatar>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener avatar por ID", description = "Retorna un avatar específico según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avatar encontrado", content = @Content(schema = @Schema(implementation = CatalogoAvatar.class))),
            @ApiResponse(responseCode = "404", description = "Avatar no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CatalogoAvatar> findById(
            @Parameter(description = "ID del avatar a buscar", required = true, example = "1") @PathVariable @NonNull Long id) {
        CatalogoAvatar entity = service.findById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear nuevo avatar", description = "Crea un nuevo avatar en el catálogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avatar creado exitosamente", content = @Content(schema = @Schema(implementation = CatalogoAvatar.class)))
    })
    @PostMapping
    public ResponseEntity<CatalogoAvatar> create(
            @Parameter(description = "Datos del avatar a crear", required = true) @RequestBody @NonNull CatalogoAvatar entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Actualizar avatar", description = "Actualiza los datos de un avatar existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avatar actualizado exitosamente", content = @Content(schema = @Schema(implementation = CatalogoAvatar.class))),
            @ApiResponse(responseCode = "404", description = "Avatar no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CatalogoAvatar> update(
            @Parameter(description = "ID del avatar a actualizar", required = true, example = "1") @PathVariable @NonNull Long id,
            @Parameter(description = "Nuevos datos del avatar", required = true) @RequestBody @NonNull CatalogoAvatar entity) {
        CatalogoAvatar existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Eliminar avatar", description = "Elimina un avatar del catálogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avatar eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Avatar no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del avatar a eliminar", required = true, example = "1") @PathVariable @NonNull Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
