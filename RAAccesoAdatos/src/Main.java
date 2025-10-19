import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final NumberFormat MONEDA = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        Cuenta cuenta = iniciarCuentaInteractiva();
        try (Scanner sc = new Scanner(System.in)) {
            boolean salir = false;
            while (!salir) {
                mostrarMenu(cuenta);
                String opcion = sc.nextLine().trim();
                try {
                    switch (opcion) {
                        case "1" -> operarIngreso(cuenta, sc);
                        case "2" -> operarRetirada(cuenta, sc);
                        case "3" -> mostrarSaldo(cuenta);
                        case "4" -> listarMovimientos(cuenta);
                        case "0" -> salir = true;
                        default -> System.out.println("Opción no válida.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Operación no válida: " + e.getMessage());
                }
            }
        }
        try {
            CuentaRepository.guardar(cuenta);
            System.out.println("Cuenta guardada en 'datos/cuenta.dat'.");
        } catch (IOException e) {
            System.out.println("Error al guardar la cuenta: " + e.getMessage());
        }
    }

    private static Cuenta iniciarCuentaInteractiva() {
        if (CuentaRepository.existe()) {
            try {
                Cuenta c = CuentaRepository.cargar();
                System.out.println("Cuenta cargada de 'datos/cuenta.dat'.");
                return c;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("No se pudo cargar la cuenta existente: " + e.getMessage());
            }
        }
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Creación de nueva cuenta");
            System.out.print("DNI: ");
            String dni = sc.nextLine();
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            Cliente cliente = new Cliente(dni, nombre);
            Cuenta cuenta = new Cuenta(cliente);
            System.out.println("Cuenta creada para " + cliente + ".");
            return cuenta;
        }
    }

    private static void mostrarMenu(Cuenta cuenta) {
        System.out.println();
        System.out.println("==== BANCO ====");
        System.out.println("Cliente: " + cuenta.getCliente());
        System.out.println("Saldo: " + MONEDA.format(cuenta.getSaldo()));
        System.out.println("1) Ingresar dinero");
        System.out.println("2) Retirar dinero");
        System.out.println("3) Consultar saldo");
        System.out.println("4) Listar movimientos");
        System.out.println("0) Salir");
        System.out.print("Elige opción: ");
    }

    private static void operarIngreso(Cuenta cuenta, Scanner sc) {
        BigDecimal cantidad = pedirImporte(sc, "Importe a ingresar: ");
        System.out.print("Concepto (opcional): ");
        String concepto = sc.nextLine();
        cuenta.ingresar(cantidad, concepto);
        System.out.println("Ingreso realizado. Nuevo saldo: " + MONEDA.format(cuenta.getSaldo()));
    }

    private static void operarRetirada(Cuenta cuenta, Scanner sc) {
        BigDecimal cantidad = pedirImporte(sc, "Importe a retirar: ");
        System.out.print("Concepto (opcional): ");
        String concepto = sc.nextLine();
        cuenta.retirar(cantidad, concepto);
        System.out.println("Retirada realizada. Nuevo saldo: " + MONEDA.format(cuenta.getSaldo()));
    }

    private static void mostrarSaldo(Cuenta cuenta) {
        System.out.println("Saldo actual: " + MONEDA.format(cuenta.getSaldo()));
    }

    private static void listarMovimientos(Cuenta cuenta) {
        if (cuenta.getMovimientos().isEmpty()) {
            System.out.println("No hay movimientos.");
            return;
        }
        System.out.println("-- Movimientos --");
        cuenta.getMovimientos().forEach(m -> {
            String signo = m.getTipo() == MovimientoTipo.INGRESO ? "+" : "-";
            System.out.println(
                    FMT.format(m.getFecha()) + " | " +
                            m.getTipo() + " | " +
                            signo + MONEDA.format(m.getImporte()) + " | " +
                            (m.getConcepto().isBlank() ? "-" : m.getConcepto())
            );
        });
    }

    private static BigDecimal pedirImporte(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String txt = sc.nextLine().trim().replace(',', '.');
            try {
                BigDecimal v = new BigDecimal(txt);
                if (v.signum() <= 0) {
                    System.out.println("Debe ser mayor que 0.");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Formato inválido. Ej.: 123.45");
            }
        }
    }
}
