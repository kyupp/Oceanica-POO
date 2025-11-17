/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Civilization.Civilization;
import Fighters.Fighter;
import Game.GameController;
import Game.GameServer.ThreadServidor;
import java.util.List;

/*
    @author gabri
*/

public class CommandUseSanity extends Command {

    public CommandUseSanity(String[] args) {
        super(CommandType.USE_SANITY, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        this.setIsBroadcast(true);
        
        String[] params = this.getParameters();
        
        if (params.length < 2) {
            threadServidor.sendError("Uso: USE_SANITY <fighterName>");
            return;
        }
        
        GameController gc = threadServidor.getServer().getGameController();
        
        if (!gc.isGameStarted()) {
            threadServidor.sendError("El juego no ha iniciado");
            return;
        }
        
        if (!threadServidor.isMyTurn()) {
            threadServidor.sendError("No es tu turno");
            return;
        }
        
        String fighterName = params[1];
        Civilization civ = threadServidor.getCivilization();
        Fighter fighter = civ.getFighter(fighterName);
        
        if (fighter == null) {
            threadServidor.sendError("Fighter no encontrado: " + fighterName);
            return;
        }
        
        List<String> log = fighter.useSanity(civ.getMap());
        
        for (String entry : log) {
            civ.logEvent(entry);
            threadServidor.getServer().getRefFrame().writeMessage(entry);
        }
        
        // Avanzar turno
        gc.nextTurn();
        
        threadServidor.sendConfirmation("Sanidad activada. Celdas curadas.");
    }
}
