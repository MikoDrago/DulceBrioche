package dulcebrioche;

import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.JSeparator;
import javax.swing.SwingConstants;


public class InterfazAdminPanel extends JFrame {

    private final Color COLOR_FONDO_APP = new Color(251, 246, 240);
    private final Color COLOR_TARJETA = new Color(255, 255, 255);
    private final Color COLOR_TITULO = new Color(140, 45, 67);
    private final Color COLOR_BOTON_PRIMARIO = new Color(200, 98, 122);
    private final Color COLOR_TEXTO = new Color(61, 43, 40);
    private final Color COLOR_BORDER_INPUT = new Color(230, 218, 210);

    public InterfazAdminPanel() {
        setTitle("Dulce Brioche - Panel de Administración");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(880, 580);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelBase = new JPanel(new GridBagLayout());
        panelBase.setBackground(COLOR_FONDO_APP);

        JPanel panelTarjeta = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 20, 20);
                g2.setColor(COLOR_TARJETA);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 20, 20);
                g2.setColor(COLOR_BORDER_INPUT);
                g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 20, 20);
                g2.dispose();
            }
        };
        panelTarjeta.setOpaque(false);
        panelTarjeta.setPreferredSize(new Dimension(800, 500));
        panelTarjeta.setBorder(new EmptyBorder(15, 25, 20, 25));

        // Cabecera
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JButton btnMenu = crearBotonEstilizado("☰", COLOR_FONDO_APP, COLOR_TITULO, 42, 36);
        btnMenu.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
        
        JPopupMenu menuDesplegable = crearMenuNavegacion();
        btnMenu.addActionListener(e -> menuDesplegable.show(btnMenu, 0, btnMenu.getHeight() + 5));

        JLabel lblTitulo = new JLabel("Panel de Control Admin", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TITULO);

        JPanel panelDerechaHeader = new JPanel();
        panelDerechaHeader.setLayout(new BoxLayout(panelDerechaHeader, BoxLayout.Y_AXIS));
        panelDerechaHeader.setOpaque(false);

        JPanel panelBotonesTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotonesTop.setOpaque(false);

        JLabel lblMarca = new JLabel("Dulce Brioche", SwingConstants.RIGHT);
        lblMarca.setFont(new Font("Georgia", Font.BOLD, 14));
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

        panelTarjeta.add(panelHeaderCompleto, BorderLayout.NORTH);

        // Opciones principales
        JPanel gridOpciones = new JPanel(new GridLayout(1, 2, 25, 0));
        gridOpciones.setOpaque(false);
        gridOpciones.setBorder(new EmptyBorder(25, 10, 20, 10));

        gridOpciones.add(crearTarjetaBoton(
            "Ver Productos / Catálogo",
            "Consultar y revisar los postres registrados en la tienda.",
            e -> {
                new InterfazProductos(true).setVisible(true);
                dispose();
            }
        ));

        gridOpciones.add(crearTarjetaBoton(
            "Agregar Nuevo Producto",
            "Registrar un nuevo pastel o postre al catálogo de la tienda.",
            e -> {
                new InterfazAgregarProductos().setVisible(true);
                dispose();
            }
        ));

        panelTarjeta.add(gridOpciones, BorderLayout.CENTER);
        panelBase.add(panelTarjeta);
        add(panelBase);
    }

    private JPanel crearTarjetaBoton(String titulo, String descripcion, ActionListener accion) {
        TarjetaPanel card = new TarjetaPanel(titulo, descripcion);
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                accion.actionPerformed(null);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setHover(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setHover(false);
            }
        });
        return card;
    }

    private class TarjetaPanel extends JPanel {
        private boolean hover;

        public TarjetaPanel(String titulo, String descripcion) {
            setLayout(new GridBagLayout());
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            JPanel contenido = new JPanel();
            contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
            contenido.setOpaque(false);

            JLabel lblTitulo = new JLabel(titulo);
            lblTitulo.setFont(new Font("Georgia", Font.BOLD, 16));
            lblTitulo.setForeground(COLOR_TITULO);
            lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblDesc = new JLabel("<html><center>" + descripcion + "</center></html>", SwingConstants.CENTER);
            lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblDesc.setForeground(new Color(120, 100, 95));
            lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblDesc.setPreferredSize(new Dimension(240, 45));

            contenido.add(lblTitulo);
            contenido.add(Box.createRigidArea(new Dimension(0, 12)));
            contenido.add(lblDesc);

            add(contenido);
        }

        public void setHover(boolean hover) {
            this.hover = hover;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hover ? new Color(253, 248, 245) : COLOR_TARJETA);
            g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 16, 16);
            g2.setColor(hover ? COLOR_TITULO : COLOR_BORDER_INPUT);
            g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 16, 16);
            g2.dispose();
        }
    }

    private JPopupMenu crearMenuNavegacion() {
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(COLOR_TARJETA);
        popup.setBorder(BorderFactory.createLineBorder(COLOR_BORDER_INPUT, 1));

        JMenuItem itemProductos = new JMenuItem("Ver Productos");
        itemProductos.addActionListener(e -> { new InterfazProductos(true).setVisible(true); dispose(); });

        JMenuItem itemAgregar = new JMenuItem("Agregar Producto");
        itemAgregar.addActionListener(e -> { new InterfazAgregarProductos().setVisible(true); dispose(); });
        
        JMenuItem itemHistorial = new JMenuItem("Historial de Ventas");
        itemHistorial.addActionListener(e -> { new InterfazHistorialPedidos(true).setVisible(true); dispose(); });

        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        itemCerrarSesion.addActionListener(e -> { new InterfazLogin().setVisible(true); dispose(); });

        popup.add(itemProductos);
        popup.add(itemAgregar);
        popup.add(itemHistorial);
        popup.addSeparator();
        popup.add(itemCerrarSesion);

        return popup;
    }

    private JButton crearBotonEstilizado(String texto, Color bg, Color fg, int ancho, int alto) {
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
        btn.setPreferredSize(new Dimension(ancho, alto));
        return btn;
    }

}