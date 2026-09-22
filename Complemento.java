public class Complemento extends Producto {
    private String tamanio;
    private int gramos;
    private int caloriasUnidad;

    public Complemento(String codigo, String nombre, double precioBase, int stock,
                        String tamanio, int gramos, int caloriasUnidad) {
        super(codigo, nombre, Categoria.COMPLEMENTOS, precioBase, stock);
        if (gramos <= 0) {
            throw new IllegalArgumentException("Los gramos deben ser mayores que cero.");
        }
        if (caloriasUnidad <= 0) {
            throw new IllegalArgumentException("Las calorías deben ser mayores que cero.");
        }
        this.tamanio = tamanio;
        this.gramos = gramos;
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

    public int calcularGramosTotales(int cantidad) {
        return gramos * cantidad;
    }

    public double calcularCostoPorGramo() {
        return redondear(getPrecioBase() / gramos);
    }

    public String getTamanio() { return tamanio; }
}
