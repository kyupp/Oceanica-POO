/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Game.GameServer.ThreadServidor;

/**
 *
 * @author mathi
 */
public class CommandUpdate extends Command {

    public CommandUpdate(CommandType type, String[] parameters) {
        super(type, parameters);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        //threadServidor.getServer().actualizarMapa(client);
    }
    
}
