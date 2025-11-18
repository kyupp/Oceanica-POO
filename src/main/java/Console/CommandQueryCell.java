/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Game.GameClient.Client;
import Game.GameMap.Cell;
import Game.GameServer.ThreadServidor;
import java.util.ArrayList;

/**
 *
 * @author mathi
 */
public class CommandQueryCell extends Command {

    public CommandQueryCell(String[] args) {
        super(CommandType.QUERY_CELL, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        this.setIsBroadcast(false);
    }

    @Override
    public void processInClient(Client client) {
        String[] parameters = this.getParameters();

        try {
            int x = Integer.parseInt(parameters[1]);
            int y = Integer.parseInt(parameters[2]);

            if (x < 0 || y < 0) {
                // TODO: Regresar mensaje al usuario que los valores deben ser mayores a 0.
                client.getRefFrame().writeMessage(
                        "Error: los valores de coordenadas no pueden ser negativos."
                );
                return;
            }

            Cell cell = client.getRefFrame().getMap().getCell(x, y);

            if (cell == null) {
                client.getRefFrame().writeMessage(
                        "Error: la celda solicitada no existe en el mapa."
                );
                return;
            }

            String message = "Información de celda (" + x + "," + y + "): " + cell;
            
            String attackHistory = arrayListToString(cell.getAttackHistory());
            
            client.getRefFrame().writeMessage(message + "\n     Attack History: " + attackHistory);

        } catch (NumberFormatException e) {
            
            client.getRefFrame().writeMessage(
                    "Error: los parámetros X e Y deben ser valores numéricos enteros."
            );
        } catch (Exception e) {
            client.getRefFrame().writeMessage(
                    "Error inesperado al consultar celda: " + e.getMessage()
            );
        }
    }
    
    public static String arrayListToString(ArrayList<String> list) {
        if (list == null) return "Todavia no ha recibido ataques";
        
        return String.join(" ", list);
    }

}

