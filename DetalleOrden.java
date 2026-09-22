public class DetalleOrden {
    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    public DetalleOrden(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto es obligatorio.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.calcularPrecioUnitario(cantidad);
    }

    public double calcularSubtotal() {
        double subtotal = precioUnitario * cantidad;
        return redondear(subtotal);
    }

    public double calcularAhorroCombo() {
        if (producto instanceof Combo) {
            Combo combo = (Combo) producto;
            double ahorroUnitario = combo.calcularAhorroPorCombo();
            return redondear(ahorroUnitario * cantidad);
        }
        return 0.0;
    }

    public int calcularCaloriasTotales() {
        return producto.calcularCalorias(cantidad);
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
}
