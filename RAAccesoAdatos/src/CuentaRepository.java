import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class CuentaRepository {
    private static final Path DIR = Paths.get("datos");
    private static final Path FILE = DIR.resolve("cuenta.dat");

    private CuentaRepository() {}

    public static boolean existe() {
        return Files.exists(FILE);
    }

    public static void guardar(Cuenta cuenta) throws IOException {
        if (cuenta == null) throw new IllegalArgumentException("Cuenta nula.");
        Files.createDirectories(DIR);
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(FILE))) {
            oos.writeObject(cuenta);
        }
    }

    public static Cuenta cargar() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(FILE))) {
            Object o = ois.readObject();
            if (!(o instanceof Cuenta c)) throw new IOException("El fichero no contiene una Cuenta válida.");
            return c;
        }
    }
}
