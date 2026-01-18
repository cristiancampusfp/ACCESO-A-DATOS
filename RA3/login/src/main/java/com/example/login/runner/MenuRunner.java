package com.example.login.runner;

import com.example.login.controller.UsuarioController;
import com.example.login.entity.Usuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class MenuRunner implements CommandLineRunner {

    private final UsuarioController usuarioController;

    public MenuRunner(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        System.out.println("=== MENU CRUD USUARIOS (consola) ===");

        while (!salir) {
            imprimirMenu();
            System.out.print("Elija una opción: ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            try {
                switch (line) {
                    case "1": // Listar
                        listarUsuarios();
                        break;
                    case "2": // Crear
                        crearUsuario(scanner);
                        break;
                    case "3": // Buscar por username
                        buscarPorUsername(scanner);
                        break;
                    case "4": // Actualizar email/password
                        actualizarUsuario(scanner);
                        break;
                    case "5": // Desactivar (borrado lógico)
                        desactivarUsuario(scanner);
                        break;
                    case "6": // Eliminar físicamente
                        eliminarUsuario(scanner);
                        break;
                    case "0":
                        salir = true;
                        System.out.println("Saliendo del menú.");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }

        scanner.close();
    }

    private void imprimirMenu() {
        System.out.println("1) Listar usuarios");
        System.out.println("2) Crear usuario");
        System.out.println("3) Buscar por username");
        System.out.println("4) Actualizar (email / password)");
        System.out.println("5) Desactivar usuario (borrado lógico)");
        System.out.println("6) Eliminar usuario (borrado físico)");
        System.out.println("0) Salir");
    }

    private void listarUsuarios() {
        List<Usuario> lista = usuarioController.listar();
        System.out.println("Usuarios (" + lista.size() + "):");
        lista.forEach(u -> System.out.println(" - id=" + u.getId()
                + " username=" + u.getUsername()
                + " email=" + u.getEmail()
                + " activo=" + u.getActivo()));
    }

    private void crearUsuario(Scanner scanner) {
        System.out.print("username: ");
        String username = scanner.nextLine().trim();
        System.out.print("email: ");
        String email = scanner.nextLine().trim();
        System.out.print("password: ");
        String password = scanner.nextLine().trim();

        Usuario u = new Usuario();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(password);

        Usuario creado = usuarioController.crear(u);
        System.out.println("Usuario creado: id=" + creado.getId() + " username=" + creado.getUsername());
    }

    private void buscarPorUsername(Scanner scanner) {
        System.out.print("username a buscar: ");
        String username = scanner.nextLine().trim();
        Usuario u = usuarioController.buscarPorUsernamePlain(username);
        System.out.println("Encontrado: id=" + u.getId() + " username=" + u.getUsername() + " email=" + u.getEmail() + " activo=" + u.getActivo());
    }

    private void actualizarUsuario(Scanner scanner) {
        System.out.print("username a actualizar: ");
        String username = scanner.nextLine().trim();
        Usuario u = usuarioController.buscarPorUsernamePlain(username);

        System.out.print("nuevo email (enter para mantener " + u.getEmail() + "): ");
        String nuevoEmail = scanner.nextLine().trim();
        if (nuevoEmail.isEmpty()) nuevoEmail = null;

        System.out.print("nueva password (enter para mantener actual): ");
        String nuevaPass = scanner.nextLine().trim();
        if (nuevaPass.isEmpty()) nuevaPass = null;

        Usuario actualizado = usuarioController.actualizar(u.getId(), nuevoEmail, nuevaPass);
        System.out.println("Usuario actualizado: id=" + actualizado.getId() + " email=" + actualizado.getEmail());
    }

    private void desactivarUsuario(Scanner scanner) {
        System.out.print("username a desactivar: ");
        String username = scanner.nextLine().trim();
        Usuario u = usuarioController.buscarPorUsernamePlain(username);
        usuarioController.desactivar(u.getId());
        System.out.println("Usuario desactivado: id=" + u.getId());
    }

    private void eliminarUsuario(Scanner scanner) {
        System.out.print("username a eliminar: ");
        String username = scanner.nextLine().trim();
        Usuario u = usuarioController.buscarPorUsernamePlain(username);
        usuarioController.eliminar(u.getId());
        System.out.println("Usuario eliminado físicamente: id=" + u.getId());
    }
}
