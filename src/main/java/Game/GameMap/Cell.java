/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game.GameMap;

import Fighters.Fighter;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 *
 * @author kyup
 */
public class Cell {
    private int x;
    private int y;
    private double lifePercent;
    private Fighter owner;
    private ArrayList<String> attackHistory;
    boolean hasVolcano;
    boolean hasSwirl;
    boolean isRadioactive;
    private JLabel celda;

    public Cell(int x, int y, double lifePercent, Fighter owner, ArrayList<String> attackHistory, boolean hasVolcano, boolean hasSwirl, boolean isRadioactive) {
        this.x = x;
        this.y = y;
        this.lifePercent = lifePercent;
        this.owner = owner;
        this.attackHistory = attackHistory;
        this.hasVolcano = hasVolcano;
        this.hasSwirl = hasSwirl;
        this.isRadioactive = isRadioactive;
    }

    public Cell(int x, int y, double lifePercent, boolean hasVolcano, boolean hasSwirl, boolean isRadioactive, JLabel celda) {
        this.x = x;
        this.y = y;
        this.lifePercent = lifePercent;
        this.hasVolcano = hasVolcano;
        this.hasSwirl = hasSwirl;
        this.isRadioactive = isRadioactive;
        this.celda = celda;
    }
    
    public String applyDamage(double amount){
        //TODO: Crear logica
        return "Daño realizado -> "+amount;
    }
    
    public void logAttack(String entry){
        //TODO: Crear logica
    }
    
    public boolean isDestroyed(){
        return lifePercent <= 0;
    }
    
}
