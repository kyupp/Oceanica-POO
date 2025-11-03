/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game.GameServer;

import Console.Command;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 *
 * @author diego
 */
public class Server {
    private final int PORT = 35500;
    private final int maxConections = 4;
    private ServerSocket serverSocket;
    private ArrayList<ThreadServidor> connectedClients; // arreglo de hilos por cada cliente conectado
    //referencia a la pantalla
    FrameServer refFrame;
    private ThreadConnections connectionsThread;

    public Server(FrameServer refFrame) {
        connectedClients = new ArrayList<ThreadServidor>();
        this.refFrame = refFrame;
        this.init();
        connectionsThread = new ThreadConnections(this);
        connectionsThread.start();
    }
    
    //método que inicializa el server
    private void init(){
        try {
            serverSocket = new ServerSocket(PORT);
            refFrame.writeMessage("Server running!!!");
        } catch (IOException ex) {
            refFrame.writeMessage("Error: " + ex.getMessage());
        }
    }
    
    void executeCommand(Command comando) {
        if (comando.isIsBroadcast())
            this.broadcast(comando);
        else
            this.sendPrivate(comando);

    }
    
    public void broadcast(Command comando){
        for (ThreadServidor client : connectedClients) {
            try {
                client.objectSender.writeObject(comando);
            } catch (IOException ex) {
                
            }
        }

    }
    
    public void sendPrivate(Command comando){
        //asumo que el nombre del cliente viene en la posición 1 .  private_message Andres "Hola"
        if (comando.getParameters().length <= 1)
            return;
        
        String searchName =  comando.getParameters()[1];
        
        for (ThreadServidor client : connectedClients) {
            if (client.name.equals(searchName)){
                try {
                //simulo enviar solo al primero, pero debe buscarse por nombre
                    client.objectSender.writeObject(comando);
                    break;
                } catch (IOException ex) {
                
                }
            }
        }
    }
    
    
    public void showAllNames(){
        this.refFrame.writeMessage("Usuarios conectados");
        for (ThreadServidor client : connectedClients) {
            this.refFrame.writeMessage(client.name);
        }
    }

    public int getMaxConections() {
        return maxConections;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public ArrayList<ThreadServidor> getConnectedClients() {
        return connectedClients;
    }

    public FrameServer getRefFrame() {
        return refFrame;
    }

    
    
    
    
    
    

    
    
}
