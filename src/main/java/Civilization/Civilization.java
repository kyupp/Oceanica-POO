/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Civilization;

import Fighters.Fighter;
import Game.GameMap.Cell;
import Game.GameMap.MapGrid;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kyup
 */

public class Civilization {
    private String name;
    private List<Fighter> fighters;
    private MapGrid map;
    private List<String> battleLog;
    private int attacksExecuted;
    private int attacksSuccessful;
    
    public Civilization(String name) {
        this.name = name;
        this.fighters = new ArrayList<>();
        this.battleLog = new ArrayList<>();
        this.attacksExecuted = 0;
        this.attacksSuccessful = 0;
    }
    
    public void addFighter(Fighter f) {
        if (fighters.size() < 3) {
            fighters.add(f);
        }
    }
    
    public Fighter getFighter(String name) {
        for (Fighter f : fighters) {
            if (f.getName().equalsIgnoreCase(name)) {
                return f;
            }
        }
        return null;
    }
    
    public void initializeMap(int width, int height) {
        // Crear matriz y distribuir celdas entre fighters
        Cell[][] grid = new Cell[width][height];
        
        int totalCells = width * height;
        int cellIndex = 0;
        
        for (Fighter f : fighters) {
            int fighterCells = (int)(totalCells * f.getPercentagleOfCivilization() / 100.0);
            
            for (int i = 0; i < fighterCells && cellIndex < totalCells; i++) {
                int x = cellIndex / height;
                int y = cellIndex % height;
                
                grid[x][y] = new Cell(x, y, 100.0, f, new ArrayList<>(), false, false, false);
                cellIndex++;
            }
        }
        
        this.map = new MapGrid(grid, width, height);
    }
    
    public boolean isDefeated() {
        return map.getAlivePercentage() == 0;
    }
    
    public void logEvent(String event) {
        battleLog.add(event);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Fighter> getFighters() {
        return fighters;
    }

    public void setFighters(List<Fighter> fighters) {
        this.fighters = fighters;
    }

    public MapGrid getMap() {
        return map;
    }

    public void setMap(MapGrid map) {
        this.map = map;
    }

    public List<String> getBattleLog() {
        return battleLog;
    }

    public void setBattleLog(List<String> battleLog) {
        this.battleLog = battleLog;
    }

    public int getAttacksExecuted() {
        return attacksExecuted;
    }

    public void setAttacksExecuted(int attacksExecuted) {
        this.attacksExecuted = attacksExecuted;
    }

    public int getAttacksSuccessful() {
        return attacksSuccessful;
    }

    public void setAttacksSuccessful(int attacksSuccessful) {
        this.attacksSuccessful = attacksSuccessful;
    }
    
    
}