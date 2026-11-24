package dulcebrioche;

import javax.swing.border.EmptyBorder;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

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
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class InterfazAgregarProductos extends JFrame {

    private JTextField txtNombreProducto;
    private JComboBox<String> cbCategoria;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JTextField txtDiasAnticipacion;
    private JTextArea txtDescripcion;
    private JButton btnImagenes;
    private JLabel lblImagenes;

    private java.util.List<byte[]> imagenes =
        new java.util.ArrayList<>();

    private final Color COLOR_FONDO_APP = new Color(251, 246, 240); 
    private final Color COLOR_TARJETA = new Color(255, 255, 255);   
    private final Color COLOR_TITULO = new Color(140, 45, 67);      
    private final Color COLOR_TEXTO = new Color(61, 43, 40);        
    private final Color COLOR_BORDER_INPUT = new Color(230, 218, 210); 
    private final Color COLOR_INPUT_BG = new Color(253, 251, 249);  

    public InterfazAgregarProductos() {
        setTitle("Dulce Brioche - Panel Admin: Agregar Producto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(840, 560);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelBase = new JPanel(new GridBagLayout());
        panelBase.setBackground(COLOR_FONDO_APP);

        JPanel panelTarjeta = new JPanel() {
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
        panelTarjeta.setLayout(new BorderLayout());
        panelTarjeta.setOpaque(false);
        panelTarjeta.setPreferredSize(new Dimension(760, 480));
        panelTarjeta.setBorder(new EmptyBorder(15, 25, 20, 25));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JLabel lblTitulo = new JLabel("Agregar Nuevo Producto", SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TITULO);

        JLabel lblMarca = new JLabel("PANEL ADMINISTRADOR", SwingConstants.RIGHT);
        lblMarca.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblMarca.setForeground(new Color(160, 130, 120));

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(lblMarca, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_BORDER_INPUT);

        JPanel panelHeaderCompleto = new JPanel(new BorderLayout());
        panelHeaderCompleto.setOpaque(false);
        panelHeaderCompleto.add(panelHeader, BorderLayout.NORTH);
        panelHeaderCompleto.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        panelHeaderCompleto.add(sep, BorderLayout.SOUTH);

        panelTarjeta.add(panelHeaderCompleto, BorderLayout.NORTH);

        JPanel gridFormulario = new JPanel(new GridLayout(3, 2, 20, 10));
        gridFormulario.setOpaque(false);
        gridFormulario.setBorder(new EmptyBorder(15, 0, 10, 0));

        txtNombreProducto = new JTextField();
        cbCategoria = new JComboBox<>(new String[]{"Pasteles de Ocasión", "Postres Individuales", "Galletas & Tartas", "Edición Especial"});
        txtPrecio = new JTextField();
        txtStock = new JTextField();
        txtDiasAnticipacion = new JTextField();

        gridFormulario.add(crearContenedorCampo(txtNombreProducto, "Nombre del Producto:"));
        gridFormulario.add(crearContenedorCombo(cbCategoria, "Categoría:"));
        gridFormulario.add(crearContenedorCampo(txtPrecio, "Precio (MXN):"));
        gridFormulario.add(crearContenedorCampo(txtStock, "Stock Disponible (Unidades):"));
        gridFormulario.add(crearContenedorCampo(txtDiasAnticipacion, "Días Mínimos de Anticipación:"));

        JPanel panelDesc = new JPanel();
        panelDesc.setLayout(new BoxLayout(panelDesc, BoxLayout.Y_AXIS));
        panelDesc.setOpaque(false);

        JLabel lblDesc = new JLabel("Descripción Corta:");
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblDesc.setForeground(COLOR_TEXTO);

        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDescripcion.setForeground(COLOR_TEXTO);
        txtDescripcion.setBackground(COLOR_INPUT_BG);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBorder(BorderFactory.createCompoundBorder(
            new BorderRedondeado(COLOR_BORDER_INPUT, 8),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        panelDesc.add(lblDesc);
        panelDesc.add(Box.createRigidArea(new Dimension(0, 2)));
        panelDesc.add(scrollDesc);

        gridFormulario.add(panelDesc);
        
        JPanel panelImagenes = new JPanel();

        panelImagenes.setOpaque(false);

        btnImagenes = crearBotonEstilizado(
                "Agregar Imágenes",
                new Color(230,222,215),
                COLOR_TEXTO);

        lblImagenes =
                new JLabel(
                        "Sin imágenes seleccionadas");

        btnImagenes.addActionListener(
                e -> seleccionarImagenes());

        panelImagenes.add(btnImagenes);
        panelImagenes.add(lblImagenes);

        panelTarjeta.add(panelImagenes,
                         BorderLayout.WEST);

        panelTarjeta.add(gridFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelBotones.setOpaque(false);

        JButton btnRegresar = crearBotonEstilizado("Regresar al Panel", new Color(230, 222, 215), COLOR_TEXTO);
        btnRegresar.addActionListener(e -> {
            new InterfazAdminPanel().setVisible(true); // Abre el Panel de Control del Administrador
            dispose();                                // Cierra la ventana actual de Agregar Productos
        });

        JButton btnGuardar = crearBotonEstilizado("Guardar Producto", COLOR_TITULO, Color.WHITE);
        btnGuardar.addActionListener(e -> {

try
    {
        if(txtNombreProducto.getText().trim().isEmpty()|| txtPrecio.getText().trim().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Complete los campos obligatorios");

            return;
        }

        Producto p = new Producto();

        p.setNombreProducto(txtNombreProducto.getText());
        p.setCategoria(cbCategoria.getSelectedItem().toString());
        p.setPrecio(Double.parseDouble(txtPrecio.getText()));
        p.setStock(Integer.parseInt(txtStock.getText()));
        p.setDiasAnticipacion(Integer.parseInt(txtDiasAnticipacion.getText()));
        p.setDescripcion(txtDescripcion.getText());

        Conecta bd = new Conecta();

        bd.insertarProducto(p,imagenes);
        bd.desconectar();

        JOptionPane.showMessageDialog(null,"Producto registrado correctamente");

        new InterfazProductos(true).setVisible(true);
        dispose();
    }
    catch(NumberFormatException ex)
    {
        JOptionPane.showMessageDialog(
                null,
                "Precio, Stock y Días deben ser valores numéricos");
    }
    catch(Exception ex)
    {
        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                null,
                "Error al registrar el producto");
    }
});

        panelBotones.add(btnRegresar);
        panelBotones.add(btnGuardar);
        panelTarjeta.add(panelBotones, BorderLayout.SOUTH);

        panelBase.add(panelTarjeta);
        add(panelBase);
    }

    private JPanel crearContenedorCampo(JTextField campo, String etiqueta) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(COLOR_TEXTO);

        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setForeground(COLOR_TEXTO);
        campo.setBackground(COLOR_INPUT_BG);
        campo.setBorder(BorderFactory.createCompoundBorder(
            new BorderRedondeado(COLOR_BORDER_INPUT, 8),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        p.add(lbl);
        p.add(Box.createRigidArea(new Dimension(0, 2)));
        p.add(campo);
        return p;
    }
    private void seleccionarImagenes()
{
    JFileChooser selector =
            new JFileChooser();

    selector.setMultiSelectionEnabled(true);

    selector.setFileFilter(new FileNameExtensionFilter("Imágenes","jpg","jpeg"));

    int resultado = selector.showOpenDialog(this);

    if(resultado == JFileChooser.APPROVE_OPTION)
    {
        try
        {
            imagenes.clear();

            for(File archivo :selector.getSelectedFiles())
            {
                BufferedImage imagenOriginal =ImageIO.read(archivo);

                if(imagenOriginal == null)
                {
                    JOptionPane.showMessageDialog(this,"No se pudo leer la imagen:\n"+ archivo.getName());

                    continue;
                }

                ByteArrayOutputStream baos =new ByteArrayOutputStream();

                ImageIO.write(imagenOriginal,"jpg",baos);

                imagenes.add(
                        baos.toByteArray());
            }

            lblImagenes.setText(
                    imagenes.size()
                    + " imagen(es) seleccionada(s)");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Error al procesar imágenes:\n"
                    + ex.getMessage());
        }
    }
}

    private JPanel crearContenedorCombo(JComboBox<String> combo, String etiqueta) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(COLOR_TEXTO);

        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBackground(COLOR_INPUT_BG);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        p.add(lbl);
        p.add(Box.createRigidArea(new Dimension(0, 2)));
        p.add(combo);
        return p;
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
        btn.setPreferredSize(new Dimension(170, 36));
        return btn;
    }

    private boolean validarFormulario() {
        if (txtNombreProducto.getText().trim().isEmpty() || 
            txtPrecio.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, "Completa al menos el Nombre y Precio del producto.", "Campos obligatorios", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        txtNombreProducto.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtDiasAnticipacion.setText("");
        txtDescripcion.setText("");
    }

    private static class BorderRedondeado implements javax.swing.border.Border {
        private Color color;
        private int radio;
        public BorderRedondeado(Color color, int radio) { this.color = color; this.radio = radio; }
        public Insets getBorderInsets(Component c) { return new Insets(this.radio / 2, this.radio / 2, this.radio / 2, this.radio / 2); }
        public boolean isBorderOpaque() { return false; }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radio, radio);
            g2.dispose();
        }
    }

}