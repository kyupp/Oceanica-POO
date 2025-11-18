/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Game.GameClient.Client;
import Game.GameServer.ThreadServidor;

/**
 *
 * @author mathi
 */
public class CommandShowOccupied extends Command {

    public CommandShowOccupied(String[] args) {
        super(CommandType.SHOW_OCCUPIED, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        this.setIsBroadcast(false);
    }
    
    @Override
    public void processInClient(Client client) {
        int hasVolcano = client.getRefFrame().getMyCivilization().getMap().getVolcanoOcupated();
        int hasSwirl = client.getRefFrame().getMyCivilization().getMap().getSwirlOcupated();
        
        client.getRefFrame().writeMessage("Celdas ocupadas por volcanes: " + hasVolcano + "\nCeldas ocupadas por remolinos: " + hasSwirl);
    }
    
}
