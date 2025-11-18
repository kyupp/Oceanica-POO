/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Game.GameClient.Client;
import Game.GameServer.ThreadServidor;

/**
 *
 * @author diego
 */
public class CommandMessage extends Command {

    public CommandMessage(String[] args) {
        // args: [0] = "MESSAGE", args[1..n-2] = texto, args[n-1] = broadcast
        super(CommandType.MESSAGE, new String[]{ extractText(args) });

        // Convertir último argumento a boolean
        boolean broadcast = Boolean.parseBoolean(args[args.length - 1]);
        this.setIsBroadcast(broadcast);
    }

    /** 
     * Reconstruye el texto tomando todo entre args[1] y args[n-2]
     */
    private static String extractText(String[] args) {
        if (args.length < 3) { 
            return ""; 
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length - 1; i++) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        threadServidor.getServer().getRefFrame()
            .writeMessage("[MSG] " + getParameters()[0] + " | Broadcast=" + isIsBroadcast());
    }
}

//public class CommandMessage extends Command {
//
//    public CommandMessage(String[] args) {
//        // Guardamos el arreglo tal cual como parameters para que el servidor pueda
//        // acceder a los indices que espera (por ejemplo para enviar privado).
//        super(CommandType.MESSAGE, args.clone()); // clonamos para evitar aliasing
//
//        // El último elemento debe ser "true" o "false"
//        if (args.length < 2) {
//            // inválido: no hay ni texto ni flag
//            this.setIsBroadcast(false);
//            return;
//        }
//        boolean broadcast = false;
//        try {
//            broadcast = Boolean.parseBoolean(args[args.length - 1]);
//        } catch (Exception e) {
//            broadcast = false;
//        }
//        this.setIsBroadcast(broadcast);
//    }
//
//    @Override
//    public void processForServer(ThreadServidor threadServidor) {
//        // Solo logueamos en el servidor; la entrega real se hace
//        // cuando el servidor llama a executeCommand(comando) en su loop.
//        String[] p = getParameters();
//        String preview = buildMessageFromParametersForServer(p);
//        threadServidor.getServer().getRefFrame().writeMessage("[MSG recv] " + preview + " | Broadcast=" + isIsBroadcast());
//    }
//
//    @Override
//    public void processInClient(Client client) {
//        // Reconstruimos y mostramos el mensaje en la UI del cliente
//        String[] p = getParameters();
//        String texto = buildMessageFromParametersForClient(p);
//        client.getRefFrame().writeMessage(texto);
//    }
//
//    /**
//     * Reconstruye un resumen legible para el servidor (quien envía, destino, text).
//     */
//    private String buildMessageFromParametersForServer(String[] args) {
//        if (args == null || args.length < 2) return "(mensaje vacío)";
//
//        boolean broadcast = isIsBroadcast();
//        if (broadcast) {
//            // texto está en args[1 .. n-2]
//            StringBuilder sb = new StringBuilder();
//            for (int i = 1; i < args.length - 1; i++) {
//                sb.append(args[i]).append(" ");
//            }
//            return "Broadcast: " + sb.toString().trim();
//        } else {
//            // asumimos forma privada: args[1] = destinatario (o remitente, según tu protocolo)
//            if (args.length < 3) return "(privado sin texto ni destinatario)";
//            String recipient = args[1];
//            StringBuilder sb = new StringBuilder();
//            for (int i = 2; i < args.length - 1; i++) {
//                sb.append(args[i]).append(" ");
//            }
//            return "Privado a '" + recipient + "': " + sb.toString().trim();
//        }
//    }
//
//    /**
//     * Reconstruye el texto que debe mostrar el cliente receptor.
//     * Si es broadcast: muestra el texto.
//     * Si es privado: muestra quien lo envió (si lo llevas en args[1]) y el texto.
//     */
//    private String buildMessageFromParametersForClient(String[] args) {
//        if (args == null || args.length < 2) return "(mensaje vacío)";
//
//        boolean broadcast = isIsBroadcast();
//        if (broadcast) {
//            StringBuilder sb = new StringBuilder();
//            for (int i = 1; i < args.length - 1; i++) {
//                sb.append(args[i]).append(" ");
//            }
//            return "[GLOBAL] " + sb.toString().trim();
//        } else {
//            if (args.length < 3) return "[PRIVADO] (sin texto)";
//            String maybeSenderOrRecipient = args[1]; // depende de cómo construyas el comando
//            StringBuilder sb = new StringBuilder();
//            for (int i = 2; i < args.length - 1; i++) {
//                sb.append(args[i]).append(" ");
//            }
//            return "[PRIVADO] " + sb.toString().trim();
//            // Si quieres mostrar remitente: return "[PRIVADO de " + maybeSenderOrRecipient + "] " + ...
//        }
//    }
//}



