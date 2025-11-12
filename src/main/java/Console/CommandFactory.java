/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

/**
 *
 * @author diego
 */
public class CommandFactory {
    
    
    public static Command getCommand(String[] args){
        String type = args[0].toUpperCase();
        
        switch (type) {
            case "ATTACK":
                return new CommandAttack(args);
            case "MESSAGE":
                return new CommandMessage(args);
            case "PRIVATE_MESSAGE":
                return new CommandPrivateMessage(args);
            case "GIVEUP":
                return new CommandGiveup(args);
            case "NAME":
                return new CommandName(args);
            case "CREATE_FIGHTER":
                return new CommandCreateFighter(args);
            case "START_GAME":
                return new CommandStartGame(args);
            case "SKIP_TURN":
                return new CommandSkipTurn(args);
            case "QUERY_CELL":
                return new CommandQueryCell(args);
            case "LOG":
                return new CommandLog(args);
            case "LOG_SUMMARY":
                return new CommandLogSummary(args);
            case "QUERY_ENEMY":
                return new CommandQueryEnemy(args);
            case "SHOW_OCCUPIED":
                return new CommandShowOccupied(args);
            case "SHOW_PERCENTAGES":
                return new CommandShowPercentages(args);
            case "SHOW_ALIVE":
                return new CommandShowAlive(args);
            case "USE_POWER":
                return new CommandUsePower(args);
            case "USE_RESISTANCE":
                return new CommandUseResistance(args);
            case "USE_SANITY":
                return new CommandUseSanity(args);
            default:
                return null;
        }
        
        
    }
    
}
