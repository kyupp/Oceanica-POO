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
public class CommandName extends Command{
    
    public CommandName(String[] args) { //name Diego
        super(CommandType.NAME, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        String[] args = this.getParameters();
        String clientName = args[1]; // El nombre viene como segundo parámetro
        threadServidor.setClientName(clientName);

        System.out.println("Cliente identificado como: " + clientName);
    }

    
    @Override
    public void processInClient(Client client) {
        //NAME Nombre de persona
        client.getRefFrame().writeMessage("Conectado el cliente: " + this.getParameters()[1]);
        
        
    }

}
