package dulcebrioche;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import Resposteria.Conecta;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class InterfazCarrito extends JFrame {

    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;
    private List<ItemCarrito> carrito = new ArrayList<>();

    private final Color COLOR_FONDO_APP = new Color(251, 246, 240);
    private final Color COLOR_TARJETA = new Color(255, 255, 255);
    private final Color COLOR_TITULO = new Color(140, 45, 67);
    private final Color COLOR_BOTON_PRIMARIO = new Color(200, 98, 122);
    private final Color COLOR_TEXTO = new Color(61, 43, 40);
    private final Color COLOR_BORDER_INPUT = new Color(230, 218, 210);

    public InterfazCarrito() {
        setTitle("Dulce Brioche - Carrito de Compras");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(880, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelBase = new JPanel(new BorderLayout());
        panelBase.setBackground(COLOR_FONDO_APP);
        panelBase.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Header con Menú
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JButton btnMenu = crearBotonEstilizado("☰", COLOR_FONDO_APP, COLOR_TITULO);
        btnMenu.setPreferredSize(new Dimension(42, 36));
        btnMenu.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));

        JPopupMenu menuDesplegable = crearMenuNavegacion();
        btnMenu.addActionListener(e -> menuDesplegable.show(btnMenu, 0, btnMenu.getHeight() + 5));

        JLabel lblTitulo = new JLabel("Mi Carrito de Pedidos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TITULO);

        JButton btnVolver = crearBotonEstilizado("Catalogo", new Color(230, 222, 215), COLOR_TEXTO);
        btnVolver.addActionListener(e -> {
            new InterfazProductos().setVisible(true);
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

        // Tabla de ítems
        String[] columnas = {"Producto", "Precio Unitario", "Cantidad", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCarrito = new JTable(modeloTabla);
        tablaCarrito.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCarrito.setRowHeight(28);
        tablaCarrito.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaCarrito.getTableHeader().setBackground(new Color(240, 230, 225));
        tablaCarrito.getTableHeader().setForeground(COLOR_TITULO);

        JScrollPane scrollTabla = new JScrollPane(tablaCarrito);
        scrollTabla.getViewport().setBackground(COLOR_TARJETA);
        scrollTabla.setBorder(BorderFactory.createLineBorder(COLOR_BORDER_INPUT));

        panelBase.add(scrollTabla, BorderLayout.CENTER);

        // Panel Inferior para Total y Acciones
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.setBorder(new EmptyBorder(15, 0, 0, 0));

        lblTotal = new JLabel("Total: $0.00 MXN");
        lblTotal.setFont(new Font("Georgia", Font.BOLD, 18));
        lblTotal.setForeground(COLOR_TITULO);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelAcciones.setOpaque(false);

        JButton btnEliminar = crearBotonEstilizado("Eliminar", new Color(230, 222, 215), COLOR_TEXTO);
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        JButton btnComprar = crearBotonEstilizado("Confirmar Pedido", COLOR_BOTON_PRIMARIO, Color.WHITE);
        btnComprar.setPreferredSize(new Dimension(160, 36));
        btnComprar.addActionListener(e -> procesarCompra());

        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnComprar);

        panelInferior.add(lblTotal, BorderLayout.WEST);
        panelInferior.add(panelAcciones, BorderLayout.EAST);

        panelBase.add(panelInferior, BorderLayout.SOUTH);

        cargarCarritoBD();
        actualizarTabla();
        add(panelBase);
    }
    

private void cargarCarritoBD()
{
    try
    {
        Conecta bd = new Conecta();

        carrito = bd.obtenerCarrito(
                Sesion.idUsuario);

        bd.desconectar();
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        double total = 0;
        for (ItemCarrito item : carrito) {
            modeloTabla.addRow(new Object[]{
                item.Nombre,
                "$" + String.format("%.2f", item.Precio),
                item.Cantidad,
                "$" + String.format("%.2f", item.getSubtotal())
            });
            total += item.getSubtotal();
        }
        lblTotal.setText("Total: $" + String.format("%.2f", total) + " MXN");
    }

    private void procesarCompra()
    {
        if(carrito.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"El carrito está vacío.","Carrito Vacío",JOptionPane.WARNING_MESSAGE);

            return;
        }

        try
        {
            Conecta bd = new Conecta();
            double total = bd.obtenerTotalCarrito(Sesion.idUsuario);
            int idPedido = bd.generarPedido(Sesion.idUsuario);

            if(idPedido > 0)
            {
                List<ItemCarrito> detalle =bd.obtenerDetallePedido(idPedido);
                GenerarPDF.generarRecibo(idPedido,detalle,total);
                JOptionPane.showMessageDialog(this,"Pedido generado correctamente");
                cargarCarritoBD();
                actualizarTabla();
            }

            bd.desconectar();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private JPopupMenu crearMenuNavegacion() {
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(COLOR_TARJETA);
        popup.setBorder(BorderFactory.createLineBorder(COLOR_BORDER_INPUT, 1));

        JMenuItem itemPerfil = new JMenuItem("Mi Perfil");
        itemPerfil.addActionListener(e -> { new InterfazPerfil().setVisible(true); dispose(); });

        JMenuItem itemCatalogo = new JMenuItem("Ver Catálogo");
        itemCatalogo.addActionListener(e -> { new InterfazProductos(false).setVisible(true); dispose(); });

        popup.add(itemPerfil);
        popup.add(itemCatalogo);
        popup.addSeparator();

        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        itemCerrarSesion.addActionListener(e -> { new InterfazLogin().setVisible(true); dispose(); });
        popup.add(itemCerrarSesion);

        return popup;
    }
    private void eliminarSeleccionado()
    {
        int row = tablaCarrito.getSelectedRow();
        if(row < 0)
        {
            JOptionPane.showMessageDialog(this,"Selecciona un producto");
            return;
        }

        try
        {
            ItemCarrito item = carrito.get(row);
            Conecta bd = new Conecta();
            bd.eliminarProductoCarrito(item.IdDetalleCarrito);
            bd.desconectar();

            cargarCarritoBD();
            actualizarTabla();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 34));
        return btn;
    }

}