/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game.GameClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author mathi
 */
public class controllerClient implements ActionListener{
    
    modelClient modelo;
    FrameClient view;

    public controllerClient(modelClient modelo, FrameClient view) {
        this.modelo = modelo;
        this.view = view;
        suscribirse();
        
    }
    
    public void suscribirse(){
        view.getBtnSend().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        String command = view.getTxfCommand().getText().trim();
        
        System.out.println("Comando recibido:   " + command);
        
        procesarComando(command);
        
        view.getTxfCommand().setText("");
    }
    
    public void procesarComando(String comando) {
        String resultado = modelo.comprobarComando(comando);
        if (!resultado.isEmpty()) {
            view.mostrarMensaje(resultado);
        }
    }
    
    
    
}
