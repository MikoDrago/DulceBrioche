package dulcebrioche;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.geom.RoundRectangle2D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;

public class InterfazPedidos extends JFrame {

    private boolean esAdmin;
    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField txtFiltroBusqueda;

    private final Color COLOR_FONDO_APP = new Color(251, 246, 240);
    private final Color COLOR_TARJETA = new Color(255, 255, 255);
    private final Color COLOR_TITULO = new Color(140, 45, 67);
    private final Color COLOR_BOTON_PRIMARIO = new Color(200, 98, 122);
    private final Color COLOR_TEXTO = new Color(61, 43, 40);
    private final Color COLOR_BORDER_INPUT = new Color(230, 218, 210);

    public InterfazPedidos() {
        this(false);
    }

    public InterfazPedidos(boolean esAdmin) {
        this.esAdmin = esAdmin;

        setTitle("Dulce Brioche - Gestión de Pedidos Pendientes " + (esAdmin ? "[ADMINISTRADOR]" : "[CLIENTE]"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(920, 640);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelBase = new JPanel(new BorderLayout());
        panelBase.setBackground(COLOR_FONDO_APP);
        panelBase.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Cabecera con Menú ☰
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JButton btnMenu = crearBotonEstilizado("☰", COLOR_FONDO_APP, COLOR_TITULO);
        btnMenu.setPreferredSize(new Dimension(42, 36));
        btnMenu.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));

        JPopupMenu menuDesplegable = crearMenuNavegacion();
        btnMenu.addActionListener(e -> menuDesplegable.show(btnMenu, 0, btnMenu.getHeight() + 5));

        JLabel lblTitulo = new JLabel("Pedidos Pendientes & En Proceso", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TITULO);

        JButton btnVolver = crearBotonEstilizado("⬅ Volver", new Color(230, 222, 215), COLOR_TEXTO);
        btnVolver.addActionListener(e -> {
            if (esAdmin) new InterfazAdminPanel().setVisible(true);
            else new InterfazProductos(false).setVisible(true);
            dispose();
        });

        panelHeader.add(btnMenu, BorderLayout.WEST);
        panelHeader.add(lblTitulo, BorderLayout.CENTER);
        panelHeader.add(btnVolver, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_BORDER_INPUT);

        JPanel panelHeaderCompleto = new JPanel(new BorderLayout());
        panelHeaderCompleto.setOpaque(false);
        panelHeaderCompleto.add(panelHeader, BorderLayout.NORTH);
        panelHeaderCompleto.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        panelHeaderCompleto.add(sep, BorderLayout.SOUTH);

        panelBase.add(panelHeaderCompleto, BorderLayout.NORTH);

        // Panel Central (Pestañas + Filtros + Tabla)
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setOpaque(false);
        panelCentro.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Secciones / Pestañas de filtrado superior
        JPanel panelPestanas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelPestanas.setOpaque(false);

        JButton btnTabTodos = crearBotonEstilizado("Todos", COLOR_TITULO, Color.WHITE);
        JButton btnTabPorRealizar = crearBotonEstilizado("Por Realizar", new Color(230, 222, 215), COLOR_TEXTO);
        JButton btnTabEnviados = crearBotonEstilizado("Enviados", new Color(230, 222, 215), COLOR_TEXTO);

        btnTabTodos.addActionListener(e -> filtrarPorEstatus(""));
        btnTabPorRealizar.addActionListener(e -> filtrarPorEstatus("Por Realizar"));
        btnTabEnviados.addActionListener(e -> filtrarPorEstatus("Enviado"));

        panelPestanas.add(btnTabTodos);
        panelPestanas.add(btnTabPorRealizar);
        panelPestanas.add(btnTabEnviados);

        // Filtro de Búsqueda
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelFiltros.setOpaque(false);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBuscar.setForeground(COLOR_TEXTO);

        txtFiltroBusqueda = new JTextField(12);
        txtFiltroBusqueda.setPreferredSize(new Dimension(140, 30));

        JButton btnBuscar = crearBotonEstilizado("🔍", COLOR_BOTON_PRIMARIO, Color.WHITE);
        btnBuscar.setPreferredSize(new Dimension(42, 30));
        btnBuscar.addActionListener(e -> aplicarFiltroTexto());

        panelFiltros.add(lblBuscar);
        panelFiltros.add(txtFiltroBusqueda);
        panelFiltros.add(btnBuscar);

        JPanel panelBarraSuperior = new JPanel(new BorderLayout());
        panelBarraSuperior.setOpaque(false);
        panelBarraSuperior.add(panelPestanas, BorderLayout.WEST);
        panelBarraSuperior.add(panelFiltros, BorderLayout.EAST);

        panelCentro.add(panelBarraSuperior, BorderLayout.NORTH);

        // Tabla de Pedidos
        String[] columnas = {"ID Pedido", "Cliente", "Producto(s)", "Fecha Entrega", "Total", "Estatus"};
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

        // Panel Inferior con acciones
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelInferior.setOpaque(false);

        if (esAdmin) {
            JButton btnCambiarEstatus = crearBotonEstilizado("Actualizar Estatus", COLOR_BOTON_PRIMARIO, Color.WHITE);
            btnCambiarEstatus.setPreferredSize(new Dimension(170, 34));
            btnCambiarEstatus.addActionListener(e -> cambiarEstatusPedidoAdmin());
            panelInferior.add(btnCambiarEstatus);
        }

        panelCentro.add(panelInferior, BorderLayout.SOUTH);

        panelBase.add(panelCentro, BorderLayout.CENTER);

        cargarDatosEjemplo();
        add(panelBase);
    }

