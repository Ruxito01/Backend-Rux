package com.example.demo.models.ServiceImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.models.dao.IUsuarioDao;
import com.example.demo.models.entity.Usuario;
import com.example.demo.models.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioDao usuarioDao;

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
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return usuarioDao.existsByEmail(email);
    }

    @Override
    @Transactional
    public Optional<Usuario> updatePartial(Long id, Usuario usuarioParcial) {
        return usuarioDao.findById(id).map(usuarioDB -> {
            // Actualizar solo los campos que no son nulos
            if (usuarioParcial.getNombre() != null) {
                usuarioDB.setNombre(usuarioParcial.getNombre());
            }
            if (usuarioParcial.getApellido() != null) {
                usuarioDB.setApellido(usuarioParcial.getApellido());
            }
            if (usuarioParcial.getEmail() != null) {
                usuarioDB.setEmail(usuarioParcial.getEmail());
            }
            if (usuarioParcial.getCelular() != null) {
                usuarioDB.setCelular(usuarioParcial.getCelular());
            }
            if (usuarioParcial.getFechaNacimiento() != null) {
                usuarioDB.setFechaNacimiento(usuarioParcial.getFechaNacimiento());
            }
            if (usuarioParcial.getFoto() != null) {
                usuarioDB.setFoto(usuarioParcial.getFoto());
            }
            // Encriptar contraseña si se proporciona
            if (usuarioParcial.getContrasena() != null && !usuarioParcial.getContrasena().isEmpty()) {
                String contrasenaEncriptada = BCrypt.hashpw(usuarioParcial.getContrasena(), BCrypt.gensalt());
                usuarioDB.setContrasena(contrasenaEncriptada);
            }

            return usuarioDao.save(usuarioDB);
        });
    }
}
