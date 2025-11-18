/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Civilization.Civilization;
import Fighters.Fighter;
import Fighters.Groups.ChooseFighterGroup;
import Game.GameClient.Client;
import Game.GameServer.ThreadServidor;

/**
 *
 * @author mathi
 */
public class CommandShowAlive extends Command {

    public CommandShowAlive(String[] args) {
        super(CommandType.SHOW_ALIVE, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
       // int ocupadorPorVolcanes = threadServidor.getCivilization().
    }

    /**
     * Envía respuesta privada al cliente
     */
    private void sendResponse(ThreadServidor thread, String message) {
        try {
            thread.sendPrivateMessage(message);
        } catch (Exception e) {
            System.out.println("Error enviando respuesta: " + e.getMessage());
        }
    }
    
    @Override
    public void processInClient(Client client) {
        double percentage = client.getRefFrame().getMyCivilization().getMap().getAlivePercentage();
        
        client.getRefFrame().writeMessage("Porcentaje de celdas con vida: " + percentage);
    }
    
}
