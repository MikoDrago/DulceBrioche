package dulcebrioche;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.geom.RoundRectangle2D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;

public class InterfazPerfil extends JFrame {

    private JLabel lblNombreUsuario;
    private JLabel lblCorreoUsuario;
    private JTable tablaCompletados;
    private DefaultTableModel modeloTabla;

    private final Color COLOR_FONDO_APP = new Color(251, 246, 240);
    private final Color COLOR_TARJETA = Color.WHITE;
    private final Color COLOR_TITULO = new Color(140, 45, 67);
    private final Color COLOR_BOTON_PRIMARIO = new Color(200, 98, 122);
    private final Color COLOR_BOTON_EDITAR = new Color(215, 140, 60); // Color cálido para edición
    private final Color COLOR_BOTON_ELIMINAR = new Color(190, 60, 60);
    private final Color COLOR_TEXTO = new Color(61, 43, 40);
    private final Color COLOR_BORDER_INPUT = new Color(230, 218, 210);

    public InterfazPerfil() {
        setTitle("Dulce Brioche - Perfil de Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(920, 640);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelBase = new JPanel(new BorderLayout());
        panelBase.setBackground(COLOR_FONDO_APP);
        panelBase.setBorder(new EmptyBorder(20, 25, 20, 25));

        // --- ENCABEZADO ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JButton btnMenu = crearBotonEstilizado("☰", COLOR_FONDO_APP, COLOR_TITULO, 42, 36);
        btnMenu.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
        btnMenu.addActionListener(e -> crearMenuNavegacion().show(btnMenu, 0, btnMenu.getHeight() + 5));

        JLabel lblTitulo = new JLabel("Mi Perfil de Usuario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TITULO);

        JPanel panelDerechaHeader = new JPanel();
        panelDerechaHeader.setLayout(new BoxLayout(panelDerechaHeader, BoxLayout.Y_AXIS));
        panelDerechaHeader.setOpaque(false);

        JPanel panelBotonesTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotonesTop.setOpaque(false);

        JButton btnCarrito = crearBotonEstilizado("Carrito", COLOR_BOTON_PRIMARIO, Color.WHITE, 100, 32);
        btnCarrito.addActionListener(e -> { new InterfazCarrito().setVisible(true); dispose(); });

        JButton btnHistorial = crearBotonEstilizado("Historial", new Color(230, 222, 215), COLOR_TEXTO, 100, 32);
        btnHistorial.addActionListener(e -> { new InterfazHistorialPedidos(false).setVisible(true); dispose(); });

        panelBotonesTop.add(btnCarrito);
        panelBotonesTop.add(btnHistorial);

        JLabel lblMarca = new JLabel("Dulce Brioche", SwingConstants.RIGHT);
        lblMarca.setFont(new Font("Georgia", Font.BOLD, 13));
        lblMarca.setForeground(COLOR_TITULO);
        lblMarca.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblSub = new JLabel("REPOSTERÍA FINA", SwingConstants.RIGHT);
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 8));
        lblSub.setForeground(new Color(160, 130, 120));
        lblSub.setAlignmentX(Component.RIGHT_ALIGNMENT);

        panelDerechaHeader.add(panelBotonesTop);
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

        // --- PANEL CENTRO ---
        JPanel panelCentro = new JPanel(new BorderLayout(0, 15));
        panelCentro.setOpaque(false);
        panelCentro.setBorder(new EmptyBorder(15, 0, 0, 0));

        // FICHA CLIENTE
        JPanel panelFichaCliente = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_TARJETA);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 16, 16);
                g2.setColor(COLOR_BORDER_INPUT);
                g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 16, 16);
                g2.dispose();
            }
        };
        panelFichaCliente.setOpaque(false);
        panelFichaCliente.setBorder(new EmptyBorder(12, 20, 12, 20));

        // Datos del cliente (Izquierda)
        JPanel pDatos = new JPanel();
        pDatos.setLayout(new BoxLayout(pDatos, BoxLayout.Y_AXIS));
        pDatos.setOpaque(false);

        lblNombreUsuario = new JLabel("Cliente: " + Sesion.nombreUsuario);
        lblNombreUsuario.setFont(new Font("Georgia", Font.BOLD, 15));
        lblNombreUsuario.setForeground(COLOR_TITULO);

        lblCorreoUsuario = new JLabel("Correo: " + Sesion.correo);
        lblCorreoUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCorreoUsuario.setForeground(COLOR_TEXTO);

        pDatos.add(lblNombreUsuario);
        pDatos.add(Box.createRigidArea(new Dimension(0, 4)));
        pDatos.add(lblCorreoUsuario);

        JPanel pBotonesDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        pBotonesDerecha.setOpaque(false);

        // Botones Eliminar y Editar
        JButton btnEditarDatos = crearBotonEstilizado("Editar Datos", COLOR_BOTON_EDITAR, Color.WHITE, 120, 32);
        JButton btnEliminarCuenta = crearBotonEstilizado("Eliminar cuenta", COLOR_BOTON_ELIMINAR, Color.WHITE, 130, 32);

        pBotonesDerecha.add(btnEditarDatos);
        pBotonesDerecha.add(btnEliminarCuenta);

        panelFichaCliente.add(pDatos, BorderLayout.WEST);
        panelFichaCliente.add(pBotonesDerecha, BorderLayout.EAST);

        panelCentro.add(panelFichaCliente, BorderLayout.NORTH);

        // Tabla de pedidos
        JPanel panelTablaContenedor = new JPanel(new BorderLayout());
        panelTablaContenedor.setOpaque(false);

        JLabel lblSubtituloTabla = new JLabel("Mis Pedidos:");
        lblSubtituloTabla.setFont(new Font("Georgia", Font.BOLD, 14));
        lblSubtituloTabla.setForeground(COLOR_TITULO);
        lblSubtituloTabla.setBorder(new EmptyBorder(0, 0, 8, 0));

        String[] columnas = { "No° Pedido", "Fecha de Compra", "Total", "Estatus" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaCompletados = new JTable(modeloTabla);
        tablaCompletados.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaCompletados.setRowHeight(28);
        tablaCompletados.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaCompletados.getTableHeader().setBackground(new Color(240, 230, 225));
        tablaCompletados.getTableHeader().setForeground(COLOR_TITULO);

        JScrollPane scrollTabla = new JScrollPane(tablaCompletados);
        scrollTabla.getViewport().setBackground(COLOR_TARJETA);
        scrollTabla.setBorder(BorderFactory.createLineBorder(COLOR_BORDER_INPUT));

        panelTablaContenedor.add(lblSubtituloTabla, BorderLayout.NORTH);
        panelTablaContenedor.add(scrollTabla, BorderLayout.CENTER);

        panelCentro.add(panelTablaContenedor, BorderLayout.CENTER);
        panelBase.add(panelCentro, BorderLayout.CENTER);

        cargarDatosCompletados();
        add(panelBase);
    }

    private void cargarDatosCompletados() {
        modeloTabla.setRowCount(0);
        try {
            Conecta bd = new Conecta();
            for (String[] fila : bd.obtenerPedidosPerfil(Sesion.idUsuario)) {
                modeloTabla.addRow(fila);
            }
            bd.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPopupMenu crearMenuNavegacion() {
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(COLOR_TARJETA);
        popup.setBorder(BorderFactory.createLineBorder(COLOR_BORDER_INPUT, 1));

        popup.add(crearItemMenu("Ver Catálogo", e -> { new InterfazProductos(false).setVisible(true); dispose(); }));
        popup.add(crearItemMenu("Mi Carrito", e -> { new InterfazCarrito().setVisible(true); dispose(); }));
        popup.add(crearItemMenu("Mis Pedidos e Historial", e -> { new InterfazHistorialPedidos(false).setVisible(true); dispose(); }));
        popup.addSeparator();
        popup.add(crearItemMenu("Cerrar Sesión", e -> { new InterfazLogin().setVisible(true); dispose(); }));

        return popup;
    }

    private JMenuItem crearItemMenu(String texto, java.awt.event.ActionListener accion) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(accion);
        return item;
    }

    private JButton crearBotonEstilizado(String texto, Color bg, Color fg, int ancho, int alto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bg.darker() : getModel().isRollover() ? bg.brighter() : bg);
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
        btn.setPreferredSize(new Dimension(ancho, alto));
        return btn;
    }

}