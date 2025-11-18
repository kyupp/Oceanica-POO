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
public class CommandQueryEnemy extends Command {

    public CommandQueryEnemy(String[] args) {
        super(CommandType.QUERY_ENEMY, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        
        String[] parameters = this.getParameters();
        
        if (parameters.length != 2){
            sendResponse(threadServidor, """
                   Error: Formato incorrecto.
                   Uso: QUERY_ENEMY <name> 
                   Ejemplo: QUERY_ENEMY Mathias
                   """);
            return;
        }
        
        String name = parameters[1];
        
        for (ThreadServidor clientThread : threadServidor.getServer().getConnectedClients()) {
            if (clientThread != null) {
                 if (name == null ? clientThread.getName() == null : name.equals(clientThread.getClientName())) {

                    if (clientThread.getCivilization() == null) {
                        sendResponse(threadServidor, "El usuario '" + name + "' aún no tiene civilización.");
                        return;
                    }

                    Double percentageLife = clientThread.getCivilization().getMap().getAlivePercentage();
                    int deathCells = clientThread.getCivilization().getMap().getDeathCells();

                    sendResponse(threadServidor, "\nPorcentaje de vida: " + percentageLife + ", Casillas muertas: " + deathCells);
                    return;
                }

            }
        }
        
        sendResponse(threadServidor, "No se encontro ningun usuario con el nombre: " + name);
    }
    
    private void sendResponse(ThreadServidor thread, String message) {
        try {
            thread.sendPrivateMessage(message);
        } catch (Exception e) {
            System.out.println("Error enviando respuesta: " + e.getMessage());
        }
    }
    
//    @Override
//    public void processInClient(Client client) {
//        System.out.println("Procesando un attack");
//    }
    
}
