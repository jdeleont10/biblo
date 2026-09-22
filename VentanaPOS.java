import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VentanaPOS extends JFrame {
    private static final Color FONDO = new Color(213, 218, 216);
    private static final Color BLANCO = new Color(252, 252, 252);
    private static final Color AMARILLO = new Color(255, 245, 0);
    private static final Color BORDE = new Color(195, 195, 195);
    private static final Color ROJO = new Color(205, 55, 32);
    private static final Color VERDE = new Color(25, 135, 62);
    private static final Color MORADO = new Color(120, 30, 130);
    private static final Color TEXTO = new Color(45, 35, 30);

    private final Sucursal sucursal;
    private final JPanel panelProductos = new JPanel(new GridLayout(0, 5, 5, 5));
    private final DefaultTableModel modeloOrden;
    private final JTable tablaOrden;
    private final JLabel etiquetaCategoria = new JLabel();
    private final JLabel etiquetaEstado = new JLabel();
    private final JLabel etiquetaOrden = new JLabel();
    private final JLabel etiquetaSubtotal = new JLabel();
    private final JLabel etiquetaAhorro = new JLabel();
    private final JLabel etiquetaIva = new JLabel();
    private final JLabel etiquetaTotal = new JLabel();
    private final JLabel etiquetaReloj = new JLabel();
    private final JLabel etiquetaCantidad = new JLabel("1", SwingConstants.CENTER);
    private final TecladoCantidad tecladoCantidad = new TecladoCantidad();

    private Orden ordenActual = new Orden();
    private Factura ultimaFactura;
    private Categoria categoriaActual = Categoria.COMBOS;
    private int contadorCombos = 0;
    private Producto seleccionEnDialogo;

    public VentanaPOS(Sucursal sucursal) {
        if (sucursal == null) throw new IllegalArgumentException("La sucursal es obligatoria.");
        this.sucursal = sucursal;
        this.modeloOrden = crearModeloNoEditable(new String[]{"Cnt", "Descripción", "Precio"});
        this.tablaOrden = crearTablaOrden();
        configurarVentana();
        construirPantalla();
        mostrarCategoria(Categoria.COMBOS);
        actualizarOrden();
        iniciarReloj();
    }

    public void configurarVentana() {
        setTitle("Burger King Puerto Barrios — Punto de venta");
        setSize(1450, 850);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setBackground(FONDO);

        File imagen = new File("img/negocio.png");
        if (imagen.isFile()) setIconImage(new ImageIcon(imagen.getAbsolutePath()).getImage());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evento) {
                cerrarSistema();
            }
        });
    }

    public void construirPantalla() {
        setLayout(new BorderLayout(4, 4));
        add(crearEncabezado(), BorderLayout.NORTH);

        JPanel zonaVenta = new JPanel(new BorderLayout(4, 4));
        zonaVenta.setBackground(FONDO);
        zonaVenta.add(crearMenuCategorias(), BorderLayout.NORTH);
        zonaVenta.add(crearZonaProductos(), BorderLayout.CENTER);
        add(zonaVenta, BorderLayout.CENTER);
        add(crearPanelOrden(), BorderLayout.EAST);
    }

    public double calcularValorVisibleInventario() {
        return sucursal.calcularValorInventario();
    }

    // Crea la parte superior de la ventana.
    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout(15, 0));
        encabezado.setBackground(new Color(236, 11, 11));
        encabezado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        JLabel marca = new JLabel("BURGER KING PUERTO BARRIOS");
        marca.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        marca.setForeground(TEXTO);
        etiquetaEstado.setText("CAJA 1  |  VENTA LOCAL");
        etiquetaEstado.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaEstado.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        etiquetaReloj.setHorizontalAlignment(SwingConstants.RIGHT);
        etiquetaReloj.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        JButton verNegocio = crearBotonPOS("IMAGEN DEL NEGOCIO");
        verNegocio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                mostrarImagenNegocio();
            }
        });
        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        derecha.setOpaque(false);
        derecha.add(verNegocio);
        derecha.add(etiquetaReloj);
        encabezado.add(marca, BorderLayout.WEST);
        encabezado.add(etiquetaEstado, BorderLayout.CENTER);
        encabezado.add(derecha, BorderLayout.EAST);
        return encabezado;
    }

    // Crea los siete botones del menú.
    private JPanel crearMenuCategorias() {
        JPanel menu = new JPanel(new GridLayout(1, Categoria.values().length, 3, 3));
        menu.setBackground(FONDO);
        menu.setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 4));
        for (Categoria categoria : Categoria.values()) {
            JButton boton = crearBotonPOS(categoria.getNombreVisible().toUpperCase());
            boton.setPreferredSize(new Dimension(130, 55));
            boton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evento) {
                    mostrarCategoria(categoria);
                }
            });
            menu.add(boton);
        }
        return menu;
    }

    // Crea el área donde aparecen los productos.
    private JPanel crearZonaProductos() {
        JPanel zona = new JPanel(new BorderLayout(4, 4));
        zona.setBackground(FONDO);
        zona.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 0));
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(FONDO);
        barra.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 4));
        etiquetaCategoria.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

        JPanel cantidad = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        cantidad.setOpaque(false);
        JLabel textoCantidad = new JLabel("Cantidad seleccionada:");
        textoCantidad.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        etiquetaCantidad.setOpaque(true);
        etiquetaCantidad.setBackground(AMARILLO);
        etiquetaCantidad.setForeground(Color.BLACK);
        etiquetaCantidad.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        etiquetaCantidad.setPreferredSize(new Dimension(55, 30));
        etiquetaCantidad.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        cantidad.add(textoCantidad);
        cantidad.add(etiquetaCantidad);
        barra.add(etiquetaCategoria, BorderLayout.WEST);
        barra.add(cantidad, BorderLayout.EAST);
        panelProductos.setBackground(FONDO);
        panelProductos.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        JPanel contenedorProductos = new JPanel(new BorderLayout());
        contenedorProductos.setBackground(FONDO);
        contenedorProductos.add(panelProductos, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(contenedorProductos);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        scroll.getViewport().setBackground(FONDO);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        zona.add(barra, BorderLayout.NORTH);
        zona.add(scroll, BorderLayout.CENTER);
        return zona;
    }

    // Crea la orden, los totales y los controles de cobro.
    private JPanel crearPanelOrden() {
        JPanel panel = new JPanel(new BorderLayout(3, 3));
        panel.setBackground(FONDO);
        panel.setPreferredSize(new Dimension(390, 0));
        panel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, BORDE));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(BLANCO);
        cabecera.setBorder(BorderFactory.createEmptyBorder(7, 8, 7, 8));
        etiquetaOrden.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        JLabel tipo = new JLabel("ORDEN PARA LLEVAR", SwingConstants.RIGHT);
        tipo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        cabecera.add(etiquetaOrden, BorderLayout.WEST);
        cabecera.add(tipo, BorderLayout.EAST);
        panel.add(cabecera, BorderLayout.NORTH);

        JScrollPane scrollOrden = new JScrollPane(tablaOrden);
        scrollOrden.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        scrollOrden.getViewport().setBackground(AMARILLO);
        panel.add(scrollOrden, BorderLayout.CENTER);

        JPanel inferior = new JPanel(new BorderLayout(3, 3));
        inferior.setBackground(FONDO);
        inferior.add(crearResumenTotales(), BorderLayout.NORTH);
        JPanel controles = new JPanel(new BorderLayout(3, 3));
        controles.setBackground(FONDO);
        controles.add(crearAcciones(), BorderLayout.NORTH);
        controles.add(crearTecladoNumerico(), BorderLayout.CENTER);
        inferior.add(controles, BorderLayout.CENTER);
        panel.add(inferior, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearResumenTotales() {
        JPanel resumen = new JPanel(new GridLayout(4, 1));
        resumen.setBackground(AMARILLO);
        resumen.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        Font fuente = new Font(Font.MONOSPACED, Font.BOLD, 15);
        for (JLabel etiqueta : new JLabel[]{etiquetaSubtotal, etiquetaAhorro, etiquetaIva, etiquetaTotal}) {
            etiqueta.setFont(fuente);
            etiqueta.setForeground(Color.BLACK);
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
            etiqueta.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 8));
            resumen.add(etiqueta);
        }
        etiquetaTotal.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
        return resumen;
    }

    private JPanel crearAcciones() {
        JPanel acciones = new JPanel(new GridLayout(2, 2, 3, 3));
        acciones.setBackground(FONDO);
        JButton anular = crearBotonPOS("ANULAR ORDEN");
        anular.setForeground(VERDE);
        anular.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                cancelarOrden();
            }
        });
        JButton eliminar = crearBotonPOS("ELIMINAR");
        eliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                eliminarSeleccionado();
            }
        });
        JButton factura = crearBotonPOS("ÚLTIMA FACTURA");
        factura.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                mostrarUltimaFactura();
            }
        });
        JButton cobrar = crearBotonPOS("COBRAR");
        cobrar.setForeground(VERDE);
        cobrar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        cobrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                cobrarOrden();
            }
        });
        acciones.add(anular);
        acciones.add(eliminar);
        acciones.add(factura);
        acciones.add(cobrar);
        return acciones;
    }

    // Teclado utilizado para indicar la cantidad del siguiente producto.
    private JPanel crearTecladoNumerico() {
        JPanel contenedor = new JPanel(new BorderLayout(3, 3));
        contenedor.setBackground(FONDO);
        JPanel teclado = new JPanel(new GridLayout(4, 3, 3, 3));
        teclado.setBackground(FONDO);
        for (String tecla : new String[]{"7", "8", "9", "4", "5", "6", "1", "2", "3"}) {
            JButton boton = crearBotonNumero(tecla);
            final String teclaPresionada = tecla;
            boton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evento) {
                    pulsarNumero(Integer.parseInt(teclaPresionada));
                }
            });
            teclado.add(boton);
        }
        JButton limpiar = crearBotonNumero("LIMPIA");
        limpiar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        limpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                limpiarCantidad();
            }
        });
        JButton cero = crearBotonNumero("0");
        cero.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                pulsarNumero(0);
            }
        });
        JButton borrar = crearBotonNumero("BORRA");
        borrar.setForeground(MORADO);
        borrar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        borrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                borrarNumero();
            }
        });
        teclado.add(limpiar);
        teclado.add(cero);
        teclado.add(borrar);
        JButton atras = crearBotonPOS("ATRÁS");
        atras.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        atras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                mostrarCategoria(Categoria.COMBOS);
            }
        });
        contenedor.add(teclado, BorderLayout.CENTER);
        contenedor.add(atras, BorderLayout.SOUTH);
        return contenedor;
    }

    private JButton crearBotonNumero(String texto) {
        JButton boton = crearBotonPOS(texto);
        boton.setForeground(ROJO);
        boton.setFont(new Font(Font.SERIF, Font.BOLD, 22));
        boton.setPreferredSize(new Dimension(90, 42));
        return boton;
    }

    private void pulsarNumero(int numero) {
        if (!tecladoCantidad.agregarDigito(numero)) {
            mostrarAviso("Ingrese una cantidad válida entre 1 y 100.");
        }
        actualizarEtiquetaCantidad();
    }

    private void borrarNumero() {
        tecladoCantidad.borrarDigito();
        actualizarEtiquetaCantidad();
    }

    private void limpiarCantidad() {
        tecladoCantidad.limpiar();
        actualizarEtiquetaCantidad();
    }

    private int obtenerCantidadSeleccionada() {
        return tecladoCantidad.getCantidad();
    }

    private void actualizarEtiquetaCantidad() {
        etiquetaCantidad.setText(String.valueOf(obtenerCantidadSeleccionada()));
    }

    // Muestra los productos de la categoría seleccionada.
    private void mostrarCategoria(Categoria categoria) {
        categoriaActual = categoria;
        panelProductos.removeAll();
        if (categoria == Categoria.COMBOS) {
            List<Producto> opciones = sucursal.getOpcionesCombo();
            etiquetaCategoria.setText("ARMA TU COMBO — " + opciones.size() + " OPCIONES");
            for (Producto principal : opciones) panelProductos.add(crearTarjetaCombo(principal));
        } else {
            List<Producto> productos = sucursal.filtrarPorCategoria(categoria);
            etiquetaCategoria.setText(categoria.crearEtiqueta(productos.size()).toUpperCase());
            for (Producto producto : productos) panelProductos.add(crearTarjetaProducto(producto));
        }
        panelProductos.revalidate();
        panelProductos.repaint();
    }

    private JButton crearTarjetaCombo(Producto principal) {
        List<Complemento> complementos = obtenerComplementosDisponibles(principal);
        Complemento complementoEjemplo = complementos.get(0);
        Bebida bebidaEjemplo = sucursal.obtenerBebidas().get(0);
        double precio = calcularPrecioCombo(principal, complementoEjemplo, bebidaEjemplo);
        String texto = "<html><center><b>COMBO " + principal.getNombre().toUpperCase() + "</b><br>"
                + "<font color='#CD3720'>DESDE Q" + String.format("%.2f", precio) + "</font></center></html>";
        JButton tarjeta = crearTarjetaTexto(texto);
        tarjeta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                armarCombo(principal);
            }
        });
        return tarjeta;
    }

    // Suma el precio de los tres componentes y le resta el 12% del combo.
    private double calcularPrecioCombo(Producto principal, Complemento complemento, Bebida bebida) {
        double precioComponentes = principal.getPrecioBase() + complemento.getPrecioBase() + bebida.getPrecioBase();
        double descuento = precioComponentes * Combo.DESCUENTO_COMBO;
        double precioFinal = precioComponentes - descuento;
        return Math.round(precioFinal * 100.0) / 100.0;
    }

    private JButton crearTarjetaProducto(Producto producto) {
        String texto = "<html><center><b>" + producto.getNombre().toUpperCase() + "</b><br>"
                + "Q" + String.format("%.2f", producto.getPrecioBase())
                + "<br><small>Stock: " + producto.getStock() + "</small></center></html>";
        JButton tarjeta = crearTarjetaTexto(texto);
        if (producto.getNombre().equals("Nuggets")) {
            tarjeta.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evento) {
                    agregarNuggetsConAderezo(producto);
                }
            });
        } else {
            tarjeta.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evento) {
                    agregarProducto(producto);
                }
            });
        }
        return tarjeta;
    }

    // Pide el aderezo (reutiliza el mismo diálogo que usan los combos) y agrega los dos productos.
    private void agregarNuggetsConAderezo(Producto producto) {
        List<Producto> aderezos = new ArrayList<Producto>(sucursal.obtenerAderezos());
        Producto aderezo = seleccionarProducto("ELIJA EL ADEREZO", aderezos);
        if (aderezo == null) {
            return;
        }
        agregarProducto(producto);
        agregarProducto(aderezo);
    }

    private JButton crearTarjetaTexto(String texto) {
        JButton tarjeta = crearBotonPOS(texto);
        tarjeta.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        tarjeta.setPreferredSize(new Dimension(50, 80));
        return tarjeta;
    }

    // Permite elegir complemento y bebida para formar un combo.
    private void armarCombo(Producto principal) {
        List<Producto> complementos = new ArrayList<Producto>();
        complementos.addAll(obtenerComplementosDisponibles(principal));
        Producto complementoElegido = seleccionarProducto(
                "1 de 2 — ELIJA EL COMPLEMENTO", complementos);
        if (complementoElegido == null) return;
        Complemento complemento = (Complemento) complementoElegido;

        List<Producto> bebidas = new ArrayList<Producto>();
        bebidas.addAll(sucursal.obtenerBebidas());
        Producto bebidaElegida = seleccionarProducto("2 de 2 — ELIJA LA BEBIDA", bebidas);
        if (bebidaElegida == null) return;
        Bebida bebida = (Bebida) bebidaElegida;

        double precioCombo = calcularPrecioCombo(principal, complemento, bebida);
        double precioComponentes = principal.getPrecioBase() + complemento.getPrecioBase() + bebida.getPrecioBase();
        String resumen = String.format(
                "Producto: %s%nComplemento: %s%nBebida: %s%nCantidad: %d%n%n"
                        + "Precio de componentes: Q%.2f%nDescuento del combo: 12%%%nPrecio por combo: Q%.2f",
                principal.getNombre(), complemento.getNombre(), bebida.getNombre(),
                obtenerCantidadSeleccionada(), precioComponentes, precioCombo);
        int confirmar = JOptionPane.showConfirmDialog(this, resumen, "CONFIRMAR COMBO",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (confirmar != JOptionPane.OK_OPTION) return;

        int stockDisponible = Math.min(principal.getStock(), Math.min(complemento.getStock(), bebida.getStock()));
        contadorCombos = contadorCombos + 1;
        String codigo = "COMBO-" + contadorCombos;
        Combo combo = new Combo(codigo, "Combo " + principal.getNombre(), Categoria.COMBOS,
                precioCombo, stockDisponible, principal, complemento, bebida);
        agregarProducto(combo);
    }

    private List<Complemento> obtenerComplementosDisponibles(Producto principal) {
        List<Complemento> disponibles = new ArrayList<Complemento>();
        for (Complemento complemento : sucursal.obtenerComplementos()) {
            if (complemento != principal) {
                disponibles.add(complemento);
            }
        }
        return disponibles;
    }

    private Producto seleccionarProducto(String titulo, List<Producto> opciones) {
        seleccionEnDialogo = null;
        JDialog dialogo = new JDialog(this, titulo, true);
        dialogo.setLayout(new BorderLayout(5, 5));
        dialogo.getContentPane().setBackground(FONDO);
        JLabel encabezado = new JLabel(titulo, SwingConstants.CENTER);
        encabezado.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        encabezado.setBorder(BorderFactory.createEmptyBorder(10, 5, 8, 5));
        dialogo.add(encabezado, BorderLayout.NORTH);

        JPanel opcionesPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        opcionesPanel.setBackground(FONDO);
        opcionesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        for (Producto opcion : opciones) {
            JButton boton = crearTarjetaTexto("<html><center><b>" + opcion.getNombre().toUpperCase()
                    + "</b><br>Q" + String.format("%.2f", opcion.getPrecioBase()) + "</center></html>");
            final Producto opcionDelBoton = opcion;
            boton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evento) {
                    seleccionEnDialogo = opcionDelBoton;
                    dialogo.dispose();
                }
            });
            opcionesPanel.add(boton);
        }
        dialogo.add(new JScrollPane(opcionesPanel), BorderLayout.CENTER);
        JButton cancelar = crearBotonPOS("CANCELAR");
        cancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                dialogo.dispose();
            }
        });
        dialogo.add(cancelar, BorderLayout.SOUTH);
        dialogo.setSize(680, 350);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);

        return seleccionEnDialogo;
    }

    // Agrega el producto y después regresa la cantidad a uno.
    private void agregarProducto(Producto producto) {
        int cantidad = obtenerCantidadSeleccionada();
        if (!ordenActual.agregarProducto(producto, cantidad)) {
            mostrarAviso("No existe suficiente disponibilidad para agregar esa cantidad.");
            return;
        }
        limpiarCantidad();
        actualizarOrden();
        mostrarCategoria(categoriaActual);
    }

    private void eliminarSeleccionado() {
        int fila = tablaOrden.getSelectedRow();
        if (fila < 0) {
            mostrarAviso("Seleccione una fila de la orden.");
            return;
        }
        ordenActual.eliminarDetalle(fila);
        actualizarOrden();
        mostrarCategoria(categoriaActual);
    }

    // Procesa el método de pago y genera la factura.
    private void cobrarOrden() {
        if (ordenActual.getDetalles().isEmpty()) {
            mostrarAviso("Agregue al menos un producto antes de cobrar.");
            return;
        }
        String[] opciones = {"Efectivo", "Tarjeta", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(this,
                String.format("Total: Q%.2f%nSeleccione el método de pago.", ordenActual.calcularTotal()),
                "Cobrar orden #" + ordenActual.getNumero(), JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (seleccion < 0 || seleccion == 2) return;
        try {
            MetodoPago pago = seleccion == 0 ? solicitarEfectivo() : solicitarTarjeta();
            if (pago == null) return;
            if (!ordenActual.completarOrden(pago)) {
                mostrarAviso("Pago rechazado. Revise el monto o los datos.");
                return;
            }
            ultimaFactura = new Factura(ordenActual);
            mostrarFactura(ultimaFactura);
            ordenActual = new Orden();
            actualizarOrden();
            mostrarCategoria(categoriaActual);
        } catch (IllegalArgumentException error) {
            mostrarAviso(error.getMessage());
        }
    }

    private MetodoPago solicitarEfectivo() {
        String monto = JOptionPane.showInputDialog(this,
                String.format("Total: Q%.2f%nMonto recibido:", ordenActual.calcularTotal()),
                "Pago en efectivo", JOptionPane.QUESTION_MESSAGE);
        if (monto == null) return null;
        try {
            return new PagoEfectivo(Double.parseDouble(monto.trim()));
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Ingrese un monto numérico válido.");
        }
    }

    private MetodoPago solicitarTarjeta() {
        JTextField titular = new JTextField(20);
        JTextField numero = new JTextField(20);
        JPanel formulario = new JPanel(new GridLayout(2, 2, 8, 8));
        formulario.add(new JLabel("Titular:"));
        formulario.add(titular);
        formulario.add(new JLabel("16 dígitos de prueba:"));
        formulario.add(numero);
        int resultado = JOptionPane.showConfirmDialog(this, formulario,
                "Pago con tarjeta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        return resultado == JOptionPane.OK_OPTION
                ? new PagoTarjeta(titular.getText(), numero.getText()) : null;
    }

    private void cancelarOrden() {
        if (ordenActual.getDetalles().isEmpty()) {
            mostrarAviso("La orden está vacía.");
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Anular la orden y devolver los productos al inventario?",
                "Anular orden", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            int unidades = ordenActual.cancelarOrden();
            ordenActual = new Orden();
            actualizarOrden();
            mostrarCategoria(categoriaActual);
            JOptionPane.showMessageDialog(this, "Unidades devueltas: " + unidades);
        }
    }

    private void actualizarOrden() {
        modeloOrden.setRowCount(0);
        for (DetalleOrden detalle : ordenActual.getDetalles()) {
            modeloOrden.addRow(new Object[]{detalle.getCantidad(), detalle.getProducto().getNombre(),
                    String.format("Q%.2f", detalle.calcularSubtotal())});
        }
        etiquetaOrden.setText("ORDEN #" + ordenActual.getNumero());
        etiquetaSubtotal.setText(String.format("Subtotal: Q%.2f", ordenActual.calcularSubtotal()));
        etiquetaAhorro.setText(String.format("Ahorro combo 12%%: Q%.2f", ordenActual.calcularAhorroCombos()));
        etiquetaIva.setText(String.format("IVA (12%%): Q%.2f", ordenActual.calcularIVA()));
        etiquetaTotal.setText(String.format("TOTAL: Q%.2f", ordenActual.calcularTotal()));
    }

    private void mostrarUltimaFactura() {
        if (ultimaFactura == null) {
            mostrarAviso("Todavía no se ha generado ninguna factura.");
            return;
        }
        mostrarFactura(ultimaFactura);
    }

    private void mostrarFactura(Factura factura) {
        JTextArea area = new JTextArea(factura.construirComprobante(), 28, 60);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setCaretPosition(0);
        JOptionPane.showMessageDialog(this, new JScrollPane(area),
                "Factura " + factura.getNumero(), JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarImagenNegocio() {
        File archivo = new File("img/negocio.png");
        if (!archivo.isFile()) {
            mostrarAviso("No se encontró la imagen del negocio.");
            return;
        }
        Image imagen = new ImageIcon(archivo.getAbsolutePath()).getImage()
                .getScaledInstance(850, 500, Image.SCALE_SMOOTH);
        JOptionPane.showMessageDialog(this, new JLabel(new ImageIcon(imagen)),
                "Burger King Puerto Barrios", JOptionPane.PLAIN_MESSAGE);
    }

    private void iniciarReloj() {
        final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss");
        Timer reloj = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                etiquetaReloj.setText(LocalDateTime.now().format(formato));
            }
        });
        reloj.setInitialDelay(0);
        reloj.start();
    }

    private void cerrarSistema() {
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Desea cerrar el punto de venta?", "Salir", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            ordenActual.cancelarOrden();
            dispose();
        }
    }

    private JButton crearBotonPOS(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(BLANCO);
        boton.setForeground(TEXTO);
        boton.setFocusPainted(false);
        boton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return boton;
    }

    private JTable crearTablaOrden() {
        JTable tabla = new JTable(modeloOrden);
        tabla.setRowHeight(28);
        tabla.setBackground(AMARILLO);
        tabla.setForeground(Color.BLACK);
        tabla.setGridColor(Color.GRAY);
        tabla.setShowGrid(true);
        tabla.setSelectionBackground(new Color(180, 220, 255));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setOpaque(true);
        tabla.getTableHeader().setBackground(new Color(235, 235, 235));
        tabla.getTableHeader().setForeground(Color.BLACK);
        tabla.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        tabla.getColumnModel().getColumn(0).setPreferredWidth(42);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(235);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(90);
        return tabla;
    }

    private DefaultTableModel crearModeloNoEditable(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    private void mostrarAviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
