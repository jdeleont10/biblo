import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.GraphicsEnvironment;
import java.util.List;

public class Main {
    public static Sucursal crearSucursal() {
        Sucursal sucursal = new Sucursal("Burger King", "Puerto Barrios, Izabal");

        Complemento papas = new Complemento("CP01", "Papas medianas", 16.00, 80, "Mediano", 120, 365);
        Complemento papasGrandes = new Complemento("CP05", "Papas grandes", 20.00, 60, "Grande", 150, 450);
        Complemento hash = new Complemento("CP02", "Hash Browns", 18.00, 45, "Mediano", 110, 310);
        Complemento nuggets = new Complemento("CP03", "Nuggets", 24.00, 55, "6 unidades", 120, 290);
        Complemento ensalada = new Complemento("CP04", "Ensalada", 20.00, 25, "Individual", 150, 120);
        Aderezo bbq = new Aderezo("AD01", "BBQ", 1.00, 200, 30, 20);
        Aderezo ranch = new Aderezo("AD02", "Ranch", 1.00, 200, 30, 25);
        Aderezo picante = new Aderezo("AD03", "Picante", 1.00, 200, 30, 15);
        Aderezo mielMostaza = new Aderezo("AD04", "Miel Mostaza", 1.00, 200, 30, 35);
        Bebida gaseosaPepsi = new Bebida("BE01", "Gaseosa Pepsi", 14.00, 100, 16, true, 180);
        Bebida agua = new Bebida("BE02", "Agua pura", 10.00, 60, 16, false, 0);
        Bebida jugo = new Bebida("BE03", "Jugo natural", 18.00, 40, 16, false, 0);
        Bebida gaseosaMirinda = new Bebida("BE04", "Gaseosa Mirinda", 14.00, 80, 16, true, 180);
        Bebida gaseosa7up = new Bebida("BE05", "Gaseosa 7up", 14.00, 80, 16, true, 180);
        Bebida gaseosaUva = new Bebida("BE06", "Gaseosa Uva", 14.00, 80, 16, true, 180);
        Bebida gaseosaPepsiGrande = new Bebida("BE07", "Gaseosa Pepsi Grande", 18.00, 60, 20, true, 240);
        Bebida gaseosaMirindaGrande = new Bebida("BE08", "Gaseosa Mirinda Grande", 18.00, 60, 20, true, 240);
        Bebida gaseosa7upGrande = new Bebida("BE09", "Gaseosa 7up Grande", 18.00, 60, 20, true, 240);
        Bebida gaseosaUvaGrande = new Bebida("BE10", "Gaseosa Uva Grande", 18.00, 60, 20, true, 240);


        Hamburguesa whopper = new Hamburguesa("HA01", "Whopper", Categoria.WHOPPER, 42.00, 45, 1, false, 657);
        Hamburguesa whopperDoble = new Hamburguesa("HA02", "Whopper Doble", Categoria.WHOPPER, 54.00, 35, 2, false, 900);
        Hamburguesa whopperTejano = new Hamburguesa("HA03", "Whopper Tejano", Categoria.WHOPPER, 49.00, 32, 1, true, 780);
        Hamburguesa whopperTejanoDoble = new Hamburguesa("HA04", "Whopper Tejano Doble", Categoria.WHOPPER, 59.00, 28, 2, true, 980);
        Hamburguesa whopperJr = new Hamburguesa("HA05", "Whopper Jr.", Categoria.WHOPPER_JR, 29.00, 50, 1, false, 390);
        Hamburguesa WhopperJrTejano = new Hamburguesa("HA15", "Whopper Jr. Tejano", Categoria.WHOPPER_JR, 33.00, 45, 1, true, 450);
        Hamburguesa quesoDoble = new Hamburguesa("HA06", "Quesoburguesa Doble", Categoria.HAMBURGUESAS, 36.00, 42, 2, true, 520);
        Hamburguesa bigKing = new Hamburguesa("HA07", "Big King", Categoria.HAMBURGUESAS, 44.00, 38, 2, true, 690);
        Hamburguesa megaBigKing = new Hamburguesa("HA08", "Mega Big King", Categoria.WHOPPER, 58.00, 28, 3, true, 980);
        Hamburguesa stackerRodeo = new Hamburguesa("HA09", "Stacker Rodeo", Categoria.WHOPPER, 48.00, 34, 2, true, 760);
        Hamburguesa stackerAtomica = new Hamburguesa("HA10", "Stacker Atómica", Categoria.WHOPPER, 52.00, 30, 2, true, 840);
        Hamburguesa kingPollo = new Hamburguesa("HA11", "King de Pollo", Categoria.POLLO_PESCADO, 43.00, 36, 1, false, 620);
        Hamburguesa kingPescado = new Hamburguesa("HA12", "King de Pescado", Categoria.POLLO_PESCADO, 45.00, 25, 1, true, 570);
        Hamburguesa tendergrill = new Hamburguesa("HA13", "Tendergrill", Categoria.POLLO_PESCADO, 46.00, 30, 1, false, 510);
        Hamburguesa crispyChicken = new Hamburguesa("HA14", "Crispy Chicken", Categoria.POLLO_PESCADO, 41.00, 35, 1, false, 670);
        Hamburguesa quesoBurgesa = new Hamburguesa("HA16", "Quesoburguesa", Categoria.HAMBURGUESAS, 28.00, 40, 1, true, 400);

        Producto[] todos = {
                whopper, whopperDoble, whopperTejano, whopperTejanoDoble, whopperJr, WhopperJrTejano,
                quesoDoble, bigKing, megaBigKing, stackerRodeo, stackerAtomica,
                kingPollo, kingPescado, tendergrill, crispyChicken, jugo,
                papas, papasGrandes, hash, nuggets, ensalada, gaseosaPepsi,
                gaseosaMirinda, gaseosa7up, gaseosaUva, gaseosaPepsiGrande,
                gaseosaMirindaGrande, gaseosa7upGrande, gaseosaUvaGrande, agua,
                bbq, ranch, picante, mielMostaza
        };
        for (Producto producto : todos) {
            sucursal.agregarProducto(producto);
        }

        Producto[] opcionesCombo = {
                whopper, quesoDoble, kingPollo, kingPescado, bigKing, whopperJr,
                whopperDoble, whopperTejano, megaBigKing, stackerRodeo,
                stackerAtomica, nuggets, tendergrill, crispyChicken
        };
        for (Producto producto : opcionesCombo) {
            sucursal.agregarOpcionCombo(producto);
        }
        return sucursal;
    }

    public static boolean verificarCodigosUnicos(Sucursal sucursal) {
        List<Producto> catalogo = sucursal.getCatalogo();
        for (int i = 0; i < catalogo.size(); i++) {
            for (int j = i + 1; j < catalogo.size(); j++) {
                String codigoUno = catalogo.get(i).getCodigo();
                String codigoDos = catalogo.get(j).getCodigo();
                if (codigoUno.equals(codigoDos)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int calcularCantidadCategorias() {
        return Categoria.values().length;
    }

    public static void main(String[] args) {
        Sucursal sucursal = crearSucursal();
        boolean codigosUnicos = verificarCodigosUnicos(sucursal);
        if (!codigosUnicos) {
            throw new IllegalStateException("Existen códigos de producto repetidos.");
        }
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println(sucursal.getNombre() + " - " + sucursal.getUbicacion());
            System.out.println("Productos: " + sucursal.getCatalogo().size()
                    + " | Categorías: " + calcularCantidadCategorias()
                    + " | Unidades: " + sucursal.calcularUnidadesDisponibles());
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {
                    // Se usa la apariencia predeterminada de Swing.
                }
                new VentanaPOS(sucursal).setVisible(true);
            }
        });
    }
}
