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
public class UnderseaFireGroup extends Fighter {

    private Random random = new Random();
    private Map<String, Integer> volcanoes = new HashMap<>(); // "x,y" -> radius

    public UnderseaFireGroup(String name, String imagePath, double power,
            double resistance, double sanity,
            int percentagleOfCivilization, String attackGroup) {
        super(name, imagePath, power, resistance, sanity,
                percentagleOfCivilization, attackGroup);
    }

    //TODO: Insertar metodos de ataque
    /*
     * Volcano Raising: Crea un volcán con radio 1-10
     */
    public List<String> attackVolcanoRaising(MapGrid targetMap, int x, int y) {
        List<String> log = new ArrayList<>();
        log.add("=== ATAQUE: VOLCANO RAISING ===");

        int radius = random.nextInt(10) + 1; // 1-10 casillas
        log.add("¡Un volcán emerge en (" + x + "," + y + ")!");
        log.add("Radio del volcán: " + radius + " casillas");

        // Guardar el volcán para futuros ataques
        volcanoes.put(x + "," + y, radius);

        int cellsDestroyed = 0;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                    int nx = x + dx;
                    int ny = y + dy;

                    if (isValidPosition(targetMap, nx, ny)) {
                        int damage = (int) (100 * getPower());
                        targetMap.applyDamage(nx, ny, damage);

                        // Marcar celda como ocupada por volcán
                        Cell cell = targetMap.getCell(nx, ny);
                        // cell.setHasVolcano(true); // Necesitarías agregar setters
                        cellsDestroyed++;
                    }
                }
            }
        }

        log.add("Celdas destruidas por el volcán: " + cellsDestroyed);
        log.add("Volcán guardado para futuras erupciones");
        return log;
    }

    /**
     * Volcano Explosion: Erupción de un volcán existente
     */
    public List<String> attackVolcanoExplosion(MapGrid targetMap, int x, int y) {
        List<String> log = new ArrayList<>();
        log.add("=== ATAQUE: VOLCANO EXPLOSION ===");

        String volcanoKey = x + "," + y;
        if (!volcanoes.containsKey(volcanoKey)) {
            log.add("ERROR: No existe volcán en (" + x + "," + y + ")");
            log.add("Volcanes disponibles: " + volcanoes.keySet());
            return log;
        }

        int volcanoRadius = volcanoes.get(volcanoKey);
        int rocks = volcanoRadius * 10; // 10 veces el tamaño

        log.add("¡El volcán en (" + x + "," + y + ") hace ERUPCIÓN!");
        log.add("Radio original: " + volcanoRadius);
        log.add("Piedras lanzadas: " + rocks);

        Cell[][] grid = targetMap.getGrid();
        int cellsAffected = 0;

        for (int i = 0; i < rocks; i++) {
            int rx = random.nextInt(grid.length);
            int ry = random.nextInt(grid[0].length);

            // Cada piedra daña 20%
            int damage = (int) (20 * getPower());
            targetMap.applyDamage(rx, ry, damage);
            cellsAffected++;
        }

        log.add("Celdas impactadas por rocas volcánicas: " + cellsAffected);
        return log;
    }

    /*
     * Termal Rush: Sobrecalentamiento alrededor de un volcán por 5-6 segundos
     */
    public List<String> attackTermalRush(MapGrid targetMap, int x, int y) {
        List<String> log = new ArrayList<>();
        log.add("=== ATAQUE: TERMAL RUSH ===");

        String volcanoKey = x + "," + y;
        if (!volcanoes.containsKey(volcanoKey)) {
            log.add("ERROR: No existe volcán en (" + x + "," + y + ")");
            return log;
        }

        int volcanoRadius = volcanoes.get(volcanoKey);
        int rushRadius = 5; // Radio fijo de 5 casillas
        int duration = random.nextInt(2) + 5; // 5-6 segundos

        log.add("¡Sobrecalentamiento termal alrededor del volcán!");
        log.add("Radio de efecto: " + rushRadius + " casillas");
        log.add("Duración: " + duration + " segundos");
        log.add("Daño por segundo: " + volcanoRadius + "%");

        int totalDamage = 0;
        List<int[]> affectedCells = new ArrayList<>();

        // Identificar celdas afectadas
        for (int dx = -rushRadius; dx <= rushRadius; dx++) {
            for (int dy = -rushRadius; dy <= rushRadius; dy++) {
                if (Math.sqrt(dx * dx + dy * dy) <= rushRadius) {
                    int nx = x + dx;
                    int ny = y + dy;

                    if (isValidPosition(targetMap, nx, ny)) {
                        affectedCells.add(new int[]{nx, ny});
                    }
                }
            }
        }

        log.add("Celdas en zona termal: " + affectedCells.size());

        // Aplicar daño por cada segundo
        for (int second = 1; second <= duration; second++) {
            log.add("Segundo " + second + ":");

            for (int[] cell : affectedCells) {
                int damage = (int) (volcanoRadius * getPower());
                targetMap.applyDamage(cell[0], cell[1], damage);
                totalDamage++;
            }
        }

        log.add("Daño total aplicado: " + (totalDamage * volcanoRadius) + "%");
        return log;
    }

    public Map<String, Integer> getVolcanoes() {
        return volcanoes;
    }

    private boolean isValidPosition(MapGrid map, int x, int y) {
        Cell[][] grid = map.getGrid();
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }
}
