public class Aderezo extends Producto {
    private int mililitros;
    private int caloriasUnidad;

    public Aderezo(String codigo, String nombre, double precioBase, int stock,
                    int mililitros, int caloriasUnidad) {
        super(codigo, nombre, Categoria.COMPLEMENTOS, precioBase, stock);
        if (mililitros <= 0) {
            throw new IllegalArgumentException("Los mililitros deben ser mayores que cero.");
        }
        this.mililitros = mililitros;
        this.caloriasUnidad = caloriasUnidad;
    }

    @Override
    public double calcularPrecioUnitario(int cantidad) {
        return getPrecioBase();
    }

    @Override
    public int calcularCalorias(int cantidad) {
        return caloriasUnidad * cantidad;
    }

    public double calcularCostoPorMililitro() {
        return redondear(getPrecioBase() / mililitros);
    }
}
