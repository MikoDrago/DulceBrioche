/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package dulcebrioche;

import javax.swing.SwingUtilities;

/**
 *
 * @author mikod
 */
public class DulceBrioche {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() ->
            new InterfazProductos().setVisible(true));
    }
    
}
