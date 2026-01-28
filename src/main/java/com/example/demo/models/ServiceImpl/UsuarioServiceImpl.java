package com.example.demo.models.ServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.models.dao.IUsuarioDao;
import com.example.demo.models.dao.ILogroDao;
import com.example.demo.models.dao.IComunidadDao;
import com.example.demo.models.dao.ICatalogoAvatarDao;
import com.example.demo.models.dao.IMiembroComunidadDao;
import com.example.demo.models.entity.CatalogoAvatar;
import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.entity.Logro;
import com.example.demo.models.entity.MiembroComunidad;
import com.example.demo.models.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private ILogroDao logroDao;

    @Autowired
    private IComunidadDao comunidadDao;

    @Autowired
    private IMiembroComunidadDao miembroComunidadDao;

    @Autowired
    private ICatalogoAvatarDao avatarDao;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return usuarioDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        return usuarioDao.findByEmail(email);
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        return usuarioDao.save(usuario);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        usuarioDao.deleteById(id);
    }

    @Autowired
    private com.example.demo.models.dao.ILogroUsuarioDao logroUsuarioDao;

    // ...

    @Override
    @Transactional
    public Optional<Usuario> asignarLogro(Long usuarioId, Long logroId) {
        Optional<Usuario> usuarioOpt = usuarioDao.findById(usuarioId);
        Optional<Logro> logroOpt = logroDao.findById(logroId);

        if (usuarioOpt.isPresent() && logroOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Logro logro = logroOpt.get();

            // Verificar si ya lo tiene
            if (logroUsuarioDao.existsByUsuarioIdAndLogroId(usuarioId, logroId)) {
                return Optional.of(usuario);
            }

            com.example.demo.models.entity.LogroUsuario lu = new com.example.demo.models.entity.LogroUsuario();
            lu.setUsuario(usuario);
            lu.setLogro(logro);
            lu.setFechaObtencion(java.time.LocalDateTime.now());
            lu.setCelebrado(false);
            logroUsuarioDao.save(lu);

            return Optional.of(usuario);
        }
        return Optional.empty();
    }

    // ... (otras funciones)

    @Override
    @Transactional(readOnly = true)
    public Set<Comunidad> getComunidadesByUsuarioId(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioDao.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Set<Comunidad> comunidades = new java.util.HashSet<>();

            // Obtener comunidades donde es miembro ACTIVO usando MiembroComunidadDao
            List<MiembroComunidad> membresias = miembroComunidadDao.findByUsuarioId(usuarioId);
            for (MiembroComunidad membresia : membresias) {
                // Solo incluir si el estado es "activo" o null (compatibilidad)
                if (membresia.getEstado() == null || "activo".equals(membresia.getEstado())) {
                    comunidades.add(membresia.getComunidad());
                }
            }

            // Agregar comunidades donde es creador (siempre, aunque haya salido)
            List<Comunidad> comunidadesCreadas = comunidadDao.findByCreador(usuario);
            comunidades.addAll(comunidadesCreadas);

            return comunidades;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Logro> getLogrosByUsuarioId(Long usuarioId) {
        // Obtenemos los logros via la tabla intermedia
        List<com.example.demo.models.entity.LogroUsuario> logrosUsuario = logroUsuarioDao.findByUsuarioId(usuarioId);
        return logrosUsuario.stream()
                .map(com.example.demo.models.entity.LogroUsuario::getLogro)
                .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    @Transactional
    public Optional<Usuario> actualizarAlias(Long usuarioId, String alias) {
        Optional<Usuario> usuarioOpt = usuarioDao.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Validar y truncar si excede 12 caracteres
            if (alias != null && alias.length() > 12) {
                alias = alias.substring(0, 12);
            }
            usuario.setAlias(alias);
            return Optional.of(usuarioDao.save(usuario));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void actualizarUltimaActividad(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioDao.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setUltimaActividad(java.time.LocalDateTime.now());
            usuarioDao.save(usuario);
        }
    }

    // ========== MÉTODOS DE AVATARES ==========

    @Override
    @Transactional(readOnly = true)
    public Set<CatalogoAvatar> getAvataresByUsuarioId(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioDao.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Set<CatalogoAvatar> avatares = usuario.getAvatares();
            avatares.size(); // Inicializar la colección lazy
            return avatares;
        }
        return null;
    }

    @Override
    @Transactional
    public Optional<Usuario> agregarAvatar(Long usuarioId, Long avatarId) {
        Optional<Usuario> usuarioOpt = usuarioDao.findById(usuarioId);
        Optional<CatalogoAvatar> avatarOpt = avatarDao.findById(avatarId);

        if (usuarioOpt.isPresent() && avatarOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            CatalogoAvatar avatar = avatarOpt.get();
            usuario.getAvatares().add(avatar);
            return Optional.of(usuarioDao.save(usuario));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> establecerAvatarActivo(Long usuarioId, Long avatarId) {
        Optional<Usuario> usuarioOpt = usuarioDao.findById(usuarioId);
        Optional<CatalogoAvatar> avatarOpt = avatarDao.findById(avatarId);

        if (usuarioOpt.isPresent() && avatarOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            CatalogoAvatar avatar = avatarOpt.get();

            // Establecer como activo directamente (sin verificar colección para evitar lazy
            // issues)
            usuario.setAvatarActivo(avatar);
            Usuario usuarioGuardado = usuarioDao.save(usuario);
            usuarioDao.flush(); // Forzar escritura inmediata a la DB
            return Optional.of(usuarioGuardado);
        }
        return Optional.empty();
    }
}
