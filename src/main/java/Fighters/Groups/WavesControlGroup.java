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
public class WavesControlGroup extends Fighter {

    private Random random = new Random();
    private Map<String, Integer> swirls = new HashMap<>(); // "x,y" -> radius
    private Map<String, Boolean> radioactiveGarbage = new HashMap<>(); // "x,y" -> isRadioactive

    public WavesControlGroup(String name, String imagePath, double power,
            double resistance, double sanity,
            int percentagleOfCivilization, String attackGroup) {
        super(name, imagePath, power, resistance, sanity,
                percentagleOfCivilization, attackGroup);
    }

    /*
     * Swirl Raising: Crea un remolino con radio 2-10
     */
    public List<String> attackSwirlRaising(MapGrid targetMap, int x, int y) {
        List<String> log = new ArrayList<>();
        log.add("=== ATAQUE: SWIRL RAISING ===");

        int radius = random.nextInt(9) + 2; // 2-10 casillas
        log.add("¡Un remolino se forma en (" + x + "," + y + ")!");
        log.add("Radio del remolino: " + radius + " casillas");

        // Guardar el remolino para futuros ataques
        swirls.put(x + "," + y, radius);

        int cellsDestroyed = 0;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                    int nx = x + dx;
                    int ny = y + dy;

                    if (isValidPosition(targetMap, nx, ny)) {
                        int damage = (int) (100 * getPower());
                        targetMap.applyDamage(nx, ny, damage);

                        // Marcar celda como ocupada por remolino
                        Cell cell = targetMap.getCell(nx, ny);
                        // cell.setHasSwirl(true); // Necesitarías agregar setters
                        cellsDestroyed++;
                    }
                }
            }
        }

        log.add("Celdas destruidas por el remolino: " + cellsDestroyed);
        log.add("Remolino guardado para futuros ataques");
        return log;
    }

    /*
     * Send Human Garbage: Envía basura desde un remolino existente
     */
    public List<String> attackSendHumanGarbage(MapGrid targetMap, int x, int y) {
        List<String> log = new ArrayList<>();
        log.add("=== ATAQUE: SEND HUMAN GARBAGE ===");

        String swirlKey = x + "," + y;
        if (!swirls.containsKey(swirlKey)) {
            log.add("ERROR: No existe remolino en (" + x + "," + y + ")");
            log.add("Remolinos disponibles: " + swirls.keySet());
            return log;
        }

        int swirlRadius = swirls.get(swirlKey);
        int garbageTons = swirlRadius * 10; // 10 veces el radio

        log.add("¡El remolino en (" + x + "," + y + ") expulsa basura humana!");
        log.add("Radio del remolino: " + swirlRadius);
        log.add("Toneladas de basura: " + garbageTons);

        Cell[][] grid = targetMap.getGrid();
        int radioactiveCount = 0;
        int normalCount = 0;

        for (int i = 0; i < garbageTons; i++) {
            int rx = random.nextInt(grid.length);
            int ry = random.nextInt(grid[0].length);

            // Cada tonelada daña 25%
            int damage = (int) (25 * getPower());
            targetMap.applyDamage(rx, ry, damage);

            // 50% de probabilidad de ser radioactiva
            boolean isRadioactive = random.nextBoolean();
            String garbageKey = rx + "," + ry;

            if (isRadioactive) {
                radioactiveGarbage.put(garbageKey, true);
                radioactiveCount++;
            } else {
                normalCount++;
            }
        }

        log.add("Basura normal: " + normalCount + " toneladas");
        log.add("Basura radioactiva: " + radioactiveCount + " toneladas");
        log.add("¡La basura radioactiva permanece en el mapa!");
        return log;
    }

    /*
     * Radioactive Rush: Activa toda la basura radioactiva por 1-10 segundos
     */
    public List<String> attackRadioactiveRush(MapGrid targetMap) {
        List<String> log = new ArrayList<>();
        log.add("=== ATAQUE: RADIOACTIVE RUSH ===");

        if (radioactiveGarbage.isEmpty()) {
            log.add("ERROR: No hay basura radioactiva en el mapa");
            log.add("Usa 'Send Human Garbage' primero para crear basura radioactiva");
            return log;
        }

        int duration = random.nextInt(10) + 1; // 1-10 segundos
        log.add("¡ACTIVACIÓN RADIOACTIVA!");
        log.add("Duración: " + duration + " segundos");
        log.add("Toneladas radioactivas activas: " + radioactiveGarbage.size());
        log.add("Daño por segundo por tonelada: 10%");

        int totalDamage = 0;

        // Por cada segundo de duración
        for (int second = 1; second <= duration; second++) {
            log.add("Segundo " + second + ":");

            // Activar cada tonelada de basura radioactiva
            for (String garbagePos : radioactiveGarbage.keySet()) {
                String[] coords = garbagePos.split(",");
                int gx = Integer.parseInt(coords[0]);
                int gy = Integer.parseInt(coords[1]);

                // Cada tonelada daña 10% por segundo
                int damage = (int) (10 * getPower());
                targetMap.applyDamage(gx, gy, damage);
                totalDamage += damage;
            }
        }

        log.add("Daño total por radiación: " + totalDamage + "%");
        log.add("Celdas afectadas: " + radioactiveGarbage.size());
        return log;
    }

    public Map<String, Integer> getSwirls() {
        return swirls;
    }

    public Map<String, Boolean> getRadioactiveGarbage() {
        return radioactiveGarbage;
    }

    private boolean isValidPosition(MapGrid map, int x, int y) {
        Cell[][] grid = map.getGrid();
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }
}