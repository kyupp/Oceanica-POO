/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Civilization.Civilization;
import Fighters.Fighter;
import Game.GameServer.ThreadServidor;
import java.util.List;

/**
 *
 * @author mathi
 */
public class CommandLogSummary extends Command {

    public CommandLogSummary(String[] args) {
        super(CommandType.LOG_SUMMARY, args);
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
            Civilization civ = threadServidor.getCivilization();
            String result = "";
            if(civ.getAttacksExecuted() <= 0){
                result = "No se han ejecutado ataques";
            }else{
                int executed = civ.getAttacksExecuted();
                int successful = civ.getAttacksSuccessful();
                int percentage = successful / executed;
                result = "Ataques hechos: " + executed + ", Ataques logrados: " + successful + ", Porcentaje ataques logrados: " + percentage;
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
