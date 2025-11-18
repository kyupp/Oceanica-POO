/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game.GameClient;

import Console.CommandFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathi
 */
public class Client {
    private final int PORT = 35500;
    private final String IP_ADDRESS = "localhost";
    private Socket socket;
    private FrameClient refFrame;
    public ObjectInputStream objectListener;
    public ObjectOutputStream objectSender;
    private ThreadClient threadClient;
    
    private String name;

    public Client(FrameClient refFrame, String name) {
        this.refFrame = refFrame;
        this.name = name;
        this.connect();
        
    }
    
    private void connect (){
        try {
            socket = new Socket(IP_ADDRESS , PORT);
            objectSender =  new ObjectOutputStream (socket.getOutputStream());
            objectSender.flush();
            objectListener =  new ObjectInputStream (socket.getInputStream());
            
            threadClient =  new ThreadClient(this);
            threadClient.start();
            
            
            //envía el nombre
            String args[] = {"NAME", this.name};
            objectSender.writeObject(CommandFactory.getCommand(args));
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public ObjectInputStream getObjectListener() {
        return objectListener; 
    }

    public ObjectOutputStream getObjectSender() {
        return objectSender;
    }
    
    

    public FrameClient getRefFrame() {
        return refFrame;
    }

    public Socket getSocket() {
        return socket;
    }

    public ThreadClient getThreadClient() {
        return threadClient;
    }

    public String getName() {
        return name;
    }
    
    
    
}
