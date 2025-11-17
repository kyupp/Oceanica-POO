/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

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
        this.setIsBroadcast(false);

        String[] parameters = this.getParameters();

        if (parameters.length != 8) {
            String msg = """
                         Error: Uso incorrecto del comando.
                         Correcto: createfighter <name> <imagePath> <power> <resistance> <sanity> <civilization%> <attackGroup>
                         """;

            // ❗ Notificación privada al usuario
            sendPrivateResponse(threadServidor, msg);
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

            Fighter fighter = ChooseFighterGroup.ChooseFighterGroup(
                    name, imagePath, power, resistance, sanity, civilization, attackGroup
            );

            // ❗ Notificación al usuario indicando éxito
            sendPrivateResponse(threadServidor,
                    "Fighter creado exitosamente: " + fighter.getName());

        } catch (NumberFormatException e) {
            // ❗ Error por parámetros numéricos incorrectos
            sendPrivateResponse(threadServidor,
                    "Error: valores numéricos inválidos. Verifique power, resistance, sanity y civilization.");
        } catch (Exception e) {
            // ❗ Error genérico, no rompemos el servidor
            sendPrivateResponse(threadServidor,
                    "Error inesperado al crear fighter: " + e.getMessage());
        }
    }

    /**
     * Envía un mensaje privado al cliente usando CommandMessage
     */
    private void sendPrivateResponse(ThreadServidor thread, String message) {
        try {
            String[] msgArgs = new String[]{"MESSAGE", message, "false"}; // false = privado
            CommandMessage response = new CommandMessage(msgArgs);
            thread.objectSender.writeObject(response);
            thread.objectSender.flush();
        } catch (IOException io) {
            System.out.println("No se pudo enviar respuesta al cliente: " + io.getMessage());
        }
    }
}

