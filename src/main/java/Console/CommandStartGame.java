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
public class CommandStartGame extends Command {

    public CommandStartGame(String[] args) {
        super(CommandType.START_GAME, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        this.setIsBroadcast(true);
        
        try {
            // Verificar que el jugador tenga civilización y 3 fighters
            if (!threadServidor.hasCivilization()) {
                threadServidor.sendError("Debes crear una civilización primero");
                return;
            }
            
            if (threadServidor.getCivilization().getFighters().size() != 3) {
                threadServidor.sendError("Necesitas 3 fighters para iniciar. " +
                    "Tienes: " + threadServidor.getCivilization().getFighters().size());
                return;
            }
            
            // Verificar porcentaje total
            int totalPercent = 0;
            for (var f : threadServidor.getCivilization().getFighters()) {
                totalPercent += f.getPercentagleOfCivilization();
            }
            
            if (totalPercent != 100) {
                threadServidor.sendError("Los fighters deben sumar 100% de la civilización. " +
                    "Actual: " + totalPercent + "%");
                return;
            }
            
            // Verificar que todas las civilizaciones estén listas
            if (!threadServidor.getServer().getGameController().allCivilizationsReady()) {
                threadServidor.sendConfirmation(
                    "Esperando a que todos los jugadores completen sus civilizaciones..."
                );
                
                threadServidor.getServer().getRefFrame().writeMessage(
                    threadServidor.name + " está listo para iniciar"
                );
                return;
            }
            
            // Iniciar el juego
            threadServidor.getServer().startGame();
            
            String startMessage = 
                "╔════════════════════════════════════╗\n" +
                "║     ¡JUEGO INICIADO!              ║\n" +
                "╚════════════════════════════════════╝\n" +
                "Orden de turnos: " + 
                threadServidor.getServer().getGameController().getTurnOrder() + "\n" +
                "Turno actual: " + 
                threadServidor.getServer().getGameController().getCurrentPlayer();
            
            threadServidor.getServer().getRefFrame().writeMessage(startMessage);
            
        } catch (Exception e) {
            threadServidor.sendError("Error al iniciar juego: " + e.getMessage());
            e.printStackTrace();
        }
    }
}