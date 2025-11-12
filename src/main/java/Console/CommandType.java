/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Console;

/**
 *
 * @author diego
 */
public enum CommandType {
    ATTACK (4),  //attack Andres 4 5
    MESSAGE (2), //message hola a todos
    PRIVATE_MESSAGE(3), //private Andres hola andres
    GIVEUP (1), //giveup
    NAME (2),
    CREATE_FIGHTER (8),
    START_GAME (1),
    SKIP_TURN (1),
    QUERY_CELL (3),
    LOG (1),
    LOG_SUMMARY (1),
    QUERY_ENEMY (2),
    SHOW_OCCUPIED (1),
    SHOW_PERCENTAGES (1),
    SHOW_ALIVE (1),
    USE_POWER(2),
    USE_RESISTANCE (2),
    USE_SANITY (2);
    
    
    
    
    
    private int requiredParameters;

    private CommandType(int requiredParameters) {
        this.requiredParameters = requiredParameters;
    }

    public int getRequiredParameters() {
        return requiredParameters;
    }
    
    
    
    
    
}
