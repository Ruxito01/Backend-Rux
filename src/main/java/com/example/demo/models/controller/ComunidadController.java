package com.example.demo.models.controller;

import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.service.IComunidadService;
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
import java.util.stream.Collectors;
import com.example.demo.models.dto.ComunidadDTO;

@RestController
@RequestMapping("/api/comunidad")
@Tag(name = "Comunidad", description = "API para gestión de Comunidad")
public class ComunidadController {

    @Autowired
    private IComunidadService service;

    @Operation(summary = "Obtener todas las comunidades", description = "Retorna una lista con todas las comunidades registradas")
    @ApiResponse(responseCode = "200", description = "Lista de comunidades obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<ComunidadDTO>> findAll() {
        List<Comunidad> entidades = service.findAll();
        List<ComunidadDTO> dtos = entidades.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener comunidad por ID", description = "Retorna una comunidad específica según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comunidad encontrada", content = @Content(schema = @Schema(implementation = ComunidadDTO.class))),
            @ApiResponse(responseCode = "404", description = "Comunidad no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ComunidadDTO> findById(
            @Parameter(description = "ID de la comunidad a buscar", required = true, example = "1") @PathVariable @NonNull Long id) {
        Comunidad entity = service.findById(id);
        return entity != null ? ResponseEntity.ok(convertToDto(entity)) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear nueva comunidad", description = "Crea una nueva comunidad en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comunidad creada exitosamente", content = @Content(schema = @Schema(implementation = ComunidadDTO.class)))
    })
    @PostMapping
    public ResponseEntity<ComunidadDTO> create(
            @Parameter(description = "Datos de la comunidad a crear", required = true) @RequestBody @NonNull Comunidad entity) {
        Comunidad saved = service.save(entity);
        return ResponseEntity.ok(convertToDto(saved));
    }

    @Operation(summary = "Actualizar comunidad", description = "Actualiza los datos de una comunidad existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comunidad actualizada exitosamente", content = @Content(schema = @Schema(implementation = ComunidadDTO.class))),
            @ApiResponse(responseCode = "404", description = "Comunidad no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ComunidadDTO> update(
            @Parameter(description = "ID de la comunidad a actualizar", required = true, example = "1") @PathVariable @NonNull Long id,
            @Parameter(description = "Nuevos datos de la comunidad", required = true) @RequestBody @NonNull Comunidad entity) {
        Comunidad existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        Comunidad saved = service.save(entity);
        return ResponseEntity.ok(convertToDto(saved));
    }

    @Operation(summary = "Eliminar comunidad", description = "Elimina una comunidad del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comunidad eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Comunidad no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la comunidad a eliminar", required = true, example = "1") @PathVariable @NonNull Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private ComunidadDTO convertToDto(Comunidad entity) {
        ComunidadDTO dto = new ComunidadDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setNivelPrivacidad(entity.getNivelPrivacidad());
        dto.setUrlImagen(entity.getUrlImagen());
        dto.setFechaCreacion(entity.getFechaCreacion());

        if (entity.getCreador() != null) {
            dto.setCreadorId(entity.getCreador().getId());
        }

        if (entity.getMiembros() != null) {
            // Extraer solo IDs para evitar recursión
            List<Long> memberIds = entity.getMiembros().stream()
                    .map(com.example.demo.models.entity.Usuario::getId)
                    .collect(Collectors.toList());
            dto.setMiembrosIds(memberIds);
        }

        return dto;
    }
}
