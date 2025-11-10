/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fighters;

import Game.GameMap.Cell;
import Game.GameMap.MapGrid;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kyup
 */
public class Fighter {

    private String name;
    private String imagePath;
    private double power;
    private double resistance;
    private double sanity;
    private int percentagleOfCivilization;
    private String attackGroup;
    private double nextAttackBoost = 1.0; // Multiplicador para el siguiente ataque
    private boolean isProtected = false;
    private double protectionLevel = 1.0;

    public Fighter(String name, String imagePath, double power, double resistance, double sanity, int percentagleOfCivilization, String attackGroup) {
        this.name = name;
        this.imagePath = imagePath;
        this.power = power;
        this.resistance = resistance;
        this.sanity = sanity;
        this.percentagleOfCivilization = percentagleOfCivilization;
        this.attackGroup = attackGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

    public double getSanity() {
        return sanity;
    }

    public void setSanity(double sanity) {
        this.sanity = sanity;
    }

    public int getPercentagleOfCivilization() {
        return percentagleOfCivilization;
    }

    public void setPercentagleOfCivilization(int percentagleOfCivilization) {
        this.percentagleOfCivilization = percentagleOfCivilization;
    }

    public String getAttackGroup() {
        return attackGroup;
    }

    public void setAttackGroup(String attackGroup) {
        this.attackGroup = attackGroup;
    }

    /*
     * Usar Sanidad: Cura las celdas parcialmente heridas del luchador
     */
    public List<String> useSanity(MapGrid map) {
        List<String> log = new ArrayList<>();
        log.add("=== USO DE SANIDAD ===");
        log.add("Luchador: " + this.name);

        ArrayList<Cell> myCells = map.getCellsByFighter(this);
        int healedCells = 0;
        double healAmount = this.sanity; // 50%, 75%, o 100%

        for (Cell cell : myCells) {
            if (!cell.isDestroyed() && cell.getLifePercent() < 100) {
                cell.heal(healAmount);
                healedCells++;
            }
        }

        log.add("Cantidad de sanación: " + healAmount + "%");
        log.add("Celdas sanadas: " + healedCells + "/" + myCells.size());
        return log;
    }

    /*
     * Usar Fuerza: Aumenta el siguiente ataque
     */
    public List<String> usePower() {
        List<String> log = new ArrayList<>();
        log.add("=== USO DE FUERZA ===");
        log.add("Luchador: " + this.name);

        double boost = this.power; // 50%, 75%, o 100%
        this.nextAttackBoost = 1.0 + (boost / 100.0);

        log.add("Boost de poder: +" + boost + "%");
        log.add("Siguiente ataque tendrá " + (this.nextAttackBoost * 100) + "% de daño");
        log.add("¡El efecto se aplica solo al SIGUIENTE ataque!");
        return log;
    }

    /*
     * Usar Resistencia: Protege contra el siguiente ataque
     */
    public List<String> useResistance() {
        List<String> log = new ArrayList<>();
        log.add("=== USO DE RESISTENCIA ===");
        log.add("Luchador: " + this.name);

        double protection = this.resistance; // 50%, 75%, o 100%
        this.isProtected = true;
        this.protectionLevel = protection / 100.0;

        log.add("Nivel de protección: " + protection + "%");
        log.add("El siguiente ataque recibido causará solo "
                + ((1.0 - this.protectionLevel) * 100) + "% del daño");
        return log;
    }

    public double getNextAttackBoost() {
        return nextAttackBoost;
    }

    public void resetAttackBoost() {
        this.nextAttackBoost = 1.0;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public double getProtectionLevel() {
        return protectionLevel;
    }

    public void removeProtection() {
        this.isProtected = false;
        this.protectionLevel = 1.0;
    }
}