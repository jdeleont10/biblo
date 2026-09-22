public abstract class Producto {
    private String codigo;
    private String nombre;
    private Categoria categoria;
    private double precioBase;
    private int stock;

    public Producto(String codigo, String nombre, Categoria categoria, double precioBase, int stock) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (precioBase <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioBase = precioBase;
        this.stock = stock;
    }

    public abstract double calcularPrecioUnitario(int cantidad);

    public abstract int calcularCalorias(int cantidad);

    public double calcularSubtotal(int cantidad) {
        double precioUnitario = calcularPrecioUnitario(cantidad);
        double subtotal = precioUnitario * cantidad;
        return redondear(subtotal);
    }

    public boolean descontarStock(int cantidad) {
        if (cantidad <= 0) {
            return false;
        }
        if (cantidad > stock) {
            return false;
        }
        stock = stock - cantidad;
        return true;
    }

    public void reponerStock(int cantidad) {
        if (cantidad > 0) {
            stock = stock + cantidad;
        }
    }

    public double calcularValorInventario() {
        double valor = precioBase * stock;
        return redondear(valor);
    }

    protected double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public Categoria getCategoria() { return categoria; }
    public double getPrecioBase() { return precioBase; }
    public int getStock() { return stock; }

    public String toString() {
        return nombre + " - Q" + precioBase;
    }
}
