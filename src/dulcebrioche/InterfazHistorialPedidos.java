package dulcebrioche;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import javax.swing.*;


public class InterfazHistorialPedidos extends JFrame {

    private boolean esAdmin;
    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
    private JLabel lblTotal;
    private JTextField txtFiltroBusqueda;

    private final Color COLOR_FONDO_APP = new Color(251, 246, 240);
    private final Color COLOR_TARJETA = new Color(255, 255, 255);
    private final Color COLOR_TITULO = new Color(140, 45, 67);
    private final Color COLOR_BOTON_PRIMARIO = new Color(200, 98, 122);
    private final Color COLOR_TEXTO = new Color(61, 43, 40);
    private final Color COLOR_BORDER_INPUT = new Color(230, 218, 210);

    public InterfazHistorialPedidos() {
        this(false);
    }

    public InterfazHistorialPedidos(boolean esAdmin) {
        this.esAdmin = esAdmin;

        setTitle("Dulce Brioche - " + (esAdmin ? "Historial & Control de Ventas [ADMIN]" : "Mis Pedidos e Historial [CLIENTE]"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(920, 640);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelBase = new JPanel(new BorderLayout());
        panelBase.setBackground(COLOR_FONDO_APP);
        panelBase.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Cabecera
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        // Izquierda: Menu
        JButton btnMenu = botonEstilizado("☰", COLOR_FONDO_APP, COLOR_TITULO, 42, 36);
        btnMenu.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
        
        JPopupMenu menuDesplegable = crearMenuNavegacion();
        btnMenu.addActionListener(e -> menuDesplegable.show(btnMenu, 0, btnMenu.getHeight() + 5));

        // Centro: Título adaptado al rol
        JLabel lblTitulo = new JLabel(esAdmin ? "Gestión de Ventas & Pedidos" : "Mis Pedidos e Historial", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TITULO);

        // Derecha: Botón Volver + Marca Dulce Brioche
        JPanel panelDerechaHeader = new JPanel();
        panelDerechaHeader.setLayout(new BoxLayout(panelDerechaHeader, BoxLayout.Y_AXIS));
        panelDerechaHeader.setOpaque(false);

        JButton btnVolver = botonEstilizado("Volver", new Color(230, 222, 215), COLOR_TEXTO, 95, 32);
        btnVolver.addActionListener(e -> {
            if (esAdmin) new InterfazAdminPanel().setVisible(true);
            else new InterfazProductos(false).setVisible(true);
            dispose();
        });

        JLabel lblMarca = new JLabel("Dulce Brioche", SwingConstants.RIGHT);
        lblMarca.setFont(new Font("Georgia", Font.BOLD, 13));
        lblMarca.setForeground(COLOR_TITULO);
        lblMarca.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblSub = new JLabel("REPOSTERÍA FINA", SwingConstants.RIGHT);
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 8));
        lblSub.setForeground(new Color(160, 130, 120));
        lblSub.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JPanel pBtnBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pBtnBox.setOpaque(false);
        pBtnBox.add(btnVolver);

        panelDerechaHeader.add(pBtnBox);
        panelDerechaHeader.add(Box.createRigidArea(new Dimension(0, 4)));
        panelDerechaHeader.add(lblMarca);
        panelDerechaHeader.add(lblSub);

        panelHeader.add(btnMenu, BorderLayout.WEST);
        panelHeader.add(lblTitulo, BorderLayout.CENTER);
        panelHeader.add(panelDerechaHeader, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_BORDER_INPUT);

        JPanel panelHeaderCompleto = new JPanel(new BorderLayout());
        panelHeaderCompleto.setOpaque(false);
        panelHeaderCompleto.add(panelHeader, BorderLayout.NORTH);
        panelHeaderCompleto.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        panelHeaderCompleto.add(sep, BorderLayout.SOUTH);

        panelBase.add(panelHeaderCompleto, BorderLayout.NORTH);

        // Panel central (barra de pensanas y tabla)
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setOpaque(false);
        panelCentro.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Pestañas Superiores de Filtro
        JPanel panelPestanas = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelPestanas.setOpaque(false);

        JButton btnTabTodos = botonEstilizado("Todos", COLOR_TITULO, Color.WHITE, 90, 30);
        JButton btnTabPendientes = botonEstilizado("En Proceso", new Color(230, 222, 215), COLOR_TEXTO, 110, 30);
        JButton btnTabCompletados = botonEstilizado("Completados", new Color(230, 222, 215), COLOR_TEXTO, 120, 30);

        btnTabTodos.addActionListener(e -> filtrarPorEstatus(""));
        btnTabPendientes.addActionListener(e -> filtrarPorEstatus("Pendiente|Confirmado|Preparacion|Listo"));
        btnTabCompletados.addActionListener(e -> filtrarPorEstatus("Entregado"));

        panelPestanas.add(btnTabTodos);
        panelPestanas.add(btnTabPendientes);
        panelPestanas.add(btnTabCompletados);

        // Buscador por texto
        JPanel panelBuscador = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        panelBuscador.setOpaque(false);

        JPanel panelBarraFiltros = new JPanel(new BorderLayout());
        panelBarraFiltros.setOpaque(false);
        panelBarraFiltros.add(panelPestanas, BorderLayout.WEST);
        panelBarraFiltros.add(panelBuscador, BorderLayout.EAST);

        panelCentro.add(panelBarraFiltros, BorderLayout.NORTH);

        // Encabezados de la tabla
        String[] columnas = new String[]{"ID Pedido","Cliente","Producto(s)","Fecha Entrega","Total","Estatus"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaPedidos = new JTable(modeloTabla);
        tablaPedidos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaPedidos.setRowHeight(28);
        tablaPedidos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaPedidos.getTableHeader().setBackground(new Color(240, 230, 225));
        tablaPedidos.getTableHeader().setForeground(COLOR_TITULO);

        sorter = new TableRowSorter<>(modeloTabla);
        tablaPedidos.setRowSorter(sorter);

        JScrollPane scrollTabla = new JScrollPane(tablaPedidos);
        scrollTabla.getViewport().setBackground(COLOR_TARJETA);
        scrollTabla.setBorder(BorderFactory.createLineBorder(COLOR_BORDER_INPUT));

        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        // Panel Inferior 
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.setBorder(new EmptyBorder(12, 0, 0, 0));

        lblTotal = new JLabel(esAdmin ? "Total Acumulado en Ventas: $0.00 MXN" : "Sin pedidos registrados.");
        lblTotal.setFont(new Font("Georgia", Font.BOLD, 15));
        lblTotal.setForeground(COLOR_TITULO);

        panelInferior.add(lblTotal, BorderLayout.WEST);

        // Cambio de estatus exclusivo para administrador
        if (esAdmin) {
            JButton btnImprimirVentas = botonEstilizado("Imprimir Ventas", new Color(230, 222, 215), COLOR_TEXTO, 150, 34);

            JButton btnCambiarEstatus = botonEstilizado("Cambiar Estatus", COLOR_BOTON_PRIMARIO, Color.WHITE, 150, 34);
            btnCambiarEstatus.addActionListener(e -> cambiarEstatusPedidoAdmin());

            JPanel panelBotonesAdmin = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            panelBotonesAdmin.setOpaque(false);
            panelBotonesAdmin.add(btnImprimirVentas);
            panelBotonesAdmin.add(btnCambiarEstatus);

            panelInferior.add(panelBotonesAdmin, BorderLayout.EAST);
        }

        panelCentro.add(panelInferior, BorderLayout.SOUTH);
        panelBase.add(panelCentro, BorderLayout.CENTER);

        cargarDatos();
        add(panelBase);
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);

        try {
            Conecta bd = new Conecta();
            if (esAdmin) {
                for (String[] filaBD : bd.obtenerTodosLosPedidos()) {
                    // Mapeo según el orden retornado por la consulta de Admin en BD
                    String id = filaBD.length > 0 ? filaBD[0] : "";
                    String cliente = filaBD.length > 1 ? filaBD[1] : "";
                    String fecha = filaBD.length > 2 ? filaBD[2] : "";
                    String estatus = filaBD.length > 3 ? filaBD[3] : "";
                    String total = filaBD.length > 4 ? filaBD[4] : "0.00";
                    String productos = filaBD.length > 5 ? filaBD[5] : "Postres";

                    // Insertar en la tabla con el orden exacto del encabezado visual
                    // [ID Pedido, Cliente, Producto(s), Fecha Entrega, Total, Estatus]
                    modeloTabla.addRow(new Object[]{id, cliente, productos, fecha, total, estatus});
                }
            } else {
                for (String[] filaBD : bd.obtenerPedidosCliente(Sesion.idUsuario)) {
                    // Según la imagen previa de Cliente:
                    // filaBD[0] -> ID
                    // filaBD[1] -> Fecha
                    // filaBD[2] -> Estatus
                    // filaBD[3] -> Total
                    String id = filaBD.length > 0 ? filaBD[0] : "";
                    String cliente = (Sesion.nombreUsuario != null && !Sesion.nombreUsuario.isEmpty()) 
                                     ? Sesion.nombreUsuario 
                                     : "Cliente";
                    String fecha = filaBD.length > 1 ? filaBD[1] : "";
                    String estatus = filaBD.length > 2 ? filaBD[2] : "";
                    String total = filaBD.length > 3 ? filaBD[3] : "0.00";

                    // Si la consulta del cliente en Conecta.java no devuelve la columna de productos (posición 4),
                    // se muestra un texto descriptivo o la posición 4 si existe en la BD
                    String productos = filaBD.length > 4 ? filaBD[4] : "Pedido #" + id;

                    // Asignación correcta a la tabla de 6 columnas
                    modeloTabla.addRow(new Object[]{id, cliente, productos, fecha, total, estatus});
                }
            }

            bd.desconectar();
            actualizarTotal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarTotal() {
        double total = 0;
        try {
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                Object val = modeloTabla.getValueAt(i, 4); // Columna Total (índice 4)
                if (val != null) {
                    String str = val.toString().replace("$", "").trim();
                    if (!str.isEmpty()) {
                        total += Double.parseDouble(str);
                    }
                }
            }
            if (esAdmin) {
                lblTotal.setText("Total Acumulado en Ventas: $" + String.format("%.2f", total) + " MXN");
            } else {
                lblTotal.setText("Total Comprado: $" + String.format("%.2f", total) + " MXN");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filtrarPorEstatus(String estatus) {
        if (estatus.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            // Columna 5 es 'Estatus'
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + estatus, 5));
        }
    }

    public void cambiarEstatusPedidoAdmin() {
        int row = tablaPedidos.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la tabla");
            return;
        }

        int modelRow = tablaPedidos.convertRowIndexToModel(row);
        int idPedido = Integer.parseInt(modeloTabla.getValueAt(modelRow, 0).toString());

        String[] opciones = {
            "Pendiente",
            "Confirmado",
            "Preparacion",
            "Listo",
            "Entregado",
            "Cancelado"
        };

        String seleccion = (String) JOptionPane.showInputDialog(this,"Selecciona el nuevo estatus:","Estado del Pedido",JOptionPane.QUESTION_MESSAGE,null, opciones, opciones[0]);
        if (seleccion == null) {
            return;
        }
        try {
            int idEstado = 1;
            switch (seleccion) {
                case "Pendiente": idEstado = 1; break;
                case "Confirmado": idEstado = 2; break;
                case "Preparacion": idEstado = 3; break;
                case "Listo": idEstado = 4; break;
                case "Entregado": idEstado = 5; break;
                case "Cancelado": idEstado = 6; break;
            }
            Conecta bd = new Conecta();
            bd.actualizarEstadoPedido(idPedido, idEstado);
            bd.desconectar();
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Estado actualizado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPopupMenu crearMenuNavegacion() {
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(COLOR_TARJETA);
        popup.setBorder(BorderFactory.createLineBorder(COLOR_BORDER_INPUT, 1));

        if (esAdmin) {
            JMenuItem itemAdmin = new JMenuItem("  🛠️  Panel de Administrador");
            itemAdmin.addActionListener(e -> { new InterfazAdminPanel().setVisible(true); dispose(); });

            JMenuItem itemProductos = new JMenuItem("  📋  Ver Productos");
            itemProductos.addActionListener(e -> { new InterfazProductos(true).setVisible(true); dispose(); });

            popup.add(itemAdmin);
            popup.add(itemProductos);
        } else {
            JMenuItem itemPerfil = new JMenuItem("  👤  Mi Perfil");
            itemPerfil.addActionListener(e -> { new InterfazPerfil().setVisible(true); dispose(); });

            JMenuItem itemCatalogo = new JMenuItem("  🎂  Ver Catálogo");
            itemCatalogo.addActionListener(e -> { new InterfazProductos(false).setVisible(true); dispose(); });

            JMenuItem itemCarrito = new JMenuItem("  🛒  Mi Carrito");
            itemCarrito.addActionListener(e -> { new InterfazCarrito().setVisible(true); dispose(); });

            popup.add(itemPerfil);
            popup.add(itemCatalogo);
            popup.add(itemCarrito);
        }
        popup.addSeparator();
        JMenuItem itemCerrarSesion = new JMenuItem("  🚪  Cerrar Sesión");
        itemCerrarSesion.addActionListener(e -> { new InterfazLogin().setVisible(true); dispose(); });
        popup.add(itemCerrarSesion);

        return popup;
    }

    private JButton botonEstilizado(String texto, Color bg, Color fg, int ancho, int alto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(bg.darker());
                else if (getModel().isRollover()) g2.setColor(bg.brighter());
                else g2.setColor(bg);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(ancho, alto));
        return btn;
    }
}