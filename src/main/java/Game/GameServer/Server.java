/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game.GameServer;

import Civilization.Civilization;
import Console.Command;
import Game.GameController;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * @author diego
 */
public class Server {
    private final int PORT = 35500;
    private final int maxConections = 4;
    private ServerSocket serverSocket;
    private ArrayList<ThreadServidor> connectedClients;
    FrameServer refFrame;
    private ThreadConnections connectionsThread;
    
    // GameController
    private GameController gameController;
    
    public Server(FrameServer refFrame) {
        connectedClients = new ArrayList<ThreadServidor>();
        this.refFrame = refFrame;
        
        // Inicializar GameController 
        this.gameController = new GameController();
        
        this.init();
        connectionsThread = new ThreadConnections(this);
        connectionsThread.start();
    }
    
    // Método que inicializa el server
    private void init(){
        try {
            serverSocket = new ServerSocket(PORT);
            refFrame.writeMessage("Server running!!!");
            refFrame.writeMessage("GameController inicializado");
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
                refFrame.writeMessage("Error broadcast: " + ex.getMessage());
            }
        }
    }
    
    public void sendPrivate(Command comando){
        // Nombre del cliente en la posición 1
        if (comando.getParameters().length <= 1)
            return;
        
        String searchName = comando.getParameters()[1];
        
        for (ThreadServidor client : connectedClients) {
            if (client.name.equals(searchName)){
                try {
                    client.objectSender.writeObject(comando);
                    break;
                } catch (IOException ex) {
                    refFrame.writeMessage("Error private message: " + ex.getMessage());
                }
            }
        }
    }
    
    // Método para registrar civilización
    public boolean registerCivilization(String playerName) {
        // Verificar si ya existe
        if (gameController.getCivilization(playerName) != null) {
            refFrame.writeMessage("Civilización " + playerName + " ya existe");
            return false;
        }
        
        // Crear nueva civilización
        Civilization newCiv = new Civilization(playerName);
        boolean added = gameController.addCivilization(newCiv);
        
        if (added) {
            refFrame.writeMessage("Civilización registrada: " + playerName);
            refFrame.writeMessage("Total civilizaciones: " + 
                gameController.getCivilizationCount());
            
            // Asociar civilización con el ThreadServidor correspondiente
            for (ThreadServidor thread : connectedClients) {
                if (thread.name != null && thread.name.equals(playerName)) {
                    thread.setCivilization(newCiv);
                    refFrame.writeMessage("Civilización asociada a thread: " + playerName);
                    break;
                }
            }
        } else {
            refFrame.writeMessage("No se pudo registrar civilización (máximo 4)");
        }
        
        return added;
    }
    
    // Iniciar juego 
    public void startGame() {
        try {
            if (!gameController.allCivilizationsReady()) {
                refFrame.writeMessage("ERROR: No todas las civilizaciones están listas");
                refFrame.writeMessage("Cada civilización necesita 3 fighters");
                return;
            }
            
            gameController.startGame();
            refFrame.writeMessage("¡JUEGO INICIADO!");
            refFrame.writeMessage("Orden de turnos: " + gameController.getTurnOrder());
            refFrame.writeMessage("Turno actual: " + gameController.getCurrentPlayer());
            
            // Notificar a todos los clientes
            // (Aquí podrías enviar un comando broadcast con el estado inicial)
            
        } catch (Exception e) {
            refFrame.writeMessage("Error al iniciar juego: " + e.getMessage());
        }
    }
    
    // Mostrar estado del juego
    public void showGameStatus() {
        refFrame.writeMessage("\n" + gameController.getGameStatus());
    }
    
    public void showAllNames(){
        refFrame.writeMessage("=== Usuarios conectados ===");
        for (ThreadServidor client : connectedClients) {
            String civInfo = "";
            if (client.getCivilization() != null) {
                civInfo = " [Civilization: " + client.getCivilization().getName() + "]";
            }
            refFrame.writeMessage("- " + client.name + civInfo);
        }
        refFrame.writeMessage("Total: " + connectedClients.size() + "/" + maxConections);
    }
    
    // GETTERS
    
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
    
    // Getter GameController
    public GameController getGameController() {
        return gameController;
    }
}