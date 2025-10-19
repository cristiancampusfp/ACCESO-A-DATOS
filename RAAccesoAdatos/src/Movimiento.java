import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Movimiento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final LocalDateTime fecha;
    private final BigDecimal importe; // siempre positivo
    private final MovimientoTipo tipo;
    private final String concepto;

    public Movimiento(LocalDateTime fecha, BigDecimal importe, MovimientoTipo tipo, String concepto) {
        if (fecha == null) throw new IllegalArgumentException("Fecha nula.");
        if (importe == null || importe.signum() <= 0) throw new IllegalArgumentException("Importe debe ser positivo.");
        if (tipo == null) throw new IllegalArgumentException("Tipo nulo.");
        this.fecha = fecha;
        this.importe = importe;
        this.tipo = tipo;
        this.concepto = concepto == null ? "" : concepto.trim();
    }

    public LocalDateTime getFecha() { return fecha; }
    public BigDecimal getImporte() { return importe; }
    public MovimientoTipo getTipo() { return tipo; }
    public String getConcepto() { return concepto; }
}
