// java
package com.example.login;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class LoginApplication implements CommandLineRunner {

    private final DataSource dataSource;

    public LoginApplication(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(LoginApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔹 Probando conexión a la base de datos...");

        try (Connection conn = dataSource.getConnection()) {
            System.out.println("✅ Conexión correcta!");
            System.out.println("Base de datos: " + conn.getCatalog());
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar a la base de datos");
            e.printStackTrace();
        }
    }
}
