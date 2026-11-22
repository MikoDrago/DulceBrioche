package dulcebrioche;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class InterfazModificarCuenta extends JFrame {

    private JTextField txtNombre, txtApellidoP, txtApellidoM, txtTelefono;
    private JTextField txtCalle, txtNumExt, txtNumInt, txtColonia, txtCiudad, txtEstado, txtCP;
    private JTextField txtCorreo;
    private JPasswordField txtContrasena;

    private final Color COLOR_FONDO_APP = new Color(251, 246, 240);
    private final Color COLOR_TARJETA = Color.WHITE;
    private final Color COLOR_TITULO = new Color(140, 45, 67);
    private final Color COLOR_BOTON_PRIMARIO = new Color(200, 98, 122);
    private final Color COLOR_TEXTO = new Color(61, 43, 40);
    private final Color COLOR_BORDER_INPUT = new Color(230, 218, 210);



    public InterfazModificarCuenta() {
        
        Conecta con = new Conecta();
        con.LlenarDatosPersona();
        cargarDatosUsuario();
        setTitle("Dulce Brioche - Editar Datos de Cuenta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(860, 660);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelBase = new JPanel(new BorderLayout(0, 15));
        panelBase.setBackground(COLOR_FONDO_APP);
        panelBase.setBorder(new EmptyBorder(20, 30, 20, 30));

        // --- TARJETA PRINCIPAL ---
        JPanel tarjeta = new JPanel(new BorderLayout(0, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_TARJETA);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 18, 18);
                g2.setColor(COLOR_BORDER_INPUT);
                g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 18, 18);
                g2.dispose();
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setBorder(new EmptyBorder(20, 28, 20, 28));

        // Encabezado
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JButton btnVolver = crearBoton("Volver", new Color(230, 222, 215), COLOR_TEXTO, 90, 32);
        btnVolver.addActionListener(e -> { new InterfazPerfil().setVisible(true); dispose(); });

        JLabel lblTitulo = new JLabel("Editar Mis Datos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TITULO);

        JLabel lblMarca = new JLabel("<html><div style='text-align:right;'><b>Dulce Brioche</b><br><span style='font-size:8px;color:#A08278;'>REPOSTERÍA FINA</span></div></html>");
        lblMarca.setFont(new Font("Georgia", Font.PLAIN, 13));
        lblMarca.setForeground(COLOR_TITULO);

        header.add(btnVolver, BorderLayout.WEST);
        header.add(lblTitulo, BorderLayout.CENTER);
        header.add(lblMarca, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_BORDER_INPUT);

        JPanel headerCompleto = new JPanel(new BorderLayout(0, 10));
        headerCompleto.setOpaque(false);
        headerCompleto.add(header, BorderLayout.NORTH);
        headerCompleto.add(sep, BorderLayout.SOUTH);

        tarjeta.add(headerCompleto, BorderLayout.NORTH);

        // Formulario
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        // Sección 1: Datos Personales y Dirección
        JLabel lblSec1 = new JLabel("DATOS PERSONALES Y DIRECCIÓN DE ENTREGA");
        lblSec1.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblSec1.setForeground(COLOR_TITULO);
        lblSec1.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblSec1);
        form.add(Box.createRigidArea(new Dimension(0, 8)));

        txtNombre = crearInput();
        txtApellidoP = crearInput();
        txtApellidoM = crearInput();
        txtTelefono = crearInput();

        txtCalle = crearInput();
        txtNumExt = crearInput();
        txtNumInt = crearInput();
        txtColonia = crearInput();
        txtCiudad = crearInput();
        txtEstado = crearInput();
        txtCP = crearInput();

        form.add(crearFila(2, campo("Nombre(s)", txtNombre), campo("Apellido paterno", txtApellidoP)));
        form.add(Box.createRigidArea(new Dimension(0, 8)));
        form.add(crearFila(2, campo("Apellido materno (opcional)", txtApellidoM), campo("Número de Teléfono", txtTelefono)));
        form.add(Box.createRigidArea(new Dimension(0, 8)));

        // Datos de direccion
        JPanel filaDireccion = new JPanel(new BorderLayout(15, 0));
        filaDireccion.setOpaque(false);
        filaDireccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaDireccion.add(campo("Calle", txtCalle), BorderLayout.CENTER);
        JPanel pNums = crearFila(2, campo("N° Exterior", txtNumExt), campo("N° Interior", txtNumInt));
        pNums.setPreferredSize(new Dimension(360, 52));
        filaDireccion.add(pNums, BorderLayout.EAST);
        form.add(filaDireccion);
        form.add(Box.createRigidArea(new Dimension(0, 8)));

        form.add(crearFila(2, campo("Colonia", txtColonia), campo("Ciudad", txtCiudad)));
        form.add(Box.createRigidArea(new Dimension(0, 8)));
        form.add(crearFila(2, campo("Estado", txtEstado), campo("Código postal", txtCP)));
        form.add(Box.createRigidArea(new Dimension(0, 14)));

        // Sección 2: Datos de Acceso
        JLabel lblSec2 = new JLabel("DATOS DE ACCESO");
        lblSec2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblSec2.setForeground(COLOR_TITULO);
        lblSec2.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblSec2);
        form.add(Box.createRigidArea(new Dimension(0, 8)));

        txtCorreo = crearInput();
        txtContrasena = new JPasswordField();
        estilizarInput(txtContrasena);

        form.add(crearFila(2, campo("Correo electrónico", txtCorreo), campo("Nueva contraseña (dejar vacío para no cambiar)", txtContrasena)));

        tarjeta.add(form, BorderLayout.CENTER);

        // Botones inferiores
        JPanel pSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        pSur.setOpaque(false);

        JButton btnCancelar = crearBoton("Cancelar", new Color(230, 222, 215), COLOR_TEXTO, 120, 36);
        btnCancelar.addActionListener(e -> { new InterfazPerfil().setVisible(true); dispose(); });

        JButton btnGuardar = crearBoton("Guardar Cambios", COLOR_BOTON_PRIMARIO, Color.WHITE, 150, 36);
        btnGuardar.addActionListener(e -> guardarDatos());

        pSur.add(btnCancelar);
        pSur.add(btnGuardar);
        tarjeta.add(pSur, BorderLayout.SOUTH);

        panelBase.add(tarjeta, BorderLayout.CENTER);
        add(panelBase);

        cargarDatosUsuario();
    }

    private void guardarDatos()
{
    if(txtNombre.getText().trim().isEmpty()
       ||
       txtCorreo.getText().trim().isEmpty())
    {
        JOptionPane.showMessageDialog(
                this,
                "El nombre y correo son obligatorios.");

        return;
    }

    try
    {
        Persona p = new Persona();

        p.setNombre(txtNombre.getText());

        p.setApellidoPaterno(txtApellidoP.getText());

        p.setApellidoMaterno(txtApellidoM.getText());

        p.setTelefono(txtTelefono.getText());

        p.setCalle(txtCalle.getText());

        p.setNumeroExterior(txtNumExt.getText());

        p.setNumeroInterior(txtNumInt.getText());

        p.setColonia(txtColonia.getText());

        p.setCiudad(txtCiudad.getText());

        p.setEstado(txtEstado.getText());

        p.setCodigoPostal(txtCP.getText());

        Conecta con =new Conecta();

        boolean actualizado =con.actualizarCuenta(p, txtCorreo.getText(), new String(txtContrasena.getPassword()));

        con.desconectar();

        if(actualizado)
        {
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente");

            new InterfazPerfil().setVisible(true);

            dispose();
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();

        JOptionPane.showMessageDialog(this,"Error al actualizar la cuenta");
    }
}

    private JPanel crearFila(int cols, JPanel... paneles) {
        JPanel fila = new JPanel(new GridLayout(1, cols, 15, 0));
        fila.setOpaque(false);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (JPanel p : paneles) fila.add(p);
        return fila;
    }

    private JPanel campo(String etiqueta, JComponent input) {
        JPanel p = new JPanel(new BorderLayout(0, 3));
        p.setOpaque(false);
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(COLOR_TEXTO);
        p.add(lbl, BorderLayout.NORTH);
        p.add(input, BorderLayout.CENTER);
        return p;
    }

    private JTextField crearInput() {
        JTextField txt = new JTextField();
        estilizarInput(txt);
        return txt;
    }

    private void estilizarInput(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setForeground(COLOR_TEXTO);
        txt.setPreferredSize(new Dimension(100, 32));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER_INPUT),
            new EmptyBorder(3, 8, 3, 8)
        ));
    }

    private JButton crearBoton(String texto, Color bg, Color fg, int ancho, int alto) {
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
    private void cargarDatosUsuario()
{
    try
    {
        Conecta con = new Conecta();

        Persona p = con.LlenarDatosPersona();

        if(p != null)
        {
            txtNombre.setText(p.getNombre());
            txtApellidoP.setText(p.getApellidoPaterno());
            txtApellidoM.setText(p.getApellidoMaterno());
            txtTelefono.setText(p.getTelefono());
            txtCalle.setText(p.getCalle());
            txtNumExt.setText(p.getNumeroExterior());
            txtNumInt.setText(p.getNumeroInterior());
            txtColonia.setText(p.getColonia());
            txtCiudad.setText(p.getCiudad());
            txtEstado.setText(p.getEstado());
            txtCP.setText(p.getCodigoPostal());
        }

        txtCorreo.setText(con.obtenerCorreo());

        con.desconectar();
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}
}
