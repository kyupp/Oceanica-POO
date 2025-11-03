/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Game.GameClient.Client;
import Game.GameServer.ThreadServidor;

/**
 *
 * @author diego
 */
public class CommandMessage extends Command{

    public CommandMessage(String[] args) {
        super(CommandType.MESSAGE, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        this.setIsBroadcast(true);
        
    }
    
    @Override
    public void processInClient(Client client) {
        //Message "string"
        client.getRefFrame().writeMessage("Mensaje recibido: " + this.getParameters()[1]);
    }
    
}
