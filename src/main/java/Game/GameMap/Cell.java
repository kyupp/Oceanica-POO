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
    
    public boolean isDestroyed() {
        return lifePercent <= 0;
    }

    public void setHasVolcano(boolean hasVolcano) {
        this.hasVolcano = hasVolcano;
    }

    public void setHasSwirl(boolean hasSwirl) {
        this.hasSwirl = hasSwirl;
    }

    public void setIsRadioactive(boolean isRadioactive) {
        this.isRadioactive = isRadioactive;
    }

    public String applyDamage(double amount) {
        double oldLife = this.lifePercent;
        this.lifePercent -= amount;

        if (this.lifePercent < 0) {
            this.lifePercent = 0;
        }

        String damageInfo = String.format("Celda (%d,%d): %.1f%% -> %.1f%% (daño: %.1f%%)",
                x, y, oldLife, lifePercent, amount);

        // Registrar en historial
        String entry = "Daño recibido: " + amount + "% | Vida restante: " + lifePercent + "%";
        logAttack(entry);

        return damageInfo;
    }

    public void logAttack(String entry) {
        if (this.attackHistory == null) {
            this.attackHistory = new ArrayList<>();
        }

        String timestamp = java.time.LocalDateTime.now().toString();
        this.attackHistory.add("[" + timestamp + "] " + entry);
    }

    public void heal(double amount) {
        double oldLife = this.lifePercent;
        this.lifePercent += amount;

        if (this.lifePercent > 100) {
            this.lifePercent = 100;
        }

        String entry = "Sanación: +" + amount + "% | Vida: " + oldLife + "% -> " + lifePercent + "%";
        logAttack(entry);
    }

    public ArrayList<String> getAttackHistory() {
        return this.attackHistory;
    }

    public int getLifePercent() {
        return (int) this.lifePercent;
    }

    public Object getOwner() {
        return this.owner;
    }
    
    

    @Override
    public String toString() {
        return """
               
               Informacion Celda: 
                   Cell{x = """ + x + ", y = " + y + "}\n     lifePercent =" + lifePercent + "\n     owner = " + owner + "\n     attackHistory = " + attackHistory + "\n     hasVolcano = " + hasVolcano + "\n     hasSwirl = " + hasSwirl + "\n     isRadioactive = " + isRadioactive;
    }
    
     

}
