/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TombolaServer;

import static TombolaServer.ServerGestore.servers;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ismaele Carbonari
 */
public class Server implements Runnable {

    private Socket socket;
    private DataOutputStream output;
    private BufferedReader input;
    private String ricevuta, inviata;
    private ArrayList<Server> contenitore;
    private ArrayList<Integer> estratti;
    private boolean first = false;
    private String[] nomi;

    public Server(Socket socket, int x, int y) {
        try {
            this.socket = socket;
            output = new DataOutputStream(this.socket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            contenitore = new ArrayList<>();
            estratti = new ArrayList<>();
        } catch (IOException ex) {
            System.out.println("Errore: " + ex.getMessage());
        }
    }

    public void comunica() throws InterruptedException {
        BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
        boolean fine = false;
        try {
            while (!fine) {
                if (ServerGestore.max <= 0 && !first) {
                    System.out.println("AGGIUNTI");
                    this.contenitore.addAll(ServerGestore.servers);
                    this.nomi = new String[contenitore.size()];
                    for (int j = 0; j < contenitore.size(); j++) {
                        nomi[j]=contenitore.get(j).input.readLine();
                    }
                    first = true;
                } else if (ServerGestore.max <= 0) {
                    System.out.println("Inserisci il numero estratto: ");
                    inviata = tastiera.readLine();
                    estratti.add(Integer.parseInt(inviata));
                    invia();
                    //ricezione
                    for (int j = 0; j < contenitore.size(); j++) {
                        ricevuta = contenitore.get(j).input.readLine();
                        System.out.println(j + ": " + ricevuta);
                        if (ricevuta.equals("TOMBOLA")) {
                            
                            System.out.println("Ha vinto: " + nomi[j] + " !");
                            output.writeBytes(nomi[j]+"\n");
                            System.out.println("Disconnetto tutti");
                            for (int h = 0; h < contenitore.size(); h++) {
                                contenitore.get(h).socket.close();
                            }
                            fine = true;
                            System.exit(0);
                        }

                    }

                }

            }
            System.out.println("Fine");

        } catch (IOException ex) {
            System.out.println("Errore: " + ex.getMessage());
        }
    }

    public void invia() {
        try {
            if (ServerGestore.max <= 0) {
                for (int j = 0; j < contenitore.size(); j++) {
                    contenitore.get(j).output.writeBytes(estratti.get(estratti.size() - 1) + "\n");
                }

            }

        } catch (IOException ex) {
            System.out.println("Errore: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            comunica();
        } catch (InterruptedException ex) {
            System.out.println("Errore: " + ex.getMessage());
        }
    }

}
