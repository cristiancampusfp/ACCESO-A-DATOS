java
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cuenta implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Cliente cliente;
    private BigDecimal saldo;
    private final List<Movimiento> movimientos;

    public Cuenta(Cliente cliente) {
        if (cliente == null) throw new IllegalArgumentException("Cliente nulo.");
        this.cliente = cliente;
        this.saldo = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.movimientos = new ArrayList<>();
    }

    public Cliente getCliente() { return cliente; }

    public BigDecimal getSaldo() { return saldo; }

    public List<Movimiento> getMovimientos() {
        return Collections.unmodifiableList(movimientos);
    }

    public void ingresar(BigDecimal cantidad, String concepto) {
        BigDecimal c = validarCantidad(cantidad);
        saldo = saldo.add(c);
        movimientos.add(new Movimiento(LocalDateTime.now(), c, MovimientoTipo.INGRESO, concepto));
    }

    public void retirar(BigDecimal cantidad, String concepto) {
        BigDecimal c = validarCantidad(cantidad);
        if (saldo.compareTo(c) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
        saldo = saldo.subtract(c);
        movimientos.add(new Movimiento(LocalDateTime.now(), c, MovimientoTipo.RETIRADA, concepto));
    }

    private BigDecimal validarCantidad(BigDecimal cantidad) {
        if (cantidad == null) throw new IllegalArgumentException("Importe nulo.");
        BigDecimal c = cantidad.setScale(2, RoundingMode.HALF_UP);
        if (c.signum() <= 0) throw new IllegalArgumentException("El importe debe ser mayor que 0.");
        return c;
    }
}