    private void cargarDatosEjemplo() {
        // Limpiamos la tabla para que no cargue filas quemadas en código
        modeloTabla.setRowCount(0);
    }

    private void aplicarFiltroTexto() {
        String texto = txtFiltroBusqueda.getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }

    private void filtrarPorEstatus(String estatus) {
        if (estatus.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + estatus, 5));
        }
    }

    private void cambiarEstatusPedidoAdmin() {
        int row = tablaPedidos.getSelectedRow();
        if (row >= 0) {
            int modelRow = tablaPedidos.convertRowIndexToModel(row);
            String[] opciones = {"Por Realizar", "Enviado", "Entregado (Mover a Historial)"};
            String seleccion = (String) JOptionPane.showInputDialog(
                this, "Selecciona el nuevo estatus:", "Actualizar Pedido",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]
            );

            if (seleccion != null) {
                if (seleccion.contains("Historial")) {
                    modeloTabla.removeRow(modelRow);
                    JOptionPane.showMessageDialog(this, "Pedido movido al Historial de Ventas.");
                } else {
                    modeloTabla.setValueAt(seleccion, modelRow, 5);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private JPopupMenu crearMenuNavegacion() {
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(COLOR_TARJETA);
        popup.setBorder(BorderFactory.createLineBorder(COLOR_BORDER_INPUT, 1));

        if (esAdmin) {
            JMenuItem itemAdmin = new JMenuItem("Panel de Administrador");
            itemAdmin.addActionListener(e -> { new InterfazAdminPanel().setVisible(true); dispose(); });

            JMenuItem itemHistorial = new JMenuItem("Historial de Ventas");
            itemHistorial.addActionListener(e -> { new InterfazHistorialPedidos(true).setVisible(true); dispose(); });

            popup.add(itemAdmin);
            popup.add(itemHistorial);
        } else {
            JMenuItem itemCatalogo = new JMenuItem("Ver Catálogo");
            itemCatalogo.addActionListener(e -> { new InterfazProductos(false).setVisible(true); dispose(); });

            JMenuItem itemHistorial = new JMenuItem("Mi Historial de Compras");
            itemHistorial.addActionListener(e -> { new InterfazHistorialPedidos(false).setVisible(true); dispose(); });

            popup.add(itemCatalogo);
            popup.add(itemHistorial);
        }

        popup.addSeparator();
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        itemCerrarSesion.addActionListener(e -> { new InterfazLogin().setVisible(true); dispose(); });
        popup.add(itemCerrarSesion);

        return popup;
    }

    private JButton crearBotonEstilizado(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(bg.darker());
                else if (getModel().isRollover()) g2.setColor(bg.brighter());
                else g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
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
        btn.setPreferredSize(new Dimension(120, 32));
        return btn;
    }


}