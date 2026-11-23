package dulcebrioche;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.nio.file.Files;

public class InterfazModificarProducto extends JFrame {

    private Producto producto;
    private JTextField txtNombre, txtPrecio, txtStock, txtDiasAnticipacion;
    private JComboBox<String> cmbCategoria;
    private JTextArea txtDescripcion;
    private JLabel lblImagenPreview;
    private byte[] imagenBytes = null;

    private final Color COLOR_FONDO_APP = new Color(251, 246, 240);
    private final Color COLOR_TARJETA = Color.WHITE;
    private final Color COLOR_TITULO = new Color(140, 45, 67);
    private final Color COLOR_BOTON_PRIMARIO = new Color(200, 98, 122);
    private final Color COLOR_TEXTO = new Color(61, 43, 40);
    private final Color COLOR_BORDER_INPUT = new Color(230, 218, 210);

    public InterfazModificarProducto(Producto p) {
        this.producto = p;

        setTitle("Dulce Brioche - Modificar Producto [ADMINISTRADOR]");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(760, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelBase = new JPanel(new BorderLayout(0, 15));
        panelBase.setBackground(COLOR_FONDO_APP);
        panelBase.setBorder(new EmptyBorder(20, 30, 20, 30));

        // --- CABECERA ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JLabel lblTitulo = new JLabel("Modificar Producto", SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TITULO);

        JButton btnVolver = crearBotonEstilizado("Volver", new Color(230, 222, 215), COLOR_TEXTO, 100, 32);
        btnVolver.addActionListener(e -> {
            new InterfazProductos(true).setVisible(true);
            dispose();
        });

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(btnVolver, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_BORDER_INPUT);

        JPanel panelHeaderCompleto = new JPanel(new BorderLayout());
        panelHeaderCompleto.setOpaque(false);
        panelHeaderCompleto.add(panelHeader, BorderLayout.NORTH);
        panelHeaderCompleto.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        panelHeaderCompleto.add(sep, BorderLayout.SOUTH);

        panelBase.add(panelHeaderCompleto, BorderLayout.NORTH);

        // Formulario
        JPanel tarjetaForm = new JPanel(new GridLayout(1, 2, 25, 0)) {
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
        tarjetaForm.setOpaque(false);
        tarjetaForm.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Parte izquierda (datos)
        JPanel colIzquierda = new JPanel();
        colIzquierda.setLayout(new BoxLayout(colIzquierda, BoxLayout.Y_AXIS));
        colIzquierda.setOpaque(false);

        txtNombre = crearCampoTexto(p != null ? p.NombreProducto : "");
        
        String[] categorias = {"Pasteles de Ocasión", "Galletas & Tartas", "Panadería Fina", "Postres Individuales", "Cupcakes & Muffins"};
        cmbCategoria = new JComboBox<>(categorias);
        cmbCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbCategoria.setBackground(Color.WHITE);
        cmbCategoria.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cmbCategoria.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (p != null && p.Categoria != null) {
            cmbCategoria.setSelectedItem(p.Categoria);
        }

        txtPrecio = crearCampoTexto(p != null ? String.valueOf(p.Precio) : "");
        txtStock = crearCampoTexto(p != null ? String.valueOf(p.Stock) : "");
        txtDiasAnticipacion = crearCampoTexto(p != null ? String.valueOf(p.DiasAnticipacion) : "");

        agregarCampo(colIzquierda, "Nombre del Producto", txtNombre);
        agregarCampo(colIzquierda, "Categoría", cmbCategoria);
        agregarCampo(colIzquierda, "Precio ($ MXN)", txtPrecio);
        agregarCampo(colIzquierda, "Existencias (Stock)", txtStock);
        agregarCampo(colIzquierda, "Días mínimos de anticipación", txtDiasAnticipacion);

        // Parte derecha (imagen)
        JPanel colDerecha = new JPanel();
        colDerecha.setLayout(new BoxLayout(colDerecha, BoxLayout.Y_AXIS));
        colDerecha.setOpaque(false);

        txtDescripcion = new JTextArea(p != null ? p.Descripcion : "", 4, 20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBorder(new EmptyBorder(6, 8, 6, 8));

        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBorder(BorderFactory.createLineBorder(COLOR_BORDER_INPUT));
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblImagenPreview = new JLabel("Sin imagen seleccionada", SwingConstants.CENTER);
        lblImagenPreview.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblImagenPreview.setForeground(new Color(150, 130, 120));
        lblImagenPreview.setPreferredSize(new Dimension(260, 140));
        lblImagenPreview.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        lblImagenPreview.setBorder(BorderFactory.createLineBorder(COLOR_BORDER_INPUT));
        lblImagenPreview.setAlignmentX(Component.LEFT_ALIGNMENT);
        cargarImagenActual();

        JButton btnCambiarImagen = crearBotonEstilizado("Seleccionar Imagen", new Color(230, 222, 215), COLOR_TEXTO, 150, 32);
        btnCambiarImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCambiarImagen.addActionListener(e -> seleccionarImagen());

        agregarCampo(colDerecha, "Descripción", scrollDesc);
        agregarCampo(colDerecha, "Fotografía del Producto", lblImagenPreview);
        colDerecha.add(btnCambiarImagen);

        tarjetaForm.add(colIzquierda);
        tarjetaForm.add(colDerecha);
        panelBase.add(tarjetaForm, BorderLayout.CENTER);

        // Boton Guardar
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelSur.setOpaque(false);

        JButton btnGuardar = crearBotonEstilizado("Guardar Cambios", COLOR_BOTON_PRIMARIO, Color.WHITE, 160, 38);
        btnGuardar.addActionListener(e -> guardarCambios());
        panelSur.add(btnGuardar);

        panelBase.add(panelSur, BorderLayout.SOUTH);
        add(panelBase);
    }

    private void agregarCampo(JPanel contenedor, String etiqueta, JComponent campo) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(COLOR_TEXTO);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        contenedor.add(lbl);
        contenedor.add(Box.createRigidArea(new Dimension(0, 4)));
        contenedor.add(campo);
        contenedor.add(Box.createRigidArea(new Dimension(0, 12)));
    }

    private JTextField crearCampoTexto(String textoInicial) {
        JTextField txt = new JTextField(textoInicial);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setForeground(COLOR_TEXTO);
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER_INPUT),
            new EmptyBorder(4, 8, 4, 8)
        ));
        return txt;
    }

    private void cargarImagenActual() {
        if (producto == null) return;
        try {
            Conecta bd = new Conecta();
            imagenBytes = bd.obtenerPrimeraImagen(producto.IdProducto);
            if (imagenBytes != null) {
                Image img = new ImageIcon(imagenBytes).getImage().getScaledInstance(240, 130, Image.SCALE_SMOOTH);
                lblImagenPreview.setText("");
                lblImagenPreview.setIcon(new ImageIcon(img));
            }
            bd.desconectar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void seleccionarImagen() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File archivo = chooser.getSelectedFile();
                imagenBytes = Files.readAllBytes(archivo.toPath());
                Image img = new ImageIcon(imagenBytes).getImage().getScaledInstance(240, 130, Image.SCALE_SMOOTH);
                lblImagenPreview.setText("");
                lblImagenPreview.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarCambios()
    {   
    if(txtNombre.getText().trim().isEmpty()
            ||
       txtPrecio.getText().trim().isEmpty())
    {
        JOptionPane.showMessageDialog(
                this,"Por favor completa los campos obligatorios.");
        return;
    }

    try
    {
        producto.setNombreProducto(txtNombre.getText());
        producto.setCategoria(cmbCategoria.getSelectedItem().toString());
        producto.setPrecio(Double.parseDouble(txtPrecio.getText()));
        producto.setStock(Integer.parseInt(txtStock.getText()));
        producto.setDiasAnticipacion(Integer.parseInt(txtDiasAnticipacion.getText()));
        producto.setDescripcion(txtDescripcion.getText());

        Conecta bd = new Conecta();

try
{
    bd.actualizarProducto(
            producto,
            imagenBytes);

    JOptionPane.showMessageDialog(
            this,
            "Producto actualizado correctamente");

    new InterfazProductos(true)
            .setVisible(true);

    dispose();
}
catch(RuntimeException e)
{
    JOptionPane.showMessageDialog(
            this,
            e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
}
finally
{
    bd.desconectar();
}

    }
    catch(NumberFormatException ex)
    {
        JOptionPane.showMessageDialog(
                this,"Precio, stock y días deben ser numéricos.");
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,"Error al actualizar el producto.");
    }

        if (txtNombre.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor completa los campos obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            double Precio = Double.parseDouble(txtPrecio.getText().trim());
            int Stock = Integer.parseInt(txtStock.getText().trim());
            int DiasAnticipacion = Integer.parseInt(txtDiasAnticipacion.getText().trim());

            JOptionPane.showMessageDialog(this, "¡Producto actualizado correctamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            new InterfazProductos(true).setVisible(true);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifica que Precio, Stock y Días sean números válidos.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(ancho, alto));
        return btn;
    }
}
