/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fighters.Groups;

import Fighters.Fighter;
import Game.GameMap.Cell;
import Game.GameMap.MapGrid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author kyup
 */
public class ThundersUnderTheSeaGroup extends Fighter {

    private Random random = new Random();

    public ThundersUnderTheSeaGroup(String name, String imagePath, double power,
            double resistance, double sanity,
            int percentagleOfCivilization) {
        super(name, imagePath, power, resistance, sanity,
                percentagleOfCivilization);
    }

    /**
     * Thunder Rain: 100 rayos que dañan 10-20% cada uno
     */
    public List<String> attackThunderRain(MapGrid targetMap) {
        List<String> log = new ArrayList<>();
        log.add("=== ATAQUE: THUNDER RAIN ===");
        log.add("¡Una tormenta de 100 rayos cae del cielo!");

        Cell[][] grid = targetMap.getGrid();
        int cellsAffected = 0;
        int[][] hitCount = new int[grid.length][grid[0].length];

        for (int i = 0; i < 100; i++) {
            int rx = random.nextInt(grid.length);
            int ry = random.nextInt(grid[0].length);

            int damage = random.nextInt(11) + 10; // 10-20%
            damage = (int) (damage * getPower());

            targetMap.applyDamage(rx, ry, damage);
            hitCount[rx][ry]++;
        }

        // Contar celdas únicas afectadas
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (hitCount[i][j] > 0) {
                    cellsAffected++;
                }
            }
        }

        log.add("Rayos lanzados: 100");
        log.add("Celdas únicas afectadas: " + cellsAffected);
        return log;
    }

    /**
     * Poseidon Thunders: 5-10 rayos con onda de destrucción radio 2-10
     */
    public List<String> attackPoseidonThunders(MapGrid targetMap) {
        List<String> log = new ArrayList<>();
        log.add("=== ATAQUE: POSEIDON THUNDERS ===");

        int numThunders = random.nextInt(6) + 5; // 5-10 rayos
        log.add("¡" + numThunders + " rayos divinos de Poseidón!");

        Cell[][] grid = targetMap.getGrid();
        int totalDestroyed = 0;

        for (int t = 0; t < numThunders; t++) {
            int rx = random.nextInt(grid.length);
            int ry = random.nextInt(grid[0].length);
            int radius = random.nextInt(9) + 2; // 2-10 casillas

            log.add("Rayo " + (t + 1) + " impacta en (" + rx + "," + ry
                    + ") - Radio de onda: " + radius);

            // Daño directo en punto de impacto
            targetMap.applyDamage(rx, ry, (int) (100 * getPower()));
            totalDestroyed++;

            // Onda de destrucción
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                        int nx = rx + dx;
                        int ny = ry + dy;

                        if (isValidPosition(targetMap, nx, ny)
                                && (nx != rx || ny != ry)) { // No contar el centro dos veces

                            int damage = (int) (100 * getPower());
                            targetMap.applyDamage(nx, ny, damage);
                            totalDestroyed++;
                        }
                    }
                }
            }
        }

        log.add("Celdas totales destruidas: " + totalDestroyed);
        return log;
    }

    /*
     * Eel Attack: 25-100 anguilas con 1-10 descargas cada una (10% por descarga)
     */
    public List<String> attackEelAttack(MapGrid targetMap) {
        List<String> log = new ArrayList<>();
        log.add("=== ATAQUE: EEL ATTACK ===");

        int numEels = random.nextInt(76) + 25; // 25-100 anguilas
        log.add("¡" + numEels + " anguilas eléctricas atacan!");

        Cell[][] grid = targetMap.getGrid();
        int totalDischarges = 0;
        int[][] damageCount = new int[grid.length][grid[0].length];

        for (int i = 0; i < numEels; i++) {
            int discharges = random.nextInt(10) + 1; // 1-10 descargas
            int rx = random.nextInt(grid.length);
            int ry = random.nextInt(grid[0].length);

            for (int d = 0; d < discharges; d++) {
                // Cada descarga daña 10%
                int damage = (int) (10 * getPower());
                targetMap.applyDamage(rx, ry, damage);
                totalDischarges++;
                damageCount[rx][ry]++;
            }
        }

        // Contar celdas afectadas
        int cellsAffected = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (damageCount[i][j] > 0) {
                    cellsAffected++;
                }
            }
        }

        log.add("Anguilas: " + numEels);
        log.add("Descargas totales: " + totalDischarges);
        log.add("Celdas afectadas: " + cellsAffected);
        return log;
    }

    private boolean isValidPosition(MapGrid map, int x, int y) {
        Cell[][] grid = map.getGrid();
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }
}
