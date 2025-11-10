/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fighters.Groups;

import Fighters.Fighter;
import Game.GameMap.Cell;
import Game.GameMap.MapGrid;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author kyup
 */
public class FishTelepathyGroup extends Fighter {

    private Random random = new Random();

    public FishTelepathyGroup(String name, String imagePath, double power,
            double resistance, double sanity,
            int percentagleOfCivilization, String attackGroup) {
        super(name, imagePath, power, resistance, sanity,
                percentagleOfCivilization, attackGroup);
    }

    // TODO: Insertar métodos de ataques de clase
    /*
     * Cardumen: 100-300 peces que dañan 33% cada uno
     */
    public List<String> attackCardumen(MapGrid targetMap) {
        List<String> log = new ArrayList<>();
        log.add("=== ATAQUE: CARDUMEN ===");

        int numFish = random.nextInt(201) + 100; // 100-300 peces
        log.add("¡" + numFish + " peces atacan!");

        Cell[][] grid = targetMap.getGrid();
        int[] damageCount = new int[grid.length * grid[0].length];

        for (int i = 0; i < numFish; i++) {
            int rx = random.nextInt(grid.length);
            int ry = random.nextInt(grid[0].length);

            int damage = (int) (33 * getPower());
            targetMap.applyDamage(rx, ry, damage);
            damageCount[rx * grid[0].length + ry]++;
        }

        int cellsAffected = 0;
        for (int count : damageCount) {
            if (count > 0) {
                cellsAffected++;
            }
        }

        log.add("Celdas afectadas: " + cellsAffected);
        log.add("Ataques totales: " + numFish);
        return log;
    }

    /*
     * Shark Attack: Tiburones atacan desde las 4 esquinas con radio 1-10
     */
    public List<String> attackSharkAttack(MapGrid targetMap) {
        List<String> log = new ArrayList<>();
        log.add("=== ATAQUE: SHARK ATTACK ===");
        log.add("¡Tiburones atacan desde las 4 esquinas!");

        Cell[][] grid = targetMap.getGrid();
        int[][] corners = {
            {0, 0}, // Superior izquierda
            {0, grid[0].length - 1}, // Superior derecha
            {grid.length - 1, 0}, // Inferior izquierda
            {grid.length - 1, grid[0].length - 1} // Inferior derecha
        };

        int totalDestroyed = 0;

        for (int i = 0; i < 4; i++) {
            int radius = random.nextInt(10) + 1; // 1-10 casillas
            int cx = corners[i][0];
            int cy = corners[i][1];

            log.add("Esquina " + (i + 1) + " (" + cx + "," + cy + ") - Radio: " + radius);

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                        int nx = cx + dx;
                        int ny = cy + dy;

                        if (isValidPosition(targetMap, nx, ny)) {
                            int damage = (int) (100 * getPower());
                            targetMap.applyDamage(nx, ny, damage);
                            totalDestroyed++;
                        }
                    }
                }
            }
        }

        log.add("Total de celdas destruidas: " + totalDestroyed);
        return log;
    }

    /*
     * Pulp: 20-50 pulpos con 8 tentáculos cada uno. 4 tentáculos = destrucción
     */
    public List<String> attackPulp(MapGrid targetMap) {
        List<String> log = new ArrayList<>();
        log.add("=== ATAQUE: PULP ===");

        int numOctopuses = random.nextInt(31) + 20; // 20-50 pulpos
        log.add("¡" + numOctopuses + " pulpos atacan con sus tentáculos!");

        Cell[][] grid = targetMap.getGrid();
        int[][] tentacleHits = new int[grid.length][grid[0].length];

        // Cada pulpo lanza 8 tentáculos a celdas aleatorias
        for (int i = 0; i < numOctopuses; i++) {
            for (int t = 0; t < 8; t++) {
                int rx = random.nextInt(grid.length);
                int ry = random.nextInt(grid[0].length);

                tentacleHits[rx][ry]++;

                // Cada tentáculo daña 25% de la celda
                int damage = (int) (25 * getPower());
                targetMap.applyDamage(rx, ry, damage);
            }
        }

        // Contar celdas que recibieron 4+ tentáculos (destruidas)
        int destroyed = 0;
        int affected = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (tentacleHits[i][j] > 0) {
                    affected++;
                    if (tentacleHits[i][j] >= 4) {
                        destroyed++;
                    }
                }
            }
        }

        log.add("Celdas afectadas: " + affected);
        log.add("Celdas completamente destruidas (4+ tentáculos): " + destroyed);
        log.add("Total de tentáculos lanzados: " + (numOctopuses * 8));
        return log;
    }

    private boolean isValidPosition(MapGrid map, int x, int y) {
        Cell[][] grid = map.getGrid();
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }
}
