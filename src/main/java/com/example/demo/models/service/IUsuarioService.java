package com.example.demo.models.service;

import com.example.demo.models.entity.CatalogoAvatar;
import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.entity.Logro;
import com.example.demo.models.entity.Usuario;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IUsuarioService {

    List<Usuario> findAll();

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByEmail(String email);

    Usuario save(Usuario usuario);

    void deleteById(Long id);

    Optional<Usuario> asignarLogro(Long usuarioId, Long logroId);

    Set<Comunidad> getComunidadesByUsuarioId(Long usuarioId);

    // Obtener los logros de un usuario (necesario porque logros tiene @JsonIgnore)
    Set<Logro> getLogrosByUsuarioId(Long usuarioId);

    // Actualizar el alias del usuario
    Optional<Usuario> actualizarAlias(Long usuarioId, String alias);

    // Actualizar última actividad del usuario
    void actualizarUltimaActividad(Long usuarioId);

    // ========== MÉTODOS DE AVATARES ==========

    // Obtener la colección de avatares del usuario
    Set<CatalogoAvatar> getAvataresByUsuarioId(Long usuarioId);

    // Agregar un avatar a la colección del usuario
    Optional<Usuario> agregarAvatar(Long usuarioId, Long avatarId);

    // Establecer el avatar activo del usuario
    Optional<Usuario> establecerAvatarActivo(Long usuarioId, Long avatarId);
}
