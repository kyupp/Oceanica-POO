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
public class CommandSkipTurn extends Command {

    public CommandSkipTurn(String[] args) {
        super(CommandType.SKIP_TURN, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        this.setIsBroadcast(true);
    }
    
//    @Override
//    public void processInClient(Client client) {
//        System.out.println("Procesando un attack");
//    }
    
}
