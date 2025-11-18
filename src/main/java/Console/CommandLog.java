/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Fighters.Fighter;
import Game.GameServer.ThreadServidor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mathi
 */
public class CommandLog extends Command {

    public CommandLog(String[] args) {
        super(CommandType.LOG, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        this.setIsBroadcast(false);
        
        String[] parameters = this.getParameters();
        
        if (parameters.length != 1) {
            sendResponse(threadServidor, " Error: Formato incorrecto del comando");
            return;
        }

        try {
            // Extraer parámetros
            List<Fighter> list = threadServidor.getCivilization().getFighters();
            String result = "";
            for(Fighter f : list){
                if(f.getPercentagleOfCivilization() >= 0){
                    result += f.getName()+ " ";
                }
            }
           sendResponse(threadServidor, result);
        return;
    } catch (Exception e) {
            System.out.println("Error enviando respuesta: " + e.getMessage());
        }
    }
        
    private void sendResponse(ThreadServidor thread, String message) {
        try {
            thread.sendPrivateMessage(message);
        } catch (Exception e) {
            System.out.println("Error enviando respuesta: " + e.getMessage());
        }
    }
    
}
