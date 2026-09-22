import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Factura {
    private static int correlativo = 5000;
    private String numero;
    private Orden orden;
    private LocalDateTime fecha;

    public Factura(Orden orden) {
        if (orden == null || !orden.isCompletada()) {
            throw new IllegalArgumentException("Solo se factura una orden completada.");
        }
        this.orden = orden;
        this.numero = generarNumero();
        this.fecha = LocalDateTime.now();
    }

    public String generarNumero() {
        correlativo = correlativo + 1;
        return String.format("BKPB-%06d", correlativo);
    }

    public int calcularProductosVendidos() {
        int cantidad = 0;
        for (DetalleOrden detalle : orden.getDetalles()) {
            cantidad = cantidad + detalle.getCantidad();
        }
        return cantidad;
    }

    public String construirComprobante() {
        StringBuilder texto = new StringBuilder();
        texto.append("\n==========================================================\n");
        texto.append("              NO. Restaurante 33261\n");
        texto.append("                  BURGER KING\n");
        texto.append("             Puerto Barrios, Izabal\n");
        texto.append("==========================================================\n");
        texto.append(String.format("Factura: %s       Orden: #%d%n", numero, orden.getNumero()));
        texto.append("Fecha: ").append(fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append('\n');
        texto.append("----------------------------------------------------------\n");
        texto.append(String.format("%-24s %4s %9s %10s%n", "Producto", "Cant", "P/U", "Subtotal"));
        for (DetalleOrden detalle : orden.getDetalles()) {
            texto.append(String.format("%-24s %4d Q%7.2f Q%8.2f%n",
                    recortar(detalle.getProducto().getNombre(), 24), detalle.getCantidad(),
                    detalle.getPrecioUnitario(), detalle.calcularSubtotal()));
            if (detalle.getProducto() instanceof Combo) {
                Combo combo = (Combo) detalle.getProducto();
                texto.append("  Incluye: ").append(recortar(combo.describirComponentes(), 46)).append('\n');
            }
        }
        texto.append("----------------------------------------------------------\n");
        texto.append(String.format("Productos: %d%n", calcularProductosVendidos()));
        texto.append(String.format("Ahorro en combos (12%%):          Q%10.2f%n", orden.calcularAhorroCombos()));
        texto.append(String.format("Subtotal:                       Q%10.2f%n", orden.calcularSubtotal()));
        texto.append(String.format("IVA (12%%):                      Q%10.2f%n", orden.calcularIVA()));
        texto.append(String.format("TOTAL PAGADO:                   Q%10.2f%n", orden.getTotalPagado()));
        texto.append("Método: ").append(orden.getMetodoPago().getNombre()).append('\n');
        if (orden.getMetodoPago() instanceof PagoEfectivo) {
            PagoEfectivo efectivo = (PagoEfectivo) orden.getMetodoPago();
            texto.append(String.format("Recibido: Q%.2f     Cambio: Q%.2f%n",
                    efectivo.getRecibido(), efectivo.getCambio()));
        }
        texto.append("=================== GRACIAS POR SU VISITA =================\n");
        return texto.toString();
    }

    private String recortar(String valor, int longitud) {
        if (valor.length() <= longitud) {
            return valor;
        }
        return valor.substring(0, longitud - 1) + "...";
    }

    public String getNumero() { return numero; }
    public LocalDateTime getFecha() { return fecha; }
}
