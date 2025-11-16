/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game;

import Civilization.Civilization;
import Game.GameMap.Cell;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *
 * @author gabri
 */
/*
 * Controlador principal del juego que gestiona turnos, civilizaciones y victoria
 */
public class GameController {
    private List<Civilization> civilizations;
    private List<String> turnOrder;  // Nombres en orden de turno
    private int currentTurnIndex;
    private boolean gameStarted;
    private boolean gameEnded;
    private String winner;
    
    public GameController() {
        this.civilizations = new ArrayList<>();
        this.turnOrder = new ArrayList<>();
        this.currentTurnIndex = 0;
        this.gameStarted = false;
        this.gameEnded = false;
        this.winner = null;
    }
    
    /**
     * Agrega una civilización al juego (máximo 4)
     */
    public boolean addCivilization(Civilization civ) {
        if (civilizations.size() >= 4) {
            return false;
        }
        
        if (gameStarted) {
            return false;
        }
        
        civilizations.add(civ);
        turnOrder.add(civ.getName());
        return true;
    }
    
    /**
     * Inicia el juego estableciendo orden aleatorio de turnos
     */
    public void startGame() {
        if (civilizations.isEmpty()) {
            throw new IllegalStateException("No hay civilizaciones registradas");
        }
        
        // Inicializar mapas para cada civilización
        for (Civilization civ : civilizations) {
            if (civ.getFighters().size() == 3) {
                civ.initializeMap(20, 30); // 600 celdas
            }
        }
        
        // Orden aleatorio
        Collections.shuffle(turnOrder);
        gameStarted = true;
        currentTurnIndex = 0;
        
        System.out.println("¡Juego iniciado!");
        System.out.println("Orden de turnos: " + turnOrder);
    }
    
    /**
     * Obtiene el nombre del jugador actual
     */
    public String getCurrentPlayer() {
        if (!gameStarted || turnOrder.isEmpty()) {
            return null;
        }
        return turnOrder.get(currentTurnIndex);
    }
    
    /**
     * Avanza al siguiente turno
     */
    public void nextTurn() {
        if (!gameStarted || gameEnded) {
            return;
        }
        
        currentTurnIndex = (currentTurnIndex + 1) % turnOrder.size();
        checkVictory();
        
        System.out.println("Turno actual: " + getCurrentPlayer());
    }
    
    /**
     * Verifica si es el turno del jugador especificado
     */
    public boolean isPlayerTurn(String playerName) {
        if (!gameStarted || playerName == null) {
            return false;
        }
        
        String current = getCurrentPlayer();
        return current != null && current.equalsIgnoreCase(playerName);
    }
    
    /**
     * Verifica si hay un ganador
     */
    private void checkVictory() {
        List<Civilization> alive = new ArrayList<>();
        
        for (Civilization civ : civilizations) {
            if (!civ.isDefeated()) {
                alive.add(civ);
            }
        }
        
        if (alive.size() == 1) {
            gameEnded = true;
            winner = alive.get(0).getName();
            System.out.println("¡VICTORIA! Ganador: " + winner);
        } else if (alive.isEmpty()) {
            gameEnded = true;
            winner = "Empate";
            System.out.println("¡EMPATE! Todas las civilizaciones fueron destruidas");
        }
    }
    
    /**
     * Busca una civilización por nombre
     */
    public Civilization getCivilization(String name) {
        if (name == null) return null;
        
        for (Civilization civ : civilizations) {
            if (civ.getName().equalsIgnoreCase(name)) {
                return civ;
            }
        }
        return null;
    }
    
    /**
     * Verifica si todas las civilizaciones están listas (3 fighters cada una)
     */
    public boolean allCivilizationsReady() {
        if (civilizations.isEmpty()) {
            return false;
        }
        
        for (Civilization civ : civilizations) {
            if (civ.getFighters().size() != 3) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Obtiene el estado del juego como texto
     */
    public String getGameStatus() {
        StringBuilder status = new StringBuilder();
        status.append("=== ESTADO DEL JUEGO ===\n");
        status.append("Civilizaciones: ").append(civilizations.size()).append("\n");
        status.append("Juego iniciado: ").append(gameStarted ? "Sí" : "No").append("\n");
        
        if (gameStarted) {
            status.append("Turno actual: ").append(getCurrentPlayer()).append("\n");
        }
        
        if (gameEnded) {
            status.append("¡JUEGO TERMINADO!\n");
            status.append("Ganador: ").append(winner).append("\n");
        }
        
        status.append("\nCivilizaciones:\n");
        for (Civilization civ : civilizations) {
            status.append("- ").append(civ.getName())
                  .append(": ").append(civ.getFighters().size())
                  .append(" fighters");
            
            if (civ.getMap() != null) {
                status.append(", Vida: ")
                      .append(String.format("%.1f%%", civ.getMap().getAlivePercentage()));
            }
            
            if (civ.isDefeated()) {
                status.append(" [DERROTADA]");
            }
            status.append("\n");
        }
        
        return status.toString();
    }
    
    /**
     * Fuerza el fin del juego (para rendirse)
     */
    public void forceEndGame(String defeatedPlayer) {
        Civilization defeated = getCivilization(defeatedPlayer);
        if (defeated != null && defeated.getMap() != null) {
            // Destruir todas las celdas
            Cell[][] grid = defeated.getMap().getGrid();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[i][j] != null) {
                        grid[i][j].applyDamage(100);
                    }
                }
            }
        }
        checkVictory();
    }
    
    //GETTERS
    
    public boolean isGameStarted() {
        return gameStarted;
    }
    
    public boolean isGameEnded() {
        return gameEnded;
    }
    
    public String getWinner() {
        return winner;
    }
    
    public List<Civilization> getCivilizations() {
        return new ArrayList<>(civilizations);
    }
    
    public List<String> getTurnOrder() {
        return new ArrayList<>(turnOrder);
    }
    
    public int getCurrentTurnIndex() {
        return currentTurnIndex;
    }
    
    public int getCivilizationCount() {
        return civilizations.size();
    }
}
