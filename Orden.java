import java.util.ArrayList;
import java.util.List;

public class Orden {
    public static final double IVA = 0.12;
    private static int siguienteNumero = 1;

    private int numero;
    private List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
    private boolean completada;
    private MetodoPago metodoPago;
    private double ivaAplicado;
    private double totalPagado;

    public Orden() {
        this.numero = siguienteNumero;
        siguienteNumero = siguienteNumero + 1;
    }

    public boolean agregarProducto(Producto producto, int cantidad) {
        if (completada) {
            return false;
        }
        if (producto == null || cantidad <= 0) {
            return false;
        }
        boolean descontado = producto.descontarStock(cantidad);
        if (!descontado) {
            return false;
        }
        detalles.add(new DetalleOrden(producto, cantidad));
        return true;
    }

    public boolean eliminarDetalle(int indice) {
        if (completada) {
            return false;
        }
        if (indice < 0 || indice >= detalles.size()) {
            return false;
        }
        DetalleOrden eliminado = detalles.remove(indice);
        eliminado.getProducto().reponerStock(eliminado.getCantidad());
        return true;
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (DetalleOrden detalle : detalles) {
            subtotal = subtotal + detalle.calcularSubtotal();
        }
        return redondear(subtotal);
    }

    public double calcularAhorroCombos() {
        double ahorro = 0;
        for (DetalleOrden detalle : detalles) {
            ahorro = ahorro + detalle.calcularAhorroCombo();
        }
        return redondear(ahorro);
    }

    public double calcularIVA() {
        if (completada) {
            return ivaAplicado;
        }
        double subtotal = calcularSubtotal();
        return redondear(subtotal * IVA);
    }

    public double calcularTotal() {
        double subtotal = calcularSubtotal();
        double iva = calcularIVA();
        return redondear(subtotal + iva);
    }

    public int calcularCaloriasOrden() {
        int calorias = 0;
        for (DetalleOrden detalle : detalles) {
            calorias = calorias + detalle.calcularCaloriasTotales();
        }
        return calorias;
    }

    public boolean completarOrden(MetodoPago pago) {
        if (completada) {
            return false;
        }
        if (detalles.isEmpty()) {
            return false;
        }
        if (pago == null) {
            return false;
        }
        double total = calcularTotal();
        boolean pagoValido = pago.procesarPago(total);
        if (!pagoValido) {
            return false;
        }
        ivaAplicado = calcularIVA();
        totalPagado = total;
        metodoPago = pago;
        completada = true;
        return true;
    }

    public int cancelarOrden() {
        if (completada) {
            return 0;
        }
        int devueltas = 0;
        for (DetalleOrden detalle : detalles) {
            detalle.getProducto().reponerStock(detalle.getCantidad());
            devueltas = devueltas + detalle.getCantidad();
        }
        detalles.clear();
        return devueltas;
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    public int getNumero() { return numero; }
    public List<DetalleOrden> getDetalles() { return detalles; }
    public boolean isCompletada() { return completada; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public double getTotalPagado() { return totalPagado; }
}
