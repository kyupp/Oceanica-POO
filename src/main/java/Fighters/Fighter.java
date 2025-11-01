/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fighters;

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
}
