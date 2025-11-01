/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Attack;

/**
 *
 * @author kyup
 */
public abstract class Attack {
    private String name;
    private String description;
    private int damageRadius;
    private int damagePercent;
    
    //TODO: Pensar bien este metodo
    public abstract void execute();
}
