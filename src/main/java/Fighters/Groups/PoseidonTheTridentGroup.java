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
public class PoseidonTheTridentGroup extends Fighter {

    private Random random = new Random();
    private boolean canControlKraken = false;
    private List<String> lastKrakenAttack = null;

    public PoseidonTheTridentGroup(String name, String imagePath, double power,
            double resistance, double sanity,
            int percentagleOfCivilization) {
        super(name, imagePath, power, resistance, sanity,
                percentagleOfCivilization);
    }

    /*
     * Three Lines: 3 puntos que destruyen 1-4 casillas en direcciones cardinales
     */
    public List<String> attackThreeLines(MapGrid targetMap, int[] x, int[] y) {
        List<String> log = new ArrayList<>();

        if (x.length != 3 || y.length != 3) {
            log.add("ERROR: Se requieren exactamente 3 coordenadas");
            return log;
        }

        log.add("=== ATAQUE: THREE LINES ===");
        String[] directions = {"ARRIBA", "ABAJO", "IZQUIERDA", "DERECHA"};
        int totalCells = 0;

        for (int i = 0; i < 3; i++) {
            int distance = random.nextInt(4) + 1; // 1-4 casillas
            String direction = directions[random.nextInt(4)];

            log.add("Línea " + (i + 1) + " desde (" + x[i] + "," + y[i]
                    + ") hacia " + direction + " - " + distance + " casillas");

            int dx = 0, dy = 0;
            switch (direction) {
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
            }

            for (int j = 1; j <= distance; j++) {
                int nx = x[i] + (dx * j);
                int ny = y[i] + (dy * j);

                if (isValidPosition(targetMap, nx, ny)) {
                    int damage = (int) (100 * getPower());
                    targetMap.applyDamage(nx, ny, damage);
                    totalCells++;
                    log.add("  └─ Celda (" + nx + "," + ny + ") dañada: " + damage + "%");
                }
            }
        }

        log.add("Total de celdas afectadas: " + totalCells);
        return log;
    }

    /*
     * Three Numbers: Juego de adivinanza con números
     */
    public List<String> attackThreeNumbers(MapGrid targetMap, int[] tridentNumbers,
            int[] playerGuess) {
        List<String> log = new ArrayList<>();
        log.add("=== ATAQUE: THREE NUMBERS ===");
        log.add("Números del Tridente: [" + tridentNumbers[0] + ", "
                + tridentNumbers[1] + ", " + tridentNumbers[2] + "]");
        log.add("Números del Jugador: [" + playerGuess[0] + ", "
                + playerGuess[1] + ", " + playerGuess[2] + "]");

        int matches = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tridentNumbers[i] == playerGuess[j]) {
                    matches++;
                    break;
                }
            }
        }

        log.add("¡Números coincidentes: " + matches + "!");

        if (matches > 0) {
            int explosionPower = tridentNumbers[0] * tridentNumbers[1] * tridentNumbers[2];
            int cellsToDestroy = Math.min(explosionPower, 100); // Limitar explosión

            log.add("¡EXPLOSIÓN! Poder: " + explosionPower);
            log.add("Destruyendo " + cellsToDestroy + " casillas aleatorias...");

            Cell[][] grid = targetMap.getGrid();
            int destroyed = 0;

            for (int i = 0; i < cellsToDestroy; i++) {
                int rx = random.nextInt(grid.length);
                int ry = random.nextInt(grid[0].length);

                int damage = (int) (100 * getPower());
                targetMap.applyDamage(rx, ry, damage);
                destroyed++;
            }

            log.add("Celdas destruidas: " + destroyed);
        } else {
            log.add("¡Ningún número coincidió! El ataque falló.");
        }

        return log;
    }

    /*
     * Control the Kraken: Devuelve un ataque Kraken al enemigo
     */
    public List<String> activateControlKraken(List<String> krakenAttackReceived) {
        List<String> log = new ArrayList<>();
        log.add("=== CONTROL THE KRAKEN - ACTIVADO ===");
        log.add("¡Poseidón toma control del Kraken enemigo!");
        log.add("El ataque es devuelto al atacante...");

        // Almacenar para aplicar en el siguiente turno
        this.lastKrakenAttack = new ArrayList<>(krakenAttackReceived);
        this.canControlKraken = true;

        log.add("El Kraken atacará a su invocador en el siguiente turno");
        return log;
    }

    private boolean isValidPosition(MapGrid map, int x, int y) {
        Cell[][] grid = map.getGrid();
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }
}
