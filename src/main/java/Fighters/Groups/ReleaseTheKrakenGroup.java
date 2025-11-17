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
public class ReleaseTheKrakenGroup extends Fighter {

    //TODO: Metodos de ataque de clase
    private Random random = new Random();

    public ReleaseTheKrakenGroup(String name, String imagePath, double power,
            double resistance, double sanity,
            int percentagleOfCivilization) {
        super(name, imagePath, power, resistance, sanity,
                percentagleOfCivilization);
    }

    /*
     Tentáculos: Coloca 3 tentáculos que destruyen en radio de 1 casilla alrededor
     */
    public List<String> attackTentaculos(MapGrid targetMap, int[] x, int[] y) {
        List<String> log = new ArrayList<>();

        if (x.length != 3 || y.length != 3) {
            log.add("ERROR: Se requieren exactamente 3 coordenadas");
            return log;
        }

        log.add("=== ATAQUE: TENTÁCULOS ===");
        int totalDamage = 0;

        for (int i = 0; i < 3; i++) {
            log.add("Tentáculo " + (i + 1) + " en posición (" + x[i] + "," + y[i] + ")");

            // Destruir en radio de 1 casilla
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int nx = x[i] + dx;
                    int ny = y[i] + dy;

                    if (isValidPosition(targetMap, nx, ny)) {
                        int damage = (int) (100 * getPower());
                        targetMap.applyDamage(nx, ny, damage);
                        totalDamage++;
                        log.add("  └─ Celda (" + nx + "," + ny + ") destruida: " + damage + "%");
                    }
                }
            }
        }

        log.add("Total de celdas afectadas: " + totalDamage);
        return log;
    }

    /*
     Kraken Breath: Aliento en una dirección que destruye 1-8 casillas
     */
    public List<String> attackKrakenBreath(MapGrid targetMap, int x, int y, String direction) {
        List<String> log = new ArrayList<>();
        log.add("=== ATAQUE: KRAKEN BREATH ===");
        log.add("Origen: (" + x + "," + y + ") Dirección: " + direction);

        int distance = random.nextInt(8) + 1; // 1-8 casillas
        int dx = 0, dy = 0;

        switch (direction.toUpperCase()) {
            case "ARRIBA":
                dy = -1;
                break;
            case "ABAJO":
                dy = 1;
                break;
            case "IZQUIERDA":
                dx = -1;
                break;
            case "DERECHA":
                dx = 1;
                break;
            default:
                log.add("ERROR: Dirección inválida. Use: ARRIBA, ABAJO, IZQUIERDA, DERECHA");
                return log;
        }

        log.add("Alcance del aliento: " + distance + " casillas");
        int cellsDestroyed = 0;

        for (int i = 1; i <= distance; i++) {
            int nx = x + (dx * i);
            int ny = y + (dy * i);

            if (isValidPosition(targetMap, nx, ny)) {
                int damage = (int) (100 * getPower());
                targetMap.applyDamage(nx, ny, damage);
                cellsDestroyed++;
                log.add("  └─ Celda (" + nx + "," + ny + ") dañada: " + damage + "%");
            }
        }

        log.add("Celdas destruidas: " + cellsDestroyed);
        return log;
    }

    /*
     * Release the Kraken: Destrucción masiva en radio aleatorio 1-9
     */
    public List<String> attackReleaseTheKraken(MapGrid targetMap, int x, int y) {
        List<String> log = new ArrayList<>();
        log.add("=== ¡¡¡RELEASE THE KRAKEN!!! ===");
        log.add("Epicentro: (" + x + "," + y + ")");

        int radius = random.nextInt(9) + 1; // 1-9 casillas de radio
        log.add("Radio de destrucción: " + radius + " casillas");

        int cellsDestroyed = 0;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                // Verificar que esté dentro del radio circular
                if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                    int nx = x + dx;
                    int ny = y + dy;

                    if (isValidPosition(targetMap, nx, ny)) {
                        int damage = (int) (100 * getPower());
                        targetMap.applyDamage(nx, ny, damage);
                        cellsDestroyed++;
                    }
                }
            }
        }

        log.add("DEVASTACIÓN TOTAL: " + cellsDestroyed + " celdas aniquiladas");
        return log;
    }

    private boolean isValidPosition(MapGrid map, int x, int y) {
        Cell[][] grid = map.getGrid();
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }
}
