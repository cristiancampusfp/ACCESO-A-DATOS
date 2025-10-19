import java.io.Serial;
import java.io.Serializable;

public class Cliente implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String dni;
    private final String nombre;

    public Cliente(String dni, String nombre) {
        if (dni == null || dni.isBlank()) throw new IllegalArgumentException("DNI vacío.");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre vacío.");
        this.dni = dni.trim();
        this.nombre = nombre.trim();
    }

    public String getDni() {
        return dni;
    }
    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre + " (" + dni + ")";
    }
}
