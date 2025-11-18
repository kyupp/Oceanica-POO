/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game.GameClient;

import Civilization.Civilization;
import Console.Command;
import Console.CommandFactory;
import Console.CommandUtil;
import Fighters.Fighter;
import Fighters.Groups.ChooseFighterGroup;
import java.io.IOException;

/**
 *
 * @author mathi
 */
public class modelClient {

    private Client client;
    private Civilization myCivilization; // ✅ NUEVO: referencia a civilización local
    
    public modelClient(Client client, Civilization myCivilization) {
        this.client = client;
        this.myCivilization = myCivilization;
    }
    
    /**
     * Ahora procesa fighters localmente antes de enviar al servidor
     */
    public String comprobarComando(String comandoIngresado) {
        if (comandoIngresado.length() > 0) {
            String args[] = CommandUtil.tokenizerArgs(comandoIngresado);
            if (args.length > 0) {
                switch (args[0].toUpperCase()) {
                    
                    case "CREATE_FIGHTER":
                        return procesarCreateFighter(args);
                    
                    case "START_GAME":
                        return procesarStartGame(args);
                    
                    case "STATUS":
                        // Comando local para ver estado
                        return mostrarEstadoLocal();
                    case "QUERY_CELL":
                        return this.procesarQuerryCell(args);
                    default:
                        Command comando = CommandFactory.getCommand(args);
                        return enviarComandoServer(comando);
                }
            }
        }
        return "";
    }
    
    /**
     * NUEVO: Procesa creación de fighter localmente Y en servidor
     */
    private String procesarCreateFighter(String[] args) {
        // Validar cantidad
        if (myCivilization.getFighters().size() >= 3) {
            return "Error: La cantidad máxima de fighters fue alcanzada (3/3)\n";
        }
        
        // Validar formato
        if (args.length != 8) {
            return """
                   Error: Formato incorrecto.
                   Uso: CREATE_FIGHTER <name> <imagePath> <power> <resistance> <sanity> <civilization%> <attackGroup>
                   Ejemplo: CREATE_FIGHTER Poseidon img.png 100 75 50 30 PoseidonTheTrident
                   """;
        }
        
        try {
            // Extraer parámetros
            String name = args[1];
            String imagePath = args[2];
            double power = Double.parseDouble(args[3]);
            double resistance = Double.parseDouble(args[4]);
            double sanity = Double.parseDouble(args[5]);
            int civilizationPercent = Integer.parseInt(args[6]);
            String attackGroup = args[7];
            
            //  Validar porcentajes
            int totalPercent = civilizationPercent;
            for (Fighter f : myCivilization.getFighters()) {
                totalPercent += f.getPercentagleOfCivilization();
            }
            
            if (totalPercent > 100) {
                return "Error: El total de porcentajes excedería 100% (" + totalPercent + "%)\n";
            }
            
            //  Crear fighter LOCALMENTE
            Fighter fighter = ChooseFighterGroup.ChooseFighterGroup(
                name, imagePath, power, resistance, sanity, 
                civilizationPercent, attackGroup
            );
            
            if (fighter == null) {
                return "Error: Grupo de ataque inválido: " + attackGroup + "\n";
            }
            
            //  Agregar a civilización local
            myCivilization.addFighter(fighter);
            
            //  Actualizar UI
            client.getRefFrame().actualizarContadorFighters();
            // Refrescar paneles de UI para mostrar el nuevo fighter sin duplicar mensajes
            try {
                client.getRefFrame().updatePlayerInfo();
                client.getRefFrame().updateFightersDisplay();
            } catch (Exception e) {
                // Si no está disponible la UI en este contexto, continuar silenciosamente
            }
            
            String mensaje = String.format(
                " Fighter creado: %s (%s)\n" +
                "   - Poder: %.0f%%, Resistencia: %.0f%%, Sanidad: %.0f%%\n" +
                "   - Representa: %d%% de la civilización\n" +
                "   - Fighters: %d/3\n",
                name, attackGroup, power, resistance, sanity,
                civilizationPercent, myCivilization.getFighters().size()
            );
            
            // Enviar al servidor para sincronización
            Command comando = CommandFactory.getCommand(args);
            String resultado = enviarComandoServer(comando);
            
            return mensaje + resultado;
            
        } catch (NumberFormatException e) {
            return "Error: Valores numéricos inválidos\n";
        } catch (Exception e) {
            return "Error inesperado: " + e.getMessage() + "\n";
        }
    }
    
