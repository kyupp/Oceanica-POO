/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Game.GameServer.ThreadServidor;

/**
 *
 * @author diego
 */
public class CommandGiveup  extends Command{

    public CommandGiveup(String[] args) {
        super(CommandType.PRIVATE_MESSAGE, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        threadServidor.getServer().getGameController().forceEndGame(threadServidor.getClientName());
    }
    
//    @Override
//    public void processInClient(Client client) {
//        System.out.println("Procesando un attack");
//    }
}
