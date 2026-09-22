public class Hamburguesa extends Producto {
    private int cantidadCarnes;
    private boolean contieneQueso;
    private int caloriasUnidad;

    public Hamburguesa(String codigo, String nombre, Categoria categoria, double precioBase, int stock,
                        int cantidadCarnes, boolean contieneQueso, int caloriasUnidad) {
        super(codigo, nombre, categoria, precioBase, stock);
        if (cantidadCarnes <= 0) {
            throw new IllegalArgumentException("La hamburguesa debe llevar al menos una carne.");
        }
        if (caloriasUnidad <= 0) {
            throw new IllegalArgumentException("Las calorías deben ser mayores que cero.");
        }
        this.cantidadCarnes = cantidadCarnes;
        this.contieneQueso = contieneQueso;
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

    public double calcularPrecioConQuesoExtra(int porciones) {
        double recargo = porciones * 4.0;
        return redondear(getPrecioBase() + recargo);
    }

    public int calcularTotalCarnes(int cantidad) {
        return cantidadCarnes * cantidad;
    }

    public boolean isContieneQueso() { return contieneQueso; }
}
