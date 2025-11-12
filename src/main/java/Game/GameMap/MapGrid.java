/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game.GameMap;

import Fighters.Fighter;
import java.util.ArrayList;

/**
 *
 * @author kyup
 */
public class MapGrid {

    private Cell[][] grid;
    private int width;
    private int height;

    public MapGrid(Cell[][] grid, int width, int height) {
        this.grid = grid;
        this.width = width;
        this.height = height;
    }

    public Cell getCell(int x, int y) {
        return grid[x][y];
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void applyDamage(int x, int y, int percent) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            Cell cell = grid[x][y];
            if (cell != null && !cell.isDestroyed()) {
                cell.applyDamage(percent);
            }
        }
    }
    
    public void addCell(int fila, int columna, Cell celda){
        
        grid[fila][columna] = celda;
        
    }

    public void healArea(int x, int y, int radius, int percent) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                    int nx = x + dx;
                    int ny = y + dy;

                    if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                        Cell cell = grid[nx][ny];
                        if (cell != null && !cell.isDestroyed()) {
                            cell.heal(percent);
                        }
                    }
                }
            }
        }
    }

    public ArrayList<Cell> getCellsByFighter(Fighter f) {
        ArrayList<Cell> fighterCells = new ArrayList<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = grid[i][j];
                if (cell != null && cell.getOwner() != null
                        && cell.getOwner().equals(f)) {
                    fighterCells.add(cell);
                }
            }
        }

        return fighterCells;
    }

    public double getAlivePercentage() {
        int totalCells = 0;
        int aliveCells = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j] != null) {
                    totalCells++;
                    if (!grid[i][j].isDestroyed()) {
                        aliveCells++;
                    }
                }
            }
        }

        return totalCells > 0 ? (double) aliveCells / totalCells * 100.0 : 0.0;
    }

    public double getFighterAlivePercentage(Fighter f) {
        ArrayList<Cell> fighterCells = getCellsByFighter(f);
        int totalCells = fighterCells.size();
        int aliveCells = 0;

        for (Cell cell : fighterCells) {
            if (!cell.isDestroyed()) {
                aliveCells++;
            }
        }

        return totalCells > 0 ? (double) aliveCells / totalCells * 100.0 : 0.0;
    }

}
