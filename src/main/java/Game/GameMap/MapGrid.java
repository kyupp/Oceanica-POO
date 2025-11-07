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
    
    public void applyDamage(int x, int y, int percent){
        //TODO: Implementar la logica, la celda tambien tiene applyDamage()
    }
    
    public void healArea(int x, int y, int percent){
        //TODO; Implementar la lógica, aun no estoy seguro de si se pueden curar
        //consultar.
    }
    
    public Cell getCell(int x, int y){
        return grid[x][y];
    }
    
    public void addCell(int x, int y, Cell cell){
        
        grid[x][y] = cell;
        
    }
    
    public ArrayList<Cell> getCellsByFighter(Fighter f){
        ArrayList<Cell> fighterList = new ArrayList<Cell>();
        //TODO: Recorrer todas las ceeldas,
        // añadirlas al arraylist, y retornar el arraylist
        return fighterList;
    }
    
    public double getAlivePercentage(){
        double aliveP = 0;
        //TODO: Calcular el porcentaje vivo y retornarlo
        return aliveP;
    }

    public Cell[][] getGrid() {
        return grid;
    }
    
}
