package dulcebrioche;

import javax.swing.border.EmptyBorder;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class InterfazProductos extends JFrame {

    private JPanel gridProductos;
    private List<Producto> listaProductos = new ArrayList<>();
    private boolean esAdmin;

    private final Color COLOR_FONDO_APP = new Color(251, 246, 240);
    private final Color COLOR_TARJETA = Color.WHITE;
    private final Color COLOR_TITULO = new Color(140, 45, 67);
    private final Color COLOR_BOTON_PRIMARIO = new Color(200, 98, 122);
    private final Color COLOR_BOTON_EDITAR = new Color(215, 140, 60);
    private final Color COLOR_BOTON_ELIMINAR = new Color(190, 60, 60);
    private final Color COLOR_TEXTO = new Color(61, 43, 40);
    private final Color COLOR_BORDER_INPUT = new Color(230, 218, 210);

    public InterfazProductos() {
        this(false);
    }

    public InterfazProductos(boolean esAdmin) {
        this.esAdmin = esAdmin;

        setTitle("Dulce Brioche - Catálogo de Productos " + (esAdmin ? "[ADMINISTRADOR]" : ""));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(880, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelBase = new JPanel(new BorderLayout());
        panelBase.setBackground(COLOR_FONDO_APP);
        panelBase.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Cabecera
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JButton btnMenu = crearBotonEstilizado("☰", COLOR_FONDO_APP, COLOR_TITULO);
        btnMenu.setPreferredSize(new Dimension(42, 36));
        btnMenu.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
        btnMenu.addActionListener(e -> crearMenuNavegacion().show(btnMenu, 0, btnMenu.getHeight() + 5));

        JLabel lblTitulo = new JLabel("Catálogo de Postres & Pasteles", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TITULO);

        JPanel panelBotonesDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotonesDerecha.setOpaque(false);

        if (esAdmin) {
            JButton btnVolver = crearBotonEstilizado("Panel", new Color(230, 222, 215), COLOR_TEXTO);
            btnVolver.addActionListener(e -> {
                new InterfazAdminPanel().setVisible(true);
                dispose();
            });
            panelBotonesDerecha.add(btnVolver);
        }

        panelHeader.add(btnMenu, BorderLayout.WEST);
        panelHeader.add(lblTitulo, BorderLayout.CENTER);
        panelHeader.add(panelBotonesDerecha, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_BORDER_INPUT);

        JPanel panelHeaderCompleto = new JPanel(new BorderLayout());
        panelHeaderCompleto.setOpaque(false);
        panelHeaderCompleto.add(panelHeader, BorderLayout.NORTH);
        panelHeaderCompleto.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        panelHeaderCompleto.add(sep, BorderLayout.SOUTH);

        panelBase.add(panelHeaderCompleto, BorderLayout.NORTH);

        // --- GRID PRODUCTOS ---
        gridProductos = new JPanel(new GridLayout(0, 2, 20, 20));
        gridProductos.setOpaque(false);
        gridProductos.setBorder(new EmptyBorder(15, 0, 0, 0));

        cargarProductosBD();
        cargarProductosEnPantalla();

        JScrollPane scrollGrid = new JScrollPane(gridProductos);
        scrollGrid.setOpaque(false);
        scrollGrid.getViewport().setOpaque(false);
        scrollGrid.setBorder(null);

        panelBase.add(scrollGrid, BorderLayout.CENTER);
        add(panelBase);
    }

    private JPopupMenu crearMenuNavegacion() {
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(COLOR_TARJETA);
        popup.setBorder(BorderFactory.createLineBorder(COLOR_BORDER_INPUT, 1));

        if (esAdmin) {
            popup.add(crearItemMenu("Panel de Administrador", e -> { new InterfazAdminPanel().setVisible(true); dispose(); }));
            popup.add(crearItemMenu("Agregar Producto", e -> { new InterfazAgregarProductos().setVisible(true); dispose(); }));
        } else {
            popup.add(crearItemMenu("Mi Perfil", e -> { new InterfazPerfil().setVisible(true); dispose(); }));
            popup.add(crearItemMenu("Ver Catálogo", e -> { new InterfazProductos(false).setVisible(true); dispose(); }));
            popup.add(crearItemMenu("Mi Carrito", e -> { new InterfazCarrito().setVisible(true); dispose(); }));
            popup.add(crearItemMenu("Historial", e -> { new InterfazHistorialPedidos(false).setVisible(true); dispose(); }));
        }

        popup.addSeparator();
        popup.add(crearItemMenu("Cerrar Sesión", e -> { new InterfazLogin().setVisible(true); dispose(); }));
        return popup;
    }

    private JMenuItem crearItemMenu(String texto, java.awt.event.ActionListener accion) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(accion);
        return item;
    }

    private void cargarProductosEnPantalla() {
        gridProductos.removeAll();
        for (Producto p : listaProductos) {
            gridProductos.add(crearTarjetaProducto(p));
        }
        gridProductos.revalidate();
        gridProductos.repaint();
    }

    private JPanel crearTarjetaProducto(Producto p) {
        JPanel tarjeta = new JPanel(new BorderLayout()) {
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
        tarjeta.setOpaque(false);
        tarjeta.setBorder(new EmptyBorder(12, 15, 12, 15));

        // Imagen
        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(180, 120));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        cargarImagenProducto(p.IdProducto, lblImagen);
        tarjeta.add(lblImagen, BorderLayout.NORTH);

        // Información
        JPanel pInfo = new JPanel();
        pInfo.setLayout(new BoxLayout(pInfo, BoxLayout.Y_AXIS));
        pInfo.setOpaque(false);

        JLabel lblCategoria = new JLabel(p.Categoria.toUpperCase());
        lblCategoria.setFont(new Font("Segoe UI", Font.BOLD, 9));
        lblCategoria.setForeground(new Color(170, 110, 80));

        JLabel lblNombre = new JLabel(p.NombreProducto);
        lblNombre.setFont(new Font("Georgia", Font.BOLD, 15));
        lblNombre.setForeground(COLOR_TITULO);

        JLabel lblPrecio = new JLabel("$" + String.format("%.2f", p.Precio) + " MXN");
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPrecio.setForeground(COLOR_TEXTO);

        pInfo.add(lblCategoria);
        pInfo.add(Box.createRigidArea(new Dimension(0, 4)));
        pInfo.add(lblNombre);
        pInfo.add(Box.createRigidArea(new Dimension(0, 4)));
        pInfo.add(lblPrecio);

        // Botones
        JPanel pBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        pBotones.setOpaque(false);

        JButton btnDetalle = crearBotonEstilizado("Detalle", new Color(230, 222, 215), COLOR_TEXTO);
        btnDetalle.setPreferredSize(new Dimension(75, 30));
        btnDetalle.addActionListener(e -> mostrarDetalleProducto(p));
        pBotones.add(btnDetalle);

        if (esAdmin) {
            JButton btnEditar = crearBotonEstilizado("Editar", COLOR_BOTON_EDITAR, Color.WHITE);
            btnEditar.setPreferredSize(new Dimension(70, 30));
            btnEditar.addActionListener(e -> {
                new InterfazModificarProduct(p).setVisible(true);
                dispose();
            });
            pBotones.add(btnEditar);

            JButton btnEliminar = crearBotonEstilizado("Eliminar", COLOR_BOTON_ELIMINAR, Color.WHITE);
            btnEliminar.setPreferredSize(new Dimension(80, 30));
            btnEliminar.addActionListener(e -> accionEliminarProducto(p));
            pBotones.add(btnEliminar);
        } else {
            JButton btnAgregar = crearBotonEstilizado("+ Carrito", COLOR_BOTON_PRIMARIO, Color.WHITE);
            btnAgregar.setPreferredSize(new Dimension(95, 30));
            btnAgregar.addActionListener(e -> agregarAlCarrito(p));
            pBotones.add(btnAgregar);
        }

        tarjeta.add(pInfo, BorderLayout.CENTER);
        tarjeta.add(pBotones, BorderLayout.SOUTH);

        return tarjeta;
    }

    private void cargarImagenProducto(int idProducto, JLabel lblImagen) {
        try {
            Conecta bd = new Conecta();
            byte[] datos = bd.obtenerPrimeraImagen(idProducto);
            if (datos != null) {
                Image img = new ImageIcon(datos).getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH);
                lblImagen.setIcon(new ImageIcon(img));
            }
            bd.desconectar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void agregarAlCarrito(Producto p) {
        try {
            Conecta bd = new Conecta();
            bd.agregarProductoCarrito(Sesion.idUsuario, p.IdProducto, 1);
            bd.desconectar();
            JOptionPane.showMessageDialog(this, "¡" + p.NombreProducto + " agregado al carrito!", "Carrito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar al carrito.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void accionEliminarProducto(Producto p) {
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de eliminar el producto '" + p.NombreProducto + "' del catálogo?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            Producto prod = new Producto();
            prod.setNombreProducto(p.NombreProducto);

            Conecta bd = new Conecta();
            boolean exito = bd.eliminarProducto(prod);
            bd.desconectar();

            if (exito) {
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente del catálogo.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarProductosBD();
                cargarProductosEnPantalla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDetalleProducto(Producto p) {
        JDialog dialog = new JDialog(this, "Detalle - " + p.NombreProducto, true);
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(this);

        JPanel pContent = new JPanel();
        pContent.setLayout(new BoxLayout(pContent, BoxLayout.Y_AXIS));
        pContent.setBackground(COLOR_FONDO_APP);
        pContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblNombre = new JLabel(p.NombreProducto);
        lblNombre.setFont(new Font("Georgia", Font.BOLD, 18));
        lblNombre.setForeground(COLOR_TITULO);

        JLabel lblCat = new JLabel("Categoría: " + p.Categoria);
        lblCat.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        JLabel lblPrecio = new JLabel("Precio: $" + String.format("%.2f", p.Precio));
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPrecio.setForeground(COLOR_TEXTO);

        JLabel lblStock = new JLabel("Existencias: " + p.Stock + " unidades.");
        lblStock.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblAnticipacion = new JLabel("Pedir con mínimo " + p.DiasAnticipacion + " días de anticipación.");
        lblAnticipacion.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JLabel lblDesc = new JLabel("<html><body style='width:320px;'>" + p.Descripcion + "</body></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        pContent.add(lblNombre);
        pContent.add(Box.createRigidArea(new Dimension(0, 5)));
        pContent.add(lblCat);
        pContent.add(Box.createRigidArea(new Dimension(0, 8)));
        pContent.add(lblPrecio);
        pContent.add(Box.createRigidArea(new Dimension(0, 3)));
        pContent.add(lblStock);
        pContent.add(Box.createRigidArea(new Dimension(0, 3)));
        pContent.add(lblAnticipacion);
        pContent.add(Box.createRigidArea(new Dimension(0, 10)));
        pContent.add(lblDesc);
        pContent.add(Box.createRigidArea(new Dimension(0, 15)));

        if (!esAdmin) {
            JButton btnAdd = crearBotonEstilizado("Agregar al Carrito", COLOR_TITULO, Color.WHITE);
            btnAdd.addActionListener(e -> {
                agregarAlCarrito(p);
                dialog.dispose();
            });
            pContent.add(btnAdd);
        }

        dialog.add(pContent);
        dialog.setVisible(true);
    }

    private JButton crearBotonEstilizado(String texto, Color bg, Color fg) {
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
        btn.setPreferredSize(new Dimension(110, 32));
        return btn;
    }

    private void cargarProductosBD() {
        try {
            Conecta bd = new Conecta();
            listaProductos = bd.consultarProductos();
            bd.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