    /**
     * Valida antes de iniciar juego
     */
    private String procesarStartGame(String[] args) {
        // Validar que tenga 3 fighters
        if (myCivilization.getFighters().size() != 3) {
            return "Error: Necesitas 3 fighters para iniciar. " +
                   "Tienes: " + myCivilization.getFighters().size() + "/3\n";
        }
        
        // Validar que sumen 100%
        int totalPercent = 0;
        for (Fighter f : myCivilization.getFighters()) {
            totalPercent += f.getPercentagleOfCivilization();
        }
        
        if (totalPercent != 100) {
            return "Error: Los fighters deben sumar 100% de la civilización. " +
                   "Actual: " + totalPercent + "%\n";
        }
        
        // Inicializar mapa si no está inicializado
        if (myCivilization.getMap() == null) {
            myCivilization.initializeMap(20, 30);
        }
        
        // Todo bien, enviar al servidor
        Command comando = CommandFactory.getCommand(args);
        return enviarComandoServer(comando) + 
               " Esperando que otros jugadores estén listos...\n";
    }
    
    /**
     * NUEVO: Muestra estado local sin consultar servidor
     */
    private String mostrarEstadoLocal() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════╗\n");
        sb.append("║  ESTADO DE ").append(myCivilization.getName()).append("\n");
        sb.append("╚════════════════════════════════════╝\n\n");
        
        sb.append("Fighters: ").append(myCivilization.getFighters().size()).append("/3\n");
        
        if (!myCivilization.getFighters().isEmpty()) {
            sb.append("\n--- FIGHTERS ---\n");
            int totalPercent = 0;
            for (Fighter f : myCivilization.getFighters()) {
                sb.append(String.format("• %s (%d%%)\n", 
                    f.getName(), f.getPercentagleOfCivilization()));
                sb.append(String.format("  Poder: %.0f%%, Resistencia: %.0f%%, Sanidad: %.0f%%\n",
                    f.getPower(), f.getResistance(), f.getSanity()));
                totalPercent += f.getPercentagleOfCivilization();
            }
            sb.append(String.format("\nTotal representación: %d%%\n", totalPercent));
            
            if (totalPercent == 100) {
                sb.append(" Listo para iniciar\n");
            } else {
                sb.append("️  Falta: ").append(100 - totalPercent).append("%\n");
            }
        }
        
        if (myCivilization.getMap() != null) {
            sb.append(String.format("\nVida del mapa: %.1f%%\n", 
                myCivilization.getMap().getAlivePercentage()));
        }
        
        sb.append("\n");
        return sb.toString();
    }
    
    /**
     * Envía comando al servidor
     */
    public String enviarComandoServer(Command comando) {
        if (comando != null) {
            try {
                client.objectSender.writeObject(comando);
                client.objectSender.flush();
            } catch (IOException ex) {
                return "Error: No se pudo enviar el comando al servidor\n";
            }
        } else {
            return "Error: Comando desconocido\n";
        }
        return "";
    }
    
    public String procesarQuerryCell(String[] args){
        if (args.length != 3) {
            return """
                   Error: Formato incorrecto.
                   Uso: QUERY_CELL <x> <y>
                   """;
        }
        
        try {
            Command comando = CommandFactory.getCommand(args);
            
            comando.processInClient(this.client);
            
            return "";
            
        } catch (NumberFormatException e) {
            return "Error: Valores numéricos inválidos\n";
        } catch (Exception e) {
            return "Error inesperado: " + e.getMessage() + "\n";
        }
    }
}