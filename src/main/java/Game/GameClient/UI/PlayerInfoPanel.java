package Game.GameClient.UI;

import Civilization.Civilization;
import Fighters.Fighter;
import Game.GameMap.Cell;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author gabri
 */
public class PlayerInfoPanel extends JPanel {
    
    private Civilization civilization;
    
    // Labels para información general
    private JLabel lblCivilizationName;
    private JLabel lblTotalLife;
    private JProgressBar pbTotalLife;
    private JLabel lblCellsDestroyed;
    private JLabel lblCellsAlive;
    
    // Panel para información de cada fighter
    private JPanel fightersStatsPanel;
    
    public PlayerInfoPanel(Civilization civilization) {
        this.civilization = civilization;
        initComponents();
        updateInfo();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(50, 117, 168), 2),
                " Estado del Territorio",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(50, 117, 168)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setBackground(Color.WHITE);
        
        // ===== PANEL IZQUIERDO: Información General =====
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));
        
        // Nombre de civilización
        lblCivilizationName = new JLabel();
        lblCivilizationName.setFont(new Font("Arial", Font.BOLD, 16));
        lblCivilizationName.setForeground(new Color(0, 70, 130));
        lblCivilizationName.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(lblCivilizationName);
        
        leftPanel.add(Box.createVerticalStrut(15));
        
        // Vida total
        JLabel lblLifeTitle = new JLabel(" Vida Total del Territorio:");
        lblLifeTitle.setFont(new Font("Arial", Font.BOLD, 12));
        lblLifeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(lblLifeTitle);
        
        leftPanel.add(Box.createVerticalStrut(5));
        
        lblTotalLife = new JLabel();
        lblTotalLife.setFont(new Font("Arial", Font.BOLD, 24));
        lblTotalLife.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(lblTotalLife);
        
        pbTotalLife = new JProgressBar(0, 100);
        pbTotalLife.setStringPainted(true);
        pbTotalLife.setFont(new Font("Arial", Font.BOLD, 12));
        pbTotalLife.setForeground(new Color(50, 180, 50));
        pbTotalLife.setBackground(new Color(230, 230, 230));
        pbTotalLife.setPreferredSize(new Dimension(200, 25));
        pbTotalLife.setMaximumSize(new Dimension(200, 25));
        pbTotalLife.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(pbTotalLife);
        
        leftPanel.add(Box.createVerticalStrut(15));
        
        // Casillas vivas
        JPanel cellsAlivePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        cellsAlivePanel.setOpaque(false);
        cellsAlivePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JLabel lblAliveIcon = new JLabel("");
        lblAliveIcon.setFont(new Font("Arial", Font.PLAIN, 14));
        lblCellsAlive = new JLabel();
        lblCellsAlive.setFont(new Font("Arial", Font.PLAIN, 12));
        cellsAlivePanel.add(lblAliveIcon);
        cellsAlivePanel.add(lblCellsAlive);
        cellsAlivePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(cellsAlivePanel);
        
        // Casillas destruidas
        JPanel cellsDestroyedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        cellsDestroyedPanel.setOpaque(false);
        cellsDestroyedPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JLabel lblDestroyedIcon = new JLabel("");
        lblDestroyedIcon.setFont(new Font("Arial", Font.PLAIN, 14));
        lblCellsDestroyed = new JLabel();
        lblCellsDestroyed.setFont(new Font("Arial", Font.PLAIN, 12));
        cellsDestroyedPanel.add(lblDestroyedIcon);
        cellsDestroyedPanel.add(lblCellsDestroyed);
        cellsDestroyedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(cellsDestroyedPanel);
        
        add(leftPanel, BorderLayout.WEST);
        
        // ===== PANEL DERECHO: Estadísticas por Fighter =====
        fightersStatsPanel = new JPanel();
        fightersStatsPanel.setLayout(new GridLayout(1, 3, 10, 0));
        fightersStatsPanel.setOpaque(false);
        
        add(fightersStatsPanel, BorderLayout.CENTER);
    }
    
    public void updateInfo() {
        if (civilization == null) return;
        
        // Actualizar nombre
        lblCivilizationName.setText(" " + civilization.getName());
        
        // Calcular estadísticas
        int totalCells = 0;
        int aliveCells = 0;
        int destroyedCells = 0;
        
        if (civilization.getMap() != null) {
            Cell[][] grid = civilization.getMap().getGrid();
            
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[i][j] != null) {
                        totalCells++;
                        if (grid[i][j].isDestroyed()) {
                            destroyedCells++;
                        } else {
                            aliveCells++;
                        }
                    }
                }
            }
        }
        
        // Actualizar vida total
        double lifePercent = totalCells > 0 ? (aliveCells * 100.0 / totalCells) : 100.0;
        lblTotalLife.setText(String.format("%.1f%%", lifePercent));
        pbTotalLife.setValue((int) lifePercent);
        pbTotalLife.setString(String.format("%.1f%%", lifePercent));
        
        // Cambiar color según vida
        if (lifePercent >= 70) {
            lblTotalLife.setForeground(new Color(0, 150, 0));
            pbTotalLife.setForeground(new Color(50, 180, 50));
        } else if (lifePercent >= 40) {
            lblTotalLife.setForeground(new Color(200, 150, 0));
            pbTotalLife.setForeground(new Color(220, 180, 50));
        } else {
            lblTotalLife.setForeground(new Color(200, 0, 0));
            pbTotalLife.setForeground(new Color(220, 50, 50));
        }
        
        // Actualizar casillas
        lblCellsAlive.setText(String.format("Casillas Vivas: %d / %d", aliveCells, totalCells));
        lblCellsDestroyed.setText(String.format("Casillas Destruidas: %d", destroyedCells));
        
        // Actualizar estadísticas por fighter
        updateFightersStats();
    }
    
    private void updateFightersStats() {
        fightersStatsPanel.removeAll();
        
        if (civilization.getFighters().isEmpty()) {
            JLabel lblNoFighters = new JLabel("No hay fighters todavía");
            lblNoFighters.setHorizontalAlignment(SwingConstants.CENTER);
            lblNoFighters.setFont(new Font("Arial", Font.ITALIC, 12));
            lblNoFighters.setForeground(Color.GRAY);
            fightersStatsPanel.add(lblNoFighters);
        } else {
            for (Fighter fighter : civilization.getFighters()) {
                JPanel fighterPanel = createFighterStatPanel(fighter);
                fightersStatsPanel.add(fighterPanel);
            }
        }
        
        fightersStatsPanel.revalidate();
        fightersStatsPanel.repaint();
    }
    
    private JPanel createFighterStatPanel(Fighter fighter) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        panel.setBackground(new Color(250, 250, 250));
        
        // Nombre del fighter
        JLabel lblName = new JLabel(fighter.getName());
        lblName.setFont(new Font("Arial", Font.BOLD, 13));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblName);
        
        panel.add(Box.createVerticalStrut(5));
        
        // Calcular estadísticas de este fighter
        int totalFighterCells = 0;
        int aliveFighterCells = 0;
        int destroyedFighterCells = 0;
        
        if (civilization.getMap() != null) {
            ArrayList<Cell> fighterCells = civilization.getMap().getCellsByFighter(fighter);
            totalFighterCells = fighterCells.size();
            
            for (Cell cell : fighterCells) {
                if (cell.isDestroyed()) {
                    destroyedFighterCells++;
                } else {
                    aliveFighterCells++;
                }
            }
        }
        
        // Porcentaje del territorio
        JLabel lblTerritory = new JLabel("️ " + fighter.getPercentagleOfCivilization() + "% territorio");
        lblTerritory.setFont(new Font("Arial", Font.PLAIN, 11));
        lblTerritory.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTerritory);
        
        panel.add(Box.createVerticalStrut(3));
        
        // Casillas totales
        JLabel lblCells = new JLabel(" " + totalFighterCells + " casillas");
        lblCells.setFont(new Font("Arial", Font.PLAIN, 11));
        lblCells.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblCells);
        
        panel.add(Box.createVerticalStrut(3));
        
        // Casillas vivas
        JLabel lblAlive = new JLabel(" Vivas: " + aliveFighterCells);
        lblAlive.setFont(new Font("Arial", Font.PLAIN, 10));
        lblAlive.setForeground(new Color(0, 120, 0));
        lblAlive.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblAlive);
        
        // Casillas destruidas
        JLabel lblDestroyed = new JLabel(" Destruidas: " + destroyedFighterCells);
        lblDestroyed.setFont(new Font("Arial", Font.PLAIN, 10));
        lblDestroyed.setForeground(new Color(150, 0, 0));
        lblDestroyed.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblDestroyed);
        
        panel.add(Box.createVerticalStrut(5));
        
        // Barra de vida del fighter
        double fighterLife = totalFighterCells > 0 ? (aliveFighterCells * 100.0 / totalFighterCells) : 100.0;
        JProgressBar pbFighterLife = new JProgressBar(0, 100);
        pbFighterLife.setValue((int) fighterLife);
        pbFighterLife.setStringPainted(true);
        pbFighterLife.setString(String.format("%.0f%%", fighterLife));
        pbFighterLife.setFont(new Font("Arial", Font.BOLD, 10));
        
        if (fighterLife >= 70) {
            pbFighterLife.setForeground(new Color(50, 180, 50));
        } else if (fighterLife >= 40) {
            pbFighterLife.setForeground(new Color(220, 180, 50));
        } else {
            pbFighterLife.setForeground(new Color(220, 50, 50));
        }
        
        pbFighterLife.setPreferredSize(new Dimension(150, 18));
        pbFighterLife.setMaximumSize(new Dimension(150, 18));
        pbFighterLife.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(pbFighterLife);
        
        return panel;
    }
    
    public void setCivilization(Civilization civilization) {
        this.civilization = civilization;
        updateInfo();
    }
}