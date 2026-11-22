package dulcebrioche;

import javax.swing.border.EmptyBorder;
import java.awt.geom.RoundRectangle2D;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class InterfazRegistro extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenedorCards;

    // Datos personales
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;
    private JTextField txtTelefono;

    // Direccion
    private JTextField txtCalle;
    private JTextField txtNumeroExterior;
    private JTextField txtNumeroInterior;
    private JTextField txtColonia;
    private JTextField txtCiudad;
    private JTextField txtEstado;
    private JTextField txtCodigoPostal;

    // Inicio sesion
    private JTextField txtUsuario;
    private JTextField txtCorreo;

    private JPasswordField txtContrasena;
    private JPasswordField txtConfirmarContrasena;

    // Banderas para controlar la visibilidad de las contraseñas
    private boolean verContrasena = false;
    private boolean verConfirmarContrasena = false;

    private final Color COLOR_FONDO_APP = new Color(251, 246, 240);
    private final Color COLOR_TARJETA = new Color(255, 255, 255);
    private final Color COLOR_TITULO = new Color(140, 45, 67);
    private final Color COLOR_BOTON_PRIMARIO = new Color(200, 98, 122);
    private final Color COLOR_TEXTO = new Color(61, 43, 40);
    private final Color COLOR_BORDER_INPUT = new Color(230, 218, 210);
    private final Color COLOR_INPUT_BG = new Color(253, 251, 249);

    public InterfazRegistro() {
        setTitle("Dulce Brioche - Registro de Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(840, 690);
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
        panelTarjeta.setPreferredSize(new Dimension(760, 610));
        panelTarjeta.setBorder(new EmptyBorder(15, 25, 20, 25));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JButton btnMenu = crearBotonEstilizado("☰", COLOR_FONDO_APP, COLOR_TITULO);
        btnMenu.setPreferredSize(new Dimension(42, 36));
        btnMenu.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
        
        JPopupMenu menuDesplegable = crearMenuNavegacion();
        btnMenu.addActionListener(e -> menuDesplegable.show(btnMenu, 0, btnMenu.getHeight() + 5));

        JLabel lblTituloRegistro = new JLabel("Registro", SwingConstants.CENTER);
        lblTituloRegistro.setFont(new Font("Georgia", Font.BOLD, 22));
        lblTituloRegistro.setForeground(COLOR_TITULO);

        JPanel panelMarcaDer = new JPanel();
        panelMarcaDer.setLayout(new BoxLayout(panelMarcaDer, BoxLayout.Y_AXIS));
        panelMarcaDer.setOpaque(false);

        JLabel lblMarca = new JLabel("Dulce Brioche", SwingConstants.RIGHT);
        lblMarca.setFont(new Font("Georgia", Font.BOLD, 16));
        lblMarca.setForeground(COLOR_TITULO);
        lblMarca.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblSub = new JLabel("REPOSTERÍA FINA", SwingConstants.RIGHT);
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 8));
        lblSub.setForeground(new Color(160, 130, 120));
        lblSub.setAlignmentX(Component.RIGHT_ALIGNMENT);

        panelMarcaDer.add(lblMarca);
        panelMarcaDer.add(lblSub);

        panelHeader.add(btnMenu, BorderLayout.WEST);
        panelHeader.add(lblTituloRegistro, BorderLayout.CENTER);
        panelHeader.add(panelMarcaDer, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_BORDER_INPUT);

        JPanel panelHeaderCompleto = new JPanel(new BorderLayout());
        panelHeaderCompleto.setOpaque(false);
        panelHeaderCompleto.add(panelHeader, BorderLayout.NORTH);
        panelHeaderCompleto.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        panelHeaderCompleto.add(sep, BorderLayout.SOUTH);

        panelTarjeta.add(panelHeaderCompleto, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        panelContenedorCards = new JPanel(cardLayout);
        panelContenedorCards.setOpaque(false);

        panelContenedorCards.add(crearPanelPaso1Horizontal(), "Paso1");
        panelContenedorCards.add(crearPanelPaso2Horizontal(), "Paso2");

        panelTarjeta.add(panelContenedorCards, BorderLayout.CENTER);

        panelBase.add(panelTarjeta);
        add(panelBase);
    }

    private JPopupMenu crearMenuNavegacion() {
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(COLOR_TARJETA);
        popup.setBorder(BorderFactory.createLineBorder(COLOR_BORDER_INPUT, 1));

        JLabel lblSeccion1 = new JLabel(" NAVEGACIÓN");
        lblSeccion1.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblSeccion1.setForeground(new Color(150, 120, 110));
        popup.add(lblSeccion1);
        popup.addSeparator();

        JMenuItem itemProductos = new JMenuItem("Ver Catálogo de Productos");
        itemProductos.addActionListener(e -> new InterfazProductos().setVisible(true));

        JMenuItem itemLogin = new JMenuItem("  🔑  Iniciar sesión");
        itemLogin.addActionListener(e -> {
            new InterfazLogin().setVisible(true);
            dispose();
        });

        JMenuItem itemRegistro = new JMenuItem("Registrarse");

        popup.add(itemProductos);
        popup.add(itemLogin);
        popup.add(itemRegistro);

        return popup;
    }

    private JPanel crearPanelPaso1Horizontal() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(12, 0, 0, 0));

        JLabel lblPaso = new JLabel("PASO 1: DATOS PERSONALES");
        lblPaso.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPaso.setForeground(COLOR_TITULO);
        panel.add(lblPaso, BorderLayout.NORTH);

        JPanel gridCampos = new JPanel(new GridLayout(6, 2, 20, 10));
        gridCampos.setOpaque(false);
        gridCampos.setBorder(new EmptyBorder(12, 0, 12, 0));

        txtNombre = new JTextField();
        txtTelefono = new JTextField();
        txtCalle = new JTextField();
        txtApellidoPaterno = new JTextField();
        txtApellidoMaterno = new JTextField();
        txtColonia = new JTextField();
        txtCiudad = new JTextField();
        txtEstado = new JTextField();
        txtCodigoPostal = new JTextField();

        gridCampos.add(crearContenedorCampo(txtNombre, "Nombre(s)"));
        gridCampos.add(crearContenedorCampo(txtApellidoPaterno, "Apellido paterno"));
        gridCampos.add(crearContenedorCampo(txtApellidoMaterno, "Apellido materno (opcional)"));
        gridCampos.add(crearContenedorCampo(txtTelefono, "Número de Teléfono"));
        gridCampos.add(crearContenedorCampo(txtCalle, "Calle"));

        JPanel panelNumeros = new JPanel(new GridLayout(1, 2, 10, 0));
        panelNumeros.setOpaque(false);
        txtNumeroExterior = new JTextField();
        txtNumeroInterior = new JTextField();
        
        panelNumeros.add(crearContenedorCampo(txtNumeroExterior, "N° Exterior"));
        panelNumeros.add(crearContenedorCampo(txtNumeroInterior, "N° Interior"));

        gridCampos.add(panelNumeros);
        gridCampos.add(crearContenedorCampo(txtColonia, "Colonia"));
        gridCampos.add(crearContenedorCampo(txtCiudad, "Ciudad"));
        gridCampos.add(crearContenedorCampo(txtEstado, "Estado"));
        gridCampos.add(crearContenedorCampo(txtCodigoPostal, "Código postal"));

        panel.add(gridCampos, BorderLayout.CENTER);

        JButton btnSiguiente = crearBotonEstilizado("Siguiente paso", COLOR_BOTON_PRIMARIO, Color.WHITE);
        btnSiguiente.addActionListener(e -> {
            if (validarCamposPaso1()) {
                cardLayout.show(panelContenedorCards, "Paso2");
            }
        });
        
        JButton btnIniciarSesion = crearBotonEstilizado("IniciarSesion", COLOR_BOTON_PRIMARIO,Color.WHITE);
        btnIniciarSesion.addActionListener(e -> {
            new InterfazLogin().setVisible(true);
            dispose();
        });

        JPanel pBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pBoton.setOpaque(false);
        pBoton.add(btnIniciarSesion);
        pBoton.add(btnSiguiente);
        panel.add(pBoton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelPaso2Horizontal() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(12, 0, 0, 0));

        JLabel lblPaso = new JLabel("PASO 2: CREAR CUENTA DE USUARIO");
        lblPaso.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPaso.setForeground(COLOR_TITULO);
        panel.add(lblPaso, BorderLayout.NORTH);

        JPanel gridCampos = new JPanel(new GridLayout(2, 2, 20, 15));
        gridCampos.setOpaque(false);
        gridCampos.setBorder(new EmptyBorder(15, 0, 15, 0));

        txtUsuario = new JTextField();
        txtCorreo = new JTextField();
        txtContrasena = new JPasswordField();
        txtConfirmarContrasena = new JPasswordField();

        gridCampos.add(crearContenedorCampo(txtUsuario, "Nombre de Usuario"));
        gridCampos.add(crearContenedorCampo(txtCorreo, "Correo Electrónico"));
        
        // Campos de Contraseña con botón de Ver/Ocultar
        gridCampos.add(crearContenedorPassword(txtContrasena, "Contraseña", 1));
        gridCampos.add(crearContenedorPassword(txtConfirmarContrasena, "Confirmar Contraseña", 2));

        panel.add(gridCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelBotones.setOpaque(false);

        JButton btnAtras = crearBotonEstilizado("Regresar", new Color(230, 222, 215), COLOR_TEXTO);
        btnAtras.addActionListener(e -> cardLayout.show(panelContenedorCards, "Paso1"));

        JButton btnGuardar = crearBotonEstilizado("Guardar Registro", COLOR_TITULO, Color.WHITE);
        btnGuardar.addActionListener(e -> {

            if(validarCamposPaso2())
            {
                try
                {
                    Conecta bd = new Conecta();

                    Persona p = new Persona();

                    p.setNombre(txtNombre.getText());
                    p.setApellidoPaterno(txtApellidoPaterno.getText());
                    p.setApellidoMaterno(txtApellidoMaterno.getText());
                    p.setTelefono(txtTelefono.getText());
                    p.setCalle(txtCalle.getText());
                    p.setNumeroExterior(txtNumeroExterior.getText());
                    p.setNumeroInterior(txtNumeroInterior.getText());
                    p.setColonia(txtColonia.getText());
                    p.setCiudad(txtCiudad.getText());
                    p.setEstado(txtEstado.getText());

                    p.setCodigoPostal(txtCodigoPostal.getText());

                    int idPersona = bd.insertarPersona(p);

                    Usuario u = new Usuario();

                    u.setIdPersona(idPersona);
                    // Rol Cliente
                    u.setIdRol(2);
                    u.setUsuario(txtUsuario.getText());
                    u.setCorreo(txtCorreo.getText());
                    u.setContrasena(
                            new String(
                                    txtContrasena.getPassword()));

                    bd.insertarUsuario(u);

                    JOptionPane.showMessageDialog(
                            null,
                            "Registro completado correctamente");

                    bd.desconectar();

                }
                catch(Exception ex)
                {
                    ex.printStackTrace();

                    JOptionPane.showMessageDialog(
                            null,
                            "Error al registrar");
                }
            }
        });

        panelBotones.add(btnAtras);
        panelBotones.add(btnGuardar);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearContenedorCampo(JTextField campo, String etiqueta) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(COLOR_TEXTO);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setForeground(COLOR_TEXTO);
        campo.setCaretColor(COLOR_TITULO);
        campo.setBackground(COLOR_INPUT_BG);
        campo.setBorder(BorderFactory.createCompoundBorder(
            new BorderRedondeado(COLOR_BORDER_INPUT, 8),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(lbl);
        p.add(Box.createRigidArea(new Dimension(0, 2)));
        p.add(campo);

        return p;
    }

    //Método especializado para envolver la caja de contraseña con el botón de ojo (👁️)
    private JPanel crearContenedorPassword(JPasswordField campoPass, String etiqueta, int tipoCampo) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(COLOR_TEXTO);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        campoPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoPass.setForeground(COLOR_TEXTO);
        campoPass.setCaretColor(COLOR_TITULO);
        campoPass.setBackground(COLOR_INPUT_BG);
        campoPass.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        // Panel interno que combina el campo y el botón
        JPanel panelInputWrap = new JPanel(new BorderLayout());
        panelInputWrap.setBackground(COLOR_INPUT_BG);
        panelInputWrap.setBorder(new BorderRedondeado(COLOR_BORDER_INPUT, 8));
        panelInputWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        panelInputWrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnOjo = new JButton("👁");
        btnOjo.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 13));
        btnOjo.setForeground(COLOR_TITULO);
        btnOjo.setContentAreaFilled(false);
        btnOjo.setBorderPainted(false);
        btnOjo.setFocusPainted(false);
        btnOjo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOjo.setPreferredSize(new Dimension(35, 30));

        btnOjo.addActionListener(e -> {
            if (tipoCampo == 1) {
                verContrasena = !verContrasena;
                if (verContrasena) {
                    campoPass.setEchoChar((char) 0); //Muestra el texto plano
                    btnOjo.setText("🙈");
                } else {
                    campoPass.setEchoChar('•'); //Oculta la contraseña
                    btnOjo.setText("👁");
                }
            } else {
                verConfirmarContrasena = !verConfirmarContrasena;
                if (verConfirmarContrasena) {
                    campoPass.setEchoChar((char) 0);
                    btnOjo.setText("🙈");
                } else {
                    campoPass.setEchoChar('•');
                    btnOjo.setText("👁");
                }
            }
        });

        panelInputWrap.add(campoPass, BorderLayout.CENTER);
        panelInputWrap.add(btnOjo, BorderLayout.EAST);

        p.add(lbl);
        p.add(Box.createRigidArea(new Dimension(0, 2)));
        p.add(panelInputWrap);

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
        btn.setPreferredSize(new Dimension(150, 36));
        return btn;
    }

    private boolean validarCamposPaso1() {
        if (txtNombre.getText().trim().isEmpty() || txtApellidoPaterno.getText().trim().isEmpty() ||
            txtCalle.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor completa los campos principales (Nombre, Apellidos, Domicilio y Teléfono).", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarCamposPaso2() {
        String pass = new String(txtContrasena.getPassword()).trim();
        String confirmPass = new String(txtConfirmarContrasena.getPassword()).trim();

        if (txtUsuario.getText().trim().isEmpty() || txtCorreo.getText().trim().isEmpty() ||
            pass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor completa todos los datos de la cuenta.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        //Validación de coincidencia de contraseñas
        if (!pass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden. Por favor verifica.", "Contraseña incorrecta", JOptionPane.ERROR_MESSAGE);
            txtConfirmarContrasena.requestFocus();
            return false;
        }

        return true;
    }

    private static class BorderRedondeado implements javax.swing.border.Border {
        private Color color;
        private int radio;

        public BorderRedondeado(Color color, int radio) {
            this.color = color;
            this.radio = radio;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radio / 2, this.radio / 2, this.radio / 2, this.radio / 2);
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radio, radio);
            g2.dispose();
        }
    }
}