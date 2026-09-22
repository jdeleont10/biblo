public class Combo extends Producto {
    public static final double DESCUENTO_COMBO = 0.12;

    private Producto productoPrincipal;
    private Complemento complemento;
    private Bebida bebida;

    public Combo(String codigo, String nombre, Categoria categoria, double precioBase, int stock,
                 Producto productoPrincipal, Complemento complemento, Bebida bebida) {
        super(codigo, nombre, categoria, precioBase, stock);
        if (productoPrincipal == null || complemento == null || bebida == null) {
            throw new IllegalArgumentException("El combo necesita producto principal, complemento y bebida.");
        }
        this.productoPrincipal = productoPrincipal;
        this.complemento = complemento;
        this.bebida = bebida;
    }

    @Override
    public double calcularPrecioUnitario(int cantidad) {
        return getPrecioBase();
    }

    @Override
    public int calcularCalorias(int cantidad) {
        int caloriasPrincipal = productoPrincipal.calcularCalorias(1);
        int caloriasComplemento = complemento.calcularCalorias(1);
        int caloriasBebida = bebida.calcularCalorias(1);
        int caloriasCombo = caloriasPrincipal + caloriasComplemento + caloriasBebida;
        return caloriasCombo * cantidad;
    }

    @Override
    public boolean descontarStock(int cantidad) {
        if (cantidad > getStock()) {
            return false;
        }
        if (cantidad > productoPrincipal.getStock()) {
            return false;
        }
        if (cantidad > complemento.getStock()) {
            return false;
        }
        if (cantidad > bebida.getStock()) {
            return false;
        }
        boolean descontado = super.descontarStock(cantidad);
        if (!descontado) {
            return false;
        }
        productoPrincipal.descontarStock(cantidad);
        complemento.descontarStock(cantidad);
        bebida.descontarStock(cantidad);
        return true;
    }

    @Override
    public void reponerStock(int cantidad) {
        super.reponerStock(cantidad);
        productoPrincipal.reponerStock(cantidad);
        complemento.reponerStock(cantidad);
        bebida.reponerStock(cantidad);
    }

    public double calcularPrecioComponentes() {
        double total = productoPrincipal.getPrecioBase() + complemento.getPrecioBase() + bebida.getPrecioBase();
        return redondear(total);
    }

    public double calcularAhorroPorCombo() {
        double ahorro = calcularPrecioComponentes() - getPrecioBase();
        return redondear(ahorro);
    }

    public String describirComponentes() {
        return productoPrincipal.getNombre() + " + " + complemento.getNombre() + " + " + bebida.getNombre();
    }

    public Producto getProductoPrincipal() { return productoPrincipal; }
    public Complemento getComplemento() { return complemento; }
    public Bebida getBebida() { return bebida; }
}
