package Game.GameClient.UI;

import Fighters.Fighter;
import javax.swing.*;
import java.awt.*;

/**
 * @author gabri
 */
public class FighterCardPanel extends JPanel {
    
    private Fighter fighter;
    private JLabel lblImage;
    private JLabel lblName;
    private JLabel lblGroup;
    private JLabel lblPercentage;
    private JLabel lblPower;
    private JLabel lblResistance;
    private JLabel lblSanity;
    private JProgressBar pbPower;
    private JProgressBar pbResistance;
    private JProgressBar pbSanity;
    
    public FighterCardPanel(Fighter fighter) {
        this.fighter = fighter;
        initComponents();
        updateData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 117, 168), 2),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        setBackground(new Color(240, 248, 255));
        setPreferredSize(new Dimension(250, 320));
        
        //  PANEL SUPERIOR: Imagen 
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        lblImage = new JLabel();
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setPreferredSize(new Dimension(100, 100));
        lblImage.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        lblImage.setBackground(new Color(200, 220, 240));
        lblImage.setOpaque(true);
        topPanel.add(lblImage, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        
        //  PANEL CENTRAL: Información 
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        // Nombre
        lblName = new JLabel();
        lblName.setFont(new Font("Arial", Font.BOLD, 16));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(lblName);
        
        centerPanel.add(Box.createVerticalStrut(3));
        
        // Grupo
        lblGroup = new JLabel();
        lblGroup.setFont(new Font("Arial", Font.ITALIC, 11));
        lblGroup.setForeground(new Color(70, 70, 70));
        lblGroup.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(lblGroup);
        
        centerPanel.add(Box.createVerticalStrut(5));
        
        // Porcentaje de representación
        lblPercentage = new JLabel();
        lblPercentage.setFont(new Font("Arial", Font.BOLD, 14));
        lblPercentage.setForeground(new Color(0, 100, 0));
        lblPercentage.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(lblPercentage);
        
        centerPanel.add(Box.createVerticalStrut(10));
        
        // ESTADÍSTICAS CON BARRAS
        
        // Poder
        JPanel powerPanel = createStatPanel(" Poder:", Color.RED);
        lblPower = (JLabel) powerPanel.getComponent(1);
        pbPower = new JProgressBar(0, 100);
        customizeProgressBar(pbPower, new Color(220, 50, 50));
        centerPanel.add(powerPanel);
        centerPanel.add(pbPower);
        centerPanel.add(Box.createVerticalStrut(5));
        
        // Resistencia
        JPanel resistancePanel = createStatPanel("️ Resistencia:", Color.BLUE);
        lblResistance = (JLabel) resistancePanel.getComponent(1);
        pbResistance = new JProgressBar(0, 100);
        customizeProgressBar(pbResistance, new Color(50, 100, 200));
        centerPanel.add(resistancePanel);
        centerPanel.add(pbResistance);
        centerPanel.add(Box.createVerticalStrut(5));
        
        // Sanidad
        JPanel sanityPanel = createStatPanel("️ Sanidad:", Color.GREEN);
        lblSanity = (JLabel) sanityPanel.getComponent(1);
        pbSanity = new JProgressBar(0, 100);
        customizeProgressBar(pbSanity, new Color(50, 180, 50));
        centerPanel.add(sanityPanel);
        centerPanel.add(pbSanity);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JPanel createStatPanel(String statName, Color color) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        
        JLabel lblName = new JLabel(statName);
        lblName.setFont(new Font("Arial", Font.PLAIN, 11));
        lblName.setForeground(color);
        
        JLabel lblValue = new JLabel();
        lblValue.setFont(new Font("Arial", Font.BOLD, 11));
        lblValue.setForeground(color);
        
        panel.add(lblName, BorderLayout.WEST);
        panel.add(lblValue, BorderLayout.EAST);
        
        return panel;
    }
    
    private void customizeProgressBar(JProgressBar pb, Color color) {
        pb.setStringPainted(false);
        pb.setForeground(color);
        pb.setBackground(new Color(230, 230, 230));
        pb.setBorderPainted(true);
        pb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
        pb.setPreferredSize(new Dimension(200, 15));
    }
    
    public void updateData() {
        // Actualizar nombre
        lblName.setText(fighter.getName());
        
        // Actualizar grupo (extraer de la clase)
        String groupName = fighter.getClass().getSimpleName()
                                 .replace("Group", "")
                                 .replaceAll("([A-Z])", " $1")
                                 .trim();
        lblGroup.setText(" " + groupName);
        
        // Actualizar porcentaje
        lblPercentage.setText(fighter.getPercentagleOfCivilization() + "% del territorio");
        
        // Actualizar estadísticas
        int power = (int) fighter.getPower();
        int resistance = (int) fighter.getResistance();
        int sanity = (int) fighter.getSanity();
        
        lblPower.setText(power + "%");
        lblResistance.setText(resistance + "%");
        lblSanity.setText(sanity + "%");
        
        pbPower.setValue(power);
        pbResistance.setValue(resistance);
        pbSanity.setValue(sanity);
        
        // Cargar imagen (si existe)
        loadImage();
    }
    
    private void loadImage() {
        try {
            // Intentar cargar imagen desde el path
            String imagePath = fighter.getImagePath();
            
            // Por ahora, mostrar un placeholder con las iniciales
            String initials = fighter.getName().substring(0, Math.min(2, fighter.getName().length())).toUpperCase();
            lblImage.setText("<html><div style='text-align:center; font-size:32px; font-weight:bold;'>" 
                           + initials + "</div></html>");
            lblImage.setFont(new Font("Arial", Font.BOLD, 32));
            lblImage.setHorizontalAlignment(SwingConstants.CENTER);
            
            // TODO: Si tienes imágenes reales, cargar aquí:
            // ImageIcon icon = new ImageIcon(imagePath);
            // Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            // lblImage.setIcon(new ImageIcon(scaledImage));
            
        } catch (Exception e) {
            lblImage.setText("?");
            lblImage.setFont(new Font("Arial", Font.BOLD, 48));
        }
    }
    
    public Fighter getFighter() {
        return fighter;
    }
}