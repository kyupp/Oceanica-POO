/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Civilization.Civilization;
import Fighters.Fighter;
import Fighters.Groups.*;
import Game.GameController;
import Game.GameServer.ThreadServidor;
import java.util.List;

/*
    @gabri
*/

public class CommandAttack extends Command {

    public CommandAttack(String[] args) {
        super(CommandType.ATTACK, args);
    }
    
    @Override
    public void processForServer(ThreadServidor threadServidor) {
        this.setIsBroadcast(true);
        
        String[] params = this.getParameters();
        // Formato: ATTACK <fighterName> <attackType> <targetPlayer> [additional params...]
        
        if (params.length < 4) {
            threadServidor.sendError("Formato: ATTACK <fighter> <attackType> <target> [params]");
            return;
        }
        
        GameController gameController = threadServidor.getServer().getGameController();
        
        // Verificar que el juego haya iniciado
        if (!gameController.isGameStarted()) {
            threadServidor.sendError("El juego no ha iniciado");
            return;
        }
        
        // Verificar turno
        if (!threadServidor.isMyTurn()) {
            threadServidor.sendError("No es tu turno. Turno actual: " + 
                                    gameController.getCurrentPlayer());
            return;
        }
        
        String attackerName = threadServidor.name;
        String fighterName = params[1];
        String attackType = params[2];
        String targetName = params[3];
        
        // Obtener civilizaciones
        Civilization attacker = gameController.getCivilization(attackerName);
        Civilization target = gameController.getCivilization(targetName);
        
        if (attacker == null || target == null) {
            threadServidor.sendError("Civilización no encontrada");
            return;
        }
        
        // Obtener fighter
        Fighter fighter = attacker.getFighter(fighterName);
        if (fighter == null) {
            threadServidor.sendError("Fighter no encontrado: " + fighterName);
            return;
        }
        
        // Ejecutar ataque según el tipo
        List<String> attackLog = executeAttack(fighter, attackType, target, params);
        
        if (attackLog != null && !attackLog.isEmpty()) {
            // Registrar en bitácoras
            for (String logEntry : attackLog) {
                attacker.logEvent("[ATAQUE] " + logEntry);
                target.logEvent("[DEFENSA] " + logEntry);
            }
            
            attacker.setAttacksExecuted(attacker.getAttacksExecuted() + 1);
            
            // Verificar si el ataque fue exitoso (dañó al enemigo)
            if (target.getMap().getAlivePercentage() < 100) {
                attacker.setAttacksSuccessful(attacker.getAttacksSuccessful() + 1);
            }
            
            threadServidor.getServer().getRefFrame().writeMessage(
                String.join("\n", attackLog)
            );
            
            // Avanzar turno
            gameController.nextTurn();
            
            // Verificar victoria
            if (gameController.isGameEnded()) {
                threadServidor.getServer().getRefFrame().writeMessage(
                    "¡JUEGO TERMINADO! Ganador: " + gameController.getWinner()
                );
            }
        } else {
            threadServidor.sendError("Ataque inválido");
        }
    }
    
