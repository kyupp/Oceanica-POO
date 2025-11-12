/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oceanica;

import Game.GameClient.FrameClient;
import Game.GameServer.FrameServer;

/**
 *
 * @author mathi
 */
public class StartOceanica {
    
    public static void StartOceanica(){
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameServer().setVisible(true);
            }
        });
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameClient().setVisible(true);
            }
        });
        
    }
    
}
