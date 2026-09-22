public class Bebida extends Producto {
    private int onzas;
    private boolean permiteRecarga;
    private int caloriasUnidad;

    public Bebida(String codigo, String nombre, double precioBase, int stock,
                   int onzas, boolean permiteRecarga, int caloriasUnidad) {
        super(codigo, nombre, Categoria.BEBIDAS, precioBase, stock);
        if (onzas <= 0) {
            throw new IllegalArgumentException("El volumen debe ser mayor que cero.");
        }
        this.onzas = onzas;
        this.permiteRecarga = permiteRecarga;
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

    public double convertirALitros() {
        return redondear(onzas * 0.0295735);
    }

    public double calcularLitrosTotales(int cantidad) {
        double litrosUnitarios = convertirALitros();
        return redondear(litrosUnitarios * cantidad);
    }

    public boolean isPermiteRecarga() { return permiteRecarga; }
}