    private List<String> executeAttack(Fighter fighter, String attackType, 
                                       Civilization target, String[] params) {
        
        if (fighter instanceof ReleaseTheKrakenGroup) {
            ReleaseTheKrakenGroup kraken = (ReleaseTheKrakenGroup) fighter;
            
            switch (attackType.toUpperCase()) {
                case "TENTACULOS":
                    if (params.length >= 10) { // 3 pares de coordenadas
                        int[] x = {Integer.parseInt(params[4]), 
                                  Integer.parseInt(params[6]), 
                                  Integer.parseInt(params[8])};
                        int[] y = {Integer.parseInt(params[5]), 
                                  Integer.parseInt(params[7]), 
                                  Integer.parseInt(params[9])};
                        return kraken.attackTentaculos(target.getMap(), x, y);
                    }
                    break;
                    
                case "KRAKENBREATH":
                    if (params.length >= 6) {
                        int x = Integer.parseInt(params[4]);
                        int y = Integer.parseInt(params[5]);
                        String direction = params[6];
                        return kraken.attackKrakenBreath(target.getMap(), x, y, direction);
                    }
                    break;
                    
                case "RELEASETHEKRAKEN":
                    if (params.length >= 6) {
                        int x = Integer.parseInt(params[4]);
                        int y = Integer.parseInt(params[5]);
                        return kraken.attackReleaseTheKraken(target.getMap(), x, y);
                    }
                    break;
            }
        }
        
        else if (fighter instanceof PoseidonTheTridentGroup) {
            PoseidonTheTridentGroup poseidon = (PoseidonTheTridentGroup) fighter;
            
            switch (attackType.toUpperCase()) {
                case "THREELINES":
                    if (params.length >= 10) {
                        int[] x = {Integer.parseInt(params[4]), 
                                  Integer.parseInt(params[6]), 
                                  Integer.parseInt(params[8])};
                        int[] y = {Integer.parseInt(params[5]), 
                                  Integer.parseInt(params[7]), 
                                  Integer.parseInt(params[9])};
                        return poseidon.attackThreeLines(target.getMap(), x, y);
                    }
                    break;
                    
                case "THREENUMBERS":
                    if (params.length >= 10) {
                        int[] tridentNums = {Integer.parseInt(params[4]), 
                                            Integer.parseInt(params[5]), 
                                            Integer.parseInt(params[6])};
                        int[] playerNums = {Integer.parseInt(params[7]), 
                                           Integer.parseInt(params[8]), 
                                           Integer.parseInt(params[9])};
                        return poseidon.attackThreeNumbers(target.getMap(), 
                                                          tridentNums, playerNums);
                    }
                    break;
            }
        }
        
        else if (fighter instanceof FishTelepathyGroup) {
            FishTelepathyGroup fish = (FishTelepathyGroup) fighter;
            
            switch (attackType.toUpperCase()) {
                case "CARDUMEN":
                    return fish.attackCardumen(target.getMap());
                    
                case "SHARKATTACK":
                    return fish.attackSharkAttack(target.getMap());
                    
                case "PULP":
                    return fish.attackPulp(target.getMap());
            }
        }
        
        else if (fighter instanceof ThundersUnderTheSeaGroup) {
            ThundersUnderTheSeaGroup thunder = (ThundersUnderTheSeaGroup) fighter;
            
            switch (attackType.toUpperCase()) {
                case "THUNDERRAIN":
                    return thunder.attackThunderRain(target.getMap());
                    
                case "POSEIDONTHUNDERS":
                    return thunder.attackPoseidonThunders(target.getMap());
                    
                case "EELATTACK":
                    return thunder.attackEelAttack(target.getMap());
            }
        }
        
        else if (fighter instanceof UnderseaFireGroup) {
            UnderseaFireGroup fire = (UnderseaFireGroup) fighter;
            
            switch (attackType.toUpperCase()) {
                case "VOLCANORAISING":
                    if (params.length >= 6) {
                        int x = Integer.parseInt(params[4]);
                        int y = Integer.parseInt(params[5]);
                        return fire.attackVolcanoRaising(target.getMap(), x, y);
                    }
                    break;
                    
                case "VOLCANOEXPLOSION":
                    if (params.length >= 6) {
                        int x = Integer.parseInt(params[4]);
                        int y = Integer.parseInt(params[5]);
                        return fire.attackVolcanoExplosion(target.getMap(), x, y);
                    }
                    break;
                    
                case "TERMALRUSH":
                    if (params.length >= 6) {
                        int x = Integer.parseInt(params[4]);
                        int y = Integer.parseInt(params[5]);
                        return fire.attackTermalRush(target.getMap(), x, y);
                    }
                    break;
            }
        }
        
        else if (fighter instanceof WavesControlGroup) {
            WavesControlGroup waves = (WavesControlGroup) fighter;
            
            switch (attackType.toUpperCase()) {
                case "SWIRLRAISING":
                    if (params.length >= 6) {
                        int x = Integer.parseInt(params[4]);
                        int y = Integer.parseInt(params[5]);
                        return waves.attackSwirlRaising(target.getMap(), x, y);
                    }
                    break;
                    
                case "SENDHUMANGARBAGE":
                    if (params.length >= 6) {
                        int x = Integer.parseInt(params[4]);
                        int y = Integer.parseInt(params[5]);
                        return waves.attackSendHumanGarbage(target.getMap(), x, y);
                    }
                    break;
                    
                case "RADIOACTIVERUSH":
                    return waves.attackRadioactiveRush(target.getMap());
            }
        }
        
        return null;
    }
}
