/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TombolaServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ismaele.carbonari
 */
public class ServerGestore {

    protected static ArrayList<Server> servers;
    private int porta = 0;
    private ServerSocket serverSocket;
    protected static int max = 3;

    public ServerGestore(int porta) {
        this.porta = porta;
        servers = new ArrayList<>();
    }

    public void attendi() {
        try {
            serverSocket = new ServerSocket(porta);
            System.out.println("Server partito in esecuzione");
            System.out.println("Aspetto il collegamento di MASSIMO 3 GIOCATORI");
            for (int i=0;i<3;i++) {
                if (max > 0) {
                    Socket socket = serverSocket.accept();
                    System.out.println("Collegato col client" + socket.getInetAddress().getHostName());
                    servers.add(new Server(socket, 5, 5));
                    Thread th = new Thread(new Server(socket, 5, 5));
                    th.start();
                    max--;
                }
            }

        } catch (IOException ex) {
            System.out.println("Errore: " + ex.getMessage());
        }
    }
}
