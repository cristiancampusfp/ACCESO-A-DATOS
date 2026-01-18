package com.example.login.service;

import com.example.login.entity.Usuario;
import com.example.login.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Usuario crearUsuario(Usuario usuario) {
        if (repository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("El username ya existe");
        }
        if (repository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya existe");
        }

        usuario.hashPassword();
        return repository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    @Transactional
    public Usuario actualizarEmail(Long id, String nuevoEmail) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.setEmail(nuevoEmail);
        return repository.save(usuario);
    }

    @Transactional
    public Usuario actualizarUsuario(Long id, String nuevoEmail, String nuevaPassword) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        boolean cambiado = false;

        if (nuevoEmail != null && !nuevoEmail.isBlank() && !nuevoEmail.equals(usuario.getEmail())) {
            if (repository.existsByEmail(nuevoEmail)) {
                throw new IllegalArgumentException("El email ya existe");
            }
            usuario.setEmail(nuevoEmail);
            cambiado = true;
        }

        if (nuevaPassword != null && !nuevaPassword.isBlank()) {
            usuario.setPassword(nuevaPassword);
            usuario.hashPassword();
            cambiado = true;
        }

        if (cambiado) {
            return repository.save(usuario);
        }
        return usuario;
    }

    @Transactional
    public void desactivarUsuario(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.setActivo(false);
        repository.save(usuario);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        repository.deleteById(id);
    }
}
