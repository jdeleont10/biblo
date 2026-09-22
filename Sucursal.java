import java.util.ArrayList;
import java.util.List;

public class Sucursal {
    private String nombre;
    private String ubicacion;
    private List<Producto> catalogo = new ArrayList<Producto>();
    private List<Producto> opcionesCombo = new ArrayList<Producto>();

    public Sucursal(String nombre, String ubicacion) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (ubicacion == null || ubicacion.isBlank()) {
            throw new IllegalArgumentException("La ubicación es obligatoria.");
        }
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public void agregarProducto(Producto producto) {
        if (producto != null) {
            catalogo.add(producto);
        }
    }

    public void agregarOpcionCombo(Producto producto) {
        if (producto != null) {
            opcionesCombo.add(producto);
        }
    }

    public List<Producto> filtrarPorCategoria(Categoria categoria) {
        List<Producto> encontrados = new ArrayList<Producto>();
        for (Producto producto : catalogo) {
            if (producto.getCategoria() == categoria) {
                encontrados.add(producto);
            }
        }
        return encontrados;
    }

    public List<Complemento> obtenerComplementos() {
        List<Complemento> complementos = new ArrayList<Complemento>();
        for (Producto producto : catalogo) {
            if (producto instanceof Complemento) {
                complementos.add((Complemento) producto);
            }
        }
        return complementos;
    }

    public List<Bebida> obtenerBebidas() {
        List<Bebida> bebidas = new ArrayList<Bebida>();
        for (Producto producto : catalogo) {
            if (producto instanceof Bebida) {
                bebidas.add((Bebida) producto);
            }
        }
        return bebidas;
    }

    public List<Aderezo> obtenerAderezos() {
        List<Aderezo> aderezos = new ArrayList<Aderezo>();
        for (Producto producto : catalogo) {
            if (producto instanceof Aderezo) {
                aderezos.add((Aderezo) producto);
            }
        }
        return aderezos;
    }

    public double calcularValorInventario() {
        double total = 0;
        for (Producto producto : catalogo) {
            total = total + producto.calcularValorInventario();
        }
        return redondear(total);
    }

    public int calcularUnidadesDisponibles() {
        int unidades = 0;
        for (Producto producto : catalogo) {
            unidades = unidades + producto.getStock();
        }
        return unidades;
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public List<Producto> getCatalogo() { return catalogo; }
    public List<Producto> getOpcionesCombo() { return opcionesCombo; }
}
