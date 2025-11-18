/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Game.GameClient;

import Civilization.Civilization;
import Console.Command;
import Console.CommandFactory;
import Console.CommandUtil;
import Fighters.Fighter;
import Game.GameClient.UI.FighterCardPanel;
import Game.GameClient.UI.PlayerInfoPanel;
import Game.GameMap.Cell;
import Game.GameMap.MapGrid;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author diego
 */
public class FrameClient extends javax.swing.JFrame {
    
    private final int CANTIDAD_MAX_FIGHTERS = 3;
    
    private Client client;
    private MapGrid map;
    private Cell[][] grid = new Cell[20][30];
    private modelClient model;
    private controllerClient controller;
    private int contadorFighters = 0;
    
    // Civilización local del cliente
    private Civilization myCivilization;
    private String civilizationName;
    
    // Paneles UI personalizados
    private PlayerInfoPanel playerInfoPanel;
    private JPanel fightersDisplayPanel;
    
    public FrameClient() {
        // IMPORTANTE: Primero inicializar los componentes Swing
        initComponents();
        
        System.out.println("DEBUG: FrameClient - initComponents() completado");
        
        // Pedir nombre de civilización
        civilizationName = JOptionPane.showInputDialog(this, "Ingrese nombre de su civilización");
        if (civilizationName == null || civilizationName.trim().isEmpty()) {
            civilizationName = "Civilization_" + System.currentTimeMillis();
        }
        
        System.out.println("DEBUG: Nombre de civilización: " + civilizationName);
        
        this.setTitle(civilizationName);
        
        // Crear civilización local
        this.myCivilization = new Civilization(civilizationName);
        
        System.out.println("DEBUG: Civilización creada");
        
        // Crear cliente con el nombre de civilización
        client = new Client(this, civilizationName);
        System.out.println("DEBUG: Cliente creado");
        
        // Inicializar mapa
        map = new MapGrid(grid, 20, 30);
        crearMapaClient crearMapa = new crearMapaClient(this);
        crearMapa.crearMapa();
        System.out.println("DEBUG: Mapa creado");
        
        // Asociar el mapa a la civilización
        this.myCivilization.setMap(map);
        
        // Inicializar modelo y controlador
        this.model = new modelClient(this.client, this.myCivilization);
        this.controller = new controllerClient(this.model, this);
        System.out.println("DEBUG: Modelo y controlador inicializados");
        
        // ✅ CRÍTICO: Inicializar paneles personalizados DESPUÉS de initComponents()
        try {
            initCustomPanels();
            System.out.println("DEBUG: Paneles personalizados inicializados");
        } catch (Exception e) {
            System.err.println("ERROR inicializando paneles: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Enviar comando al servidor para registrar civilización
        registrarCivilizacionEnServidor();
    }
    
    
    
    /**
     * ✅ Inicializa los paneles personalizados de UI
     */
    private void initCustomPanels() {
        System.out.println("DEBUG: Iniciando initCustomPanels()");
        
        // Verificar que los paneles existan
        if (pnlPlayerInfo == null) {
            System.err.println("ERROR: pnlPlayerInfo es NULL!");
            return;
        }
        if (pnlPlayers == null) {
            System.err.println("ERROR: pnlPlayers es NULL!");
            return;
        }
        
        System.out.println("DEBUG: Paneles base verificados");
        
        // Panel de información del jugador (pnlPlayerInfo)
        try {
            playerInfoPanel = new PlayerInfoPanel(myCivilization);
            pnlPlayerInfo.setLayout(new BorderLayout());
            pnlPlayerInfo.removeAll(); // Limpiar cualquier contenido previo
            pnlPlayerInfo.add(playerInfoPanel, BorderLayout.CENTER);
            pnlPlayerInfo.revalidate();
            pnlPlayerInfo.repaint();
            System.out.println("DEBUG: PlayerInfoPanel agregado");
        } catch (Exception e) {
            System.err.println("ERROR creando PlayerInfoPanel: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Panel de fighters (pnlPlayers)
        try {
            fightersDisplayPanel = new JPanel();
            fightersDisplayPanel.setLayout(new BoxLayout(fightersDisplayPanel, BoxLayout.Y_AXIS));
            fightersDisplayPanel.setBackground(Color.WHITE);
            fightersDisplayPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(50, 117, 168), 2),
                    "⚔️ Mis Fighters",
                    javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    new Color(50, 117, 168)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            
            System.out.println("DEBUG: fightersDisplayPanel creado");
            
            // Agregar scroll al panel de fighters
            JScrollPane scrollFighters = new JScrollPane(fightersDisplayPanel);
            scrollFighters.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollFighters.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollFighters.setBorder(null);
            
            pnlPlayers.setLayout(new BorderLayout());
            pnlPlayers.removeAll(); // Limpiar cualquier contenido previo
            pnlPlayers.add(scrollFighters, BorderLayout.CENTER);
            pnlPlayers.revalidate();
            pnlPlayers.repaint();
            
            System.out.println("DEBUG: Panel de fighters agregado a pnlPlayers");
            
            // Mostrar mensaje inicial
            updateFightersDisplay();
            System.out.println("DEBUG: updateFightersDisplay() inicial completado");
            
        } catch (Exception e) {
            System.err.println("ERROR creando panel de fighters: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * ✅ Actualiza la visualización de fighters
     */
    public void updateFightersDisplay() {
        System.out.println("DEBUG: updateFightersDisplay() llamado");
        
        if (fightersDisplayPanel == null) {
            System.err.println("ERROR: fightersDisplayPanel es NULL!");
            return;
        }
        
        // Limpiar panel
        fightersDisplayPanel.removeAll();
        System.out.println("DEBUG: Panel limpiado");
        
        int fighterCount = myCivilization.getFighters().size();
        System.out.println("DEBUG: Fighters en civilización: " + fighterCount);
        
        if (fighterCount == 0) {
            // Mostrar mensaje de placeholder
            JPanel placeholderPanel = new JPanel(new GridBagLayout());
            placeholderPanel.setOpaque(false);
            
            JLabel lblNoFighters = new JLabel("<html><div style='text-align:center;'>" +
                "🌊<br><br>" +
                "Aún no tienes fighters<br>" +
                "Usa el comando:<br>" +
                "<b>CREATE_FIGHTER</b><br>" +
                "para crear tus guerreros" +
                "</div></html>");
            lblNoFighters.setFont(new Font("Arial", Font.PLAIN, 12));
            lblNoFighters.setForeground(Color.GRAY);
            lblNoFighters.setHorizontalAlignment(SwingConstants.CENTER);
            
            placeholderPanel.add(lblNoFighters);
            fightersDisplayPanel.add(placeholderPanel);
            System.out.println("DEBUG: Placeholder agregado");
        } else {
            // Mostrar cada fighter como tarjeta
            System.out.println("DEBUG: Agregando " + fighterCount + " fighters al panel");
            
            for (int i = 0; i < fighterCount; i++) {
                Fighter fighter = myCivilization.getFighters().get(i);
                System.out.println("DEBUG: Creando card para fighter: " + fighter.getName());
                
                try {
                    FighterCardPanel card = new FighterCardPanel(fighter);
                    card.setAlignmentX(Component.CENTER_ALIGNMENT);
                    fightersDisplayPanel.add(card);
                    fightersDisplayPanel.add(Box.createVerticalStrut(10));
                    System.out.println("DEBUG: Card agregada para " + fighter.getName());
                } catch (Exception e) {
                    System.err.println("ERROR creando card para " + fighter.getName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // Agregar espacio flexible al final
            fightersDisplayPanel.add(Box.createVerticalGlue());
        }
        
        // CRÍTICO: Forzar actualización visual
        fightersDisplayPanel.revalidate();
        fightersDisplayPanel.repaint();
        
        // También actualizar el contenedor padre
        if (pnlPlayers != null) {
            pnlPlayers.revalidate();
            pnlPlayers.repaint();
        }
        
        System.out.println("DEBUG: updateFightersDisplay() completado");
    }
    
    /**
     * ✅ Actualiza la información del jugador
     */
    public void updatePlayerInfo() {
        System.out.println("DEBUG: updatePlayerInfo() llamado");
        
        if (playerInfoPanel != null) {
            try {
                playerInfoPanel.updateInfo();
                System.out.println("DEBUG: PlayerInfo actualizado");
            } catch (Exception e) {
                System.err.println("ERROR actualizando PlayerInfo: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("ERROR: playerInfoPanel es NULL!");
        }
    }
    
    /**
     * ✅ Método llamado cuando se crea un fighter exitosamente
     */
    public void onFighterCreated(Fighter fighter) {
        System.out.println("DEBUG: onFighterCreated() llamado para " + fighter.getName());
        
        writeMessage("✅ Fighter creado: " + fighter.getName());
        
        // Actualizar displays
        try {
            System.out.println("DEBUG: Actualizando displays...");
            updateFightersDisplay();
            updatePlayerInfo();
            System.out.println("DEBUG: Displays actualizados");
        } catch (Exception e) {
            System.err.println("ERROR en onFighterCreated: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Mostrar resumen
        int totalPercent = 0;
        for (Fighter f : myCivilization.getFighters()) {
            totalPercent += f.getPercentagleOfCivilization();
        }
        
        writeMessage(String.format("   Fighters: %d/3 | Territorio asignado: %d%%/100%%",
            myCivilization.getFighters().size(), totalPercent));
        
        if (myCivilization.getFighters().size() == 3 && totalPercent == 100) {
            writeMessage("🎉 ¡Todos los fighters creados! Puedes usar START_GAME cuando estés listo");
        }
    }
    
    /**
     * ✅ Método llamado cuando inicia el juego
     */
    public void onGameStarted() {
        System.out.println("DEBUG: onGameStarted() llamado");
        writeMessage("⚔️ ¡EL JUEGO HA COMENZADO!");
        
        // Inicializar mapa si no está inicializado
        if (myCivilization.getMap() == null) {
            myCivilization.initializeMap(20, 30);
        }
        
        updatePlayerInfo();
        updateFightersDisplay();
    }
    
    /**
     * ✅ Método llamado cuando se recibe daño
     */
    public void onDamageReceived() {
        System.out.println("DEBUG: onDamageReceived() llamado");
        updatePlayerInfo();
    }
    
    /**
     * Registra la civilización en el servidor
     */
    private void registrarCivilizacionEnServidor() {
        try {
            writeMessage("Registrando civilización: " + civilizationName);
        } catch (Exception e) {
            writeMessage("Error al registrar civilización: " + e.getMessage());
        }
    }
    
    // Getters
    
    public Civilization getMyCivilization() {
        return myCivilization;
    }
    
    public String getCivilizationName() {
        return civilizationName;
    }
    
    public boolean comprobarCantidadFighers() {
        return myCivilization.getFighters().size() < CANTIDAD_MAX_FIGHTERS;
    }
    
    public void actualizarContadorFighters() {
        this.contadorFighters = myCivilization.getFighters().size();
    }
    
    public void mostrarEstadoCivilizacion() {
        StringBuilder estado = new StringBuilder();
        estado.append("\n╔════════════════════════════════════╗\n");
        estado.append("║  ESTADO DE ").append(civilizationName).append("\n");
        estado.append("╚════════════════════════════════════╝\n\n");
        
        estado.append("Fighters: ").append(myCivilization.getFighters().size()).append("/3\n");
        
        int totalPorcentaje = 0;
        for (var fighter : myCivilization.getFighters()) {
            estado.append("- ").append(fighter.getName())
                  .append(": ").append(fighter.getPercentagleOfCivilization()).append("%\n");
            totalPorcentaje += fighter.getPercentagleOfCivilization();
        }
        
        estado.append("Total: ").append(totalPorcentaje).append("%\n");
        
        if (map != null) {
            estado.append("Vida del mapa: ")
                  .append(String.format("%.1f%%", map.getAlivePercentage())).append("\n");
        }
        
        writeMessage(estado.toString());
    }
    
    public void writeMessage(String msg) {
        txaMessages.append(msg + "\n");
    }

    public MapGrid getMap() {
        return map;
    }
    
    public void mostrarMensaje(String mensaje) {
        this.writeMessage(mensaje); 
    }

    public JTextField getTxfCommand() {
        return txfCommand;
    }

    public int getContadorFighters() {
        return contadorFighters;
    }

    public JTextArea getTxaMessages() {
        return txaMessages;
    }
    
    public void aumentarContadorFighters(){
        if (comprobarCantidadFighers()){
            this.contadorFighters ++;       
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txaMessages = new javax.swing.JTextArea();
        txfCommand = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaLog = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txaAttackResult = new javax.swing.JTextArea();
        pnlMap = new javax.swing.JPanel();
        pnlPlayerInfo = new javax.swing.JPanel();
        pnlPlayers = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txaMessages.setColumns(20);
        txaMessages.setRows(5);
        txaMessages.setEnabled(false);
        jScrollPane1.setViewportView(txaMessages);

        btnSend.setText("SEND");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        txaLog.setColumns(20);
        txaLog.setRows(5);
        txaLog.setEnabled(false);
        jScrollPane2.setViewportView(txaLog);

        txaAttackResult.setColumns(20);
        txaAttackResult.setRows(5);
        txaAttackResult.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txaAttackResult.setEnabled(false);
        jScrollPane3.setViewportView(txaAttackResult);

        pnlMap.setBackground(new java.awt.Color(255, 255, 255));
        pnlMap.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pnlMapLayout = new javax.swing.GroupLayout(pnlMap);
        pnlMap.setLayout(pnlMapLayout);
        pnlMapLayout.setHorizontalGroup(
            pnlMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
        );
        pnlMapLayout.setVerticalGroup(
            pnlMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 441, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlPlayerInfoLayout = new javax.swing.GroupLayout(pnlPlayerInfo);
        pnlPlayerInfo.setLayout(pnlPlayerInfoLayout);
        pnlPlayerInfoLayout.setHorizontalGroup(
            pnlPlayerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 697, Short.MAX_VALUE)
        );
        pnlPlayerInfoLayout.setVerticalGroup(
            pnlPlayerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlPlayersLayout = new javax.swing.GroupLayout(pnlPlayers);
        pnlPlayers.setLayout(pnlPlayersLayout);
        pnlPlayersLayout.setHorizontalGroup(
            pnlPlayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 306, Short.MAX_VALUE)
        );
        pnlPlayersLayout.setVerticalGroup(
            pnlPlayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlPlayerInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlPlayers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txfCommand)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSend)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(pnlMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlPlayerInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlPlayers, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfCommand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSend))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed

    }//GEN-LAST:event_btnSendActionPerformed

    public JPanel getPnlMap() {
        return pnlMap;
    }

    public JButton getBtnSend() {
        return btnSend;
    }

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameClient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel pnlMap;
    private javax.swing.JPanel pnlPlayerInfo;
    private javax.swing.JPanel pnlPlayers;
    private javax.swing.JTextArea txaAttackResult;
    private javax.swing.JTextArea txaLog;
    private javax.swing.JTextArea txaMessages;
    private javax.swing.JTextField txfCommand;
    // End of variables declaration//GEN-END:variables
}
