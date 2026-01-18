// src/main/java/com/example/login/controller/UsuarioController.java
package com.example.login.controller;

import com.example.login.entity.Usuario;
import com.example.login.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Métodos públicos para uso interno por el MenuRunner (no mapeados como endpoints)
    public Usuario crear(Usuario usuario) {
        return usuarioService.crearUsuario(usuario);
    }

    public List<Usuario> listar() {
        return usuarioService.listarUsuarios();
    }

    public Usuario buscarPorUsernamePlain(String username) {
        return usuarioService.buscarPorUsername(username);
    }

    public Usuario actualizar(Long id, String nuevoEmail, String nuevaPassword) {
        return usuarioService.actualizarUsuario(id, nuevoEmail, nuevaPassword);
    }

    public void desactivar(Long id) {
        usuarioService.desactivarUsuario(id);
    }

    public void eliminar(Long id) {
        usuarioService.eliminarUsuario(id);
    }
}
