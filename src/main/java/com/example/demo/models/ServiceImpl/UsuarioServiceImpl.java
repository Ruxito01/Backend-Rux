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
import com.example.demo.models.entity.Comunidad;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.entity.Logro;
import com.example.demo.models.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private ILogroDao logroDao;

    @Autowired
    private IComunidadDao comunidadDao;

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

    @Override
    @Transactional
    public Optional<Usuario> asignarLogro(Long usuarioId, Long logroId) {
        Optional<Usuario> usuarioOpt = usuarioDao.findById(usuarioId);
        Optional<Logro> logroOpt = logroDao.findById(logroId);

        if (usuarioOpt.isPresent() && logroOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Logro logro = logroOpt.get();
            usuario.getLogros().add(logro);
            return Optional.of(usuarioDao.save(usuario));
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Comunidad> getComunidadesByUsuarioId(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioDao.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Obtener comunidades donde es miembro
            Set<Comunidad> comunidades = usuario.getComunidades();
            comunidades.size(); // Inicializar la colección lazy

            // Agregar comunidades donde es creador (si no están ya en el set)
            List<Comunidad> comunidadesCreadas = comunidadDao.findByCreador(usuario);
            comunidades.addAll(comunidadesCreadas);

            return comunidades;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Logro> getLogrosByUsuarioId(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioDao.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Forzar la carga de logros dentro de la transacción
            Set<Logro> logros = usuario.getLogros();
            logros.size(); // Inicializar la colección lazy
            return logros;
        }
        return null;
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
}
