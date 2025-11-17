/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Civilization.Civilization;
import Fighters.Fighter;
import Fighters.Groups.ChooseFighterGroup;
import Game.GameServer.ThreadServidor;
import java.io.IOException;

/**
 *
 * @author mathi
 */
public class CommandCreateFighter extends Command {

    public CommandCreateFighter(String[] args) {
        super(CommandType.CREATE_FIGHTER, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        // No es broadcast - solo confirmar al cliente
        this.setIsBroadcast(false);

        String[] parameters = this.getParameters();

        if (parameters.length != 8) {
            sendResponse(threadServidor, " Error: Formato incorrecto del comando");
            return;
        }

        try {
            // Extraer parámetros
            String name = parameters[1];
            String imagePath = parameters[2];
            double power = Double.parseDouble(parameters[3]);
            double resistance = Double.parseDouble(parameters[4]);
            double sanity = Double.parseDouble(parameters[5]);
            int civilization = Integer.parseInt(parameters[6]);
            String attackGroup = parameters[7];

            //  Verificar que el jugador tenga civilización
            if (!threadServidor.hasCivilization()) {
                // Crear civilización si no existe
                threadServidor.getServer().registerCivilization(threadServidor.name);
            }

            Civilization civ = threadServidor.getCivilization();

            //  Validaciones en servidor
            if (civ.getFighters().size() >= 3) {
                sendResponse(threadServidor, " Ya tienes 3 fighters");
                return;
            }

            // Validar porcentaje total
            int totalPercent = civilization;
            for (Fighter f : civ.getFighters()) {
                totalPercent += f.getPercentagleOfCivilization();
            }

            if (totalPercent > 100) {
                sendResponse(threadServidor,
                        " Porcentaje total excedería 100% (actual: " + totalPercent + "%)");
                return;
            }

            // Crear fighter en servidor
            Fighter fighter = ChooseFighterGroup.ChooseFighterGroup(
                    name, imagePath, power, resistance, sanity, civilization, attackGroup
            );

            if (fighter == null) {
                sendResponse(threadServidor, " Grupo de ataque inválido: " + attackGroup);
                return;
            }

            // Agregar a civilización del servidor
            civ.addFighter(fighter);

            // Log en servidor
            threadServidor.getServer().getRefFrame().writeMessage(
                    String.format("%s creó fighter: %s (%s) - %d%% | Fighters: %d/3",
                            threadServidor.name, name, attackGroup, civilization,
                            civ.getFighters().size())
            );

            // Confirmar al cliente
            sendResponse(threadServidor,
                    "Fighter registrado en servidor: " + name);

        } catch (NumberFormatException e) {
            sendResponse(threadServidor, " Error: valores numéricos inválidos");
        } catch (Exception e) {
            sendResponse(threadServidor, " Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Envía respuesta privada al cliente
     */
    private void sendResponse(ThreadServidor thread, String message) {
        try {
            thread.sendPrivateMessage(message);
        } catch (Exception e) {
            System.out.println("Error enviando respuesta: " + e.getMessage());
        }
    }
}