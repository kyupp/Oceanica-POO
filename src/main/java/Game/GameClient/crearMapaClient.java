/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game.GameClient;

import Game.GameMap.Cell;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 *
 * @author mathiasviquez
 */
public class crearMapaClient {
    
    private FrameClient pantalla;
    private final int FILAS = 20;
    private final int COLUMNAS = 30;

    public crearMapaClient(FrameClient pantalla) {
        this.pantalla = pantalla;
    }
    
    public void crearMapa(){
        int size = 22;
        int x,y;
        
        for (int fila = 0; fila < FILAS; fila++){
            for (int columna = 0; columna < COLUMNAS; columna++){
                
                // Calcular coordenadas en píxeles
                x = fila * size;
                y = columna * size;

                // Crear el JLabel visual
                JLabel lblCasilla = new JLabel();
                lblCasilla.setOpaque(true);
                lblCasilla.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                lblCasilla.setBounds(y, x, size, size);

                lblCasilla.setBackground(new java.awt.Color(50, 117, 168));
                
                pantalla.getPnlMap().add(lblCasilla);
                
                Cell cell = new Cell(x,y,100,false,false,false,lblCasilla);
                
                //System.out.println("Intento:" + fila +", " + columna);
                
                pantalla.getMap().addCell(fila, columna, cell);
                
            }
        }
    }
    
}
