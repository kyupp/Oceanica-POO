/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Fighters.Fighter;
import Fighters.Groups.ChooseFighterGroup;
import Game.GameServer.ThreadServidor;

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
        
        if (parameters.length != 8){
            System.out.println("""
                               "Error: Uso incorrecto del comando.\\n" +
                                               "Correcto: createfighter <name> <imagePath> <power> <resistance> <sanity> <civilization%> <attackGroup>"
                                           );""");
            return;
        }
        
        try{
            // Extraer parámetros
            
            String name = parameters[1];
            String imagePath = parameters[2];
            double power = Double.parseDouble(parameters[3]);
            double resistance = Double.parseDouble(parameters[4]);
            double sanity = Double.parseDouble(parameters[5]);
            int civilization = Integer.parseInt(parameters[6]);
            String attackGroup = parameters[7];
            
            Fighter fighter = ChooseFighterGroup.ChooseFighterGroup(name, imagePath, power, resistance, sanity, civilization, attackGroup);
            
            //TODO: Notificar al usuario

            
        }
        catch (NumberFormatException e){
            //TODO: Notificar al usuario del error.
        }
        catch (Exception e){
            //TODO: Notificar al usuario del error.
        }
        
    }
    
//    @Override
//    public void processInClient(Client client) {
//        System.out.println("Procesando un attack");
//    }
    
}
