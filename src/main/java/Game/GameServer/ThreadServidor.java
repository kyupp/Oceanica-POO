/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game.GameServer;

import Console.Command;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author diego
 */
public class ThreadServidor extends Thread{
    private Server server;
    private Socket socket;
    //Streams para leer y escribir objetos
    public ObjectInputStream objectListener;
    public ObjectOutputStream objectSender;
    public String name;
    
    public boolean isActive = true;
        
    public boolean isRunning = true;
    
    
    

    public ThreadServidor(Server server, Socket socket) {
        try {
        this.server = server;
        this.socket = socket;
        objectSender =  new ObjectOutputStream (socket.getOutputStream());
        objectSender.flush();
        objectListener =  new ObjectInputStream (socket.getInputStream());
        } catch (IOException ex) {
                System.out.println(ex.getMessage());
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        
    }
    
    public void run (){
        Command comando;
        while (isRunning){
            try {
                comando = (Command)objectListener.readObject();
                server.refFrame.writeMessage("ThreadServer recibió: " + comando);
                comando.processForServer(this);
                
                if (isActive)
                    server.executeCommand(comando);
                
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }  
        } 
    }
    
    public void showAllClients (){
        this.server.showAllNames();
    }
    
    
    
    
    
}
