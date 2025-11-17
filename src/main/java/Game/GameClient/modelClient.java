/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game.GameClient;

import Console.Command;
import Console.CommandFactory;
import Console.CommandUtil;
import java.io.IOException;

/**
 *
 * @author mathi
 */
public class modelClient {

    public modelClient(Client client) {
        this.client = client;
    }
    
    private Client client;
    
    public String comprobarComando(String comandoIngresado) {
        if (comandoIngresado.length() > 0) {
            String args[] = CommandUtil.tokenizerArgs(comandoIngresado);
            if (args.length > 0) {
                switch (args[0].toUpperCase()){
                    case "CREATE_FIGHTER":
                        System.out.println(client.getRefFrame().comprobarCantidadFighers());
                        if (client.getRefFrame().comprobarCantidadFighers()){
                            Command comando = CommandFactory.getCommand(args);
                            if (comando != null){
                                client.getRefFrame().aumentarContadorFighters();
                                return enviarComandoServer(comando);
                            }
                        }else{
                           return "Error:  La cantidad maxima de fighters fue alcanzada";
                            
                        }
                    default:
                        Command comando = CommandFactory.getCommand(args);
                        return enviarComandoServer(comando);
                }
            }
        }
        return "";
    }
    
    public String enviarComandoServer(Command comando){
        if (comando != null) {
            try {
                client.objectSender.writeObject(comando);
            } catch (IOException ex) {
                return "Error: no se pudo enviar el comando";
            }
        } else {
            return "Error: comando desconocido";
         }
        return "";
    }

    
}
