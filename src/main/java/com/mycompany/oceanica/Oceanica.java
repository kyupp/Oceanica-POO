/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.oceanica;

import Game.GameClient.FrameClient;
import Game.GameServer.FrameServer;

/**
 *
 * @author kyup
 */
public class Oceanica {

    public static void main(String[] args) {
        //System.out.println("Hello World!");
        
        FrameServer server = new FrameServer();
        server.setVisible(true);
        FrameClient client = new FrameClient();
        client.setVisible(true);
        
        
        
    }
}
