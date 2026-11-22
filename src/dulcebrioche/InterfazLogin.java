package dulcebrioche;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import Resposteria.Conecta;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class InterfazLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;

    private final Color COLOR_FONDO_APP = new Color(251, 246, 240);
    private final Color COLOR_TARJETA = new Color(255, 255, 255);
    private final Color COLOR_TITULO = new Color(140, 45, 67);
    private final Color COLOR_TEXTO = new Color(61, 43, 40);
    private final Color COLOR_BORDER_INPUT = new Color(230, 218, 210);
    private final Color COLOR_INPUT_BG = new Color(253, 251, 249);

    public InterfazLogin() {
        setTitle("Dulce Brioche - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 470);
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
        panelTarjeta.setLayout(new BoxLayout(panelTarjeta, BoxLayout.Y_AXIS));
        panelTarjeta.setOpaque(false);
        panelTarjeta.setPreferredSize(new Dimension(380, 380));
        panelTarjeta.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel lblTitulo = new JLabel("Dulce Brioche");
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TITULO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("INICIAR SESIÓN");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblSub.setForeground(new Color(160, 130, 120));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelTarjeta.add(lblTitulo);
        panelTarjeta.add(lblSub);
        panelTarjeta.add(Box.createRigidArea(new Dimension(0, 20)));

        txtUsuario = new JTextField();
        txtContrasena = new JPasswordField();

        panelTarjeta.add(crearContenedorCampo(txtUsuario, "Usuario / Correo"));
        panelTarjeta.add(Box.createRigidArea(new Dimension(0, 10)));
        panelTarjeta.add(crearContenedorCampo(txtContrasena, "Contraseña"));
        panelTarjeta.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnLogin = crearBotonEstilizado("Iniciar Sesión", COLOR_TITULO, Color.WHITE);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> {

            String user = txtUsuario.getText().trim();

            String pass = new String(txtContrasena.getPassword()).trim();

            if(user.isEmpty() || pass.isEmpty())
            {
                JOptionPane.showMessageDialog(this,"Por favor ingresa usuario y contraseña.","Campos vacíos",JOptionPane.WARNING_MESSAGE);
                return;
            }

            try
            {
                Conecta bd = new Conecta();

                boolean acceso = bd.iniciarSesion(user,pass);

                bd.desconectar();

                if(acceso)
                {
                    JOptionPane.showMessageDialog(this,"Bienvenido "+ Sesion.nombreUsuario);

                    if(Sesion.idRol == 1)
                    {
                        new InterfazAdminPanel().setVisible(true);
                    }
                    else if(Sesion.idRol == 2)
                    {
                        new InterfazProductos(false).setVisible(true);
                    }

                    dispose();
                }       
                else
                {
                    JOptionPane.showMessageDialog(
                            this,
                            "Usuario o contraseña incorrectos",
                            "Acceso denegado",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        });

        //Botón para redirigir al Registro si no tiene cuenta
        JButton btnIrRegistro = new JButton("¿No tienes cuenta? Regístrate aquí");
        btnIrRegistro.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnIrRegistro.setForeground(COLOR_TITULO);
        btnIrRegistro.setContentAreaFilled(false);
        btnIrRegistro.setBorderPainted(false);
        btnIrRegistro.setFocusPainted(false);
        btnIrRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIrRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIrRegistro.addActionListener(e -> {
            new InterfazRegistro().setVisible(true);
            dispose();
        });

        panelTarjeta.add(btnLogin);
        panelTarjeta.add(Box.createRigidArea(new Dimension(0, 10)));
        panelTarjeta.add(btnIrRegistro);

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
        btn.setPreferredSize(new Dimension(200, 36));
        btn.setMaximumSize(new Dimension(200, 36));
        return btn;
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