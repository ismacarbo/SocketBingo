/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TombolaClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ismaele.carbonari
 */
public class Client implements Comparable<Integer> {

    private Socket socket;
    private DataOutputStream output;
    private BufferedReader input;
    private String indirizzo, ricevuta, inviata, nome;
    private int porta, punti, premioMinore;
    private Integer[][] tabella;
    private ArrayList<Integer> trovati;
    private int numeroTrovato = 0;

    public Client(String indirizzo, int porta, String nome) {
        this.indirizzo = indirizzo;
        this.porta = porta;
        trovati = new ArrayList<>();
        punti = 1;
        this.nome = nome;
    }

    public void connetti() {
        try {
            socket = new Socket(indirizzo, porta);
            System.out.println("Collegato col server");
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new DataOutputStream(socket.getOutputStream());
            output.writeBytes(nome + "\n");
            this.tabella = new Integer[5][5];
            for (int i = 0; i < tabella.length; i++) { //carica la tabella per ogni client di numeri casuali
                for (int j = 0; j < tabella[i].length; j++) {
                    tabella[i][j] = new Random().nextInt(90) + 1;
                }
            }
            System.out.println(stampaMatrice());
        } catch (IOException ex) {
            System.out.println("Errore: " + ex.getMessage());
        }
    }

    public void comunica() {
        boolean fine = false;
        try {
            while (!fine) {
                ricevuta = input.readLine();
                System.out.println("NUMERO: " + ricevuta);
                try {
                    if (ricerca(Integer.parseInt(ricevuta)) != null) {
                        numeroTrovato = ricerca(Integer.parseInt(ricevuta));
                        if (!trovati.contains(numeroTrovato)) {
                            trovati.add(numeroTrovato);
                            for (int i = 0; i < tabella.length; i++) {
                                for (int j = 0; j < trovati.size(); j++) {
                                    if (tabella[i][i].equals(trovati.get(j))) {
                                        premioMinore++;
                                    }
                                }
                            }
                            System.out.println("Un punto per me!");
                            switch (premioMinore) {
                                case 2:
                                    System.out.println("Ho fatto ambo!");
                                    output.writeBytes("ambo e punto per: " + nome + "!\n");
                                    break;
                                case 3:
                                    System.out.println("Ho fatto terno!");
                                    output.writeBytes("terno e punto per: " + nome + "!\n");
                                    break;
                                case 4:
                                    System.out.println("Ho fatto quaterna!");
                                    output.writeBytes("quaterna e punto per: " + nome + "!\n");
                                    break;
                                case 5:
                                    System.out.println("Ho fatto cinquina!");
                                    output.writeBytes("cinquina e punto: " + nome + "!\n");
                                    break;
                                default:
                                    System.out.println(matrixToList().size() + " PUNTI: " + punti);
                                    if (matrixToList().size() == punti) {
                                        System.out.println("HO FATTO TOMBOLA!");
                                        output.writeBytes("TOMBOLA" + "\n");
                                        socket.close();
                                        fine = true;
                                    } else {
                                        output.writeBytes("Un punto per " + nome + "\n");
                                    }
                            }
                            punti++;
                        } else {
                            punti++;
                            System.out.println("è gia mio");
                            output.writeBytes(nome + ": è gia mio\n");
                        }
                    } else {
                        System.out.println("Non ho questo numero...");
                        output.writeBytes(nome + ": Non ho questo numero...\n");
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Mi dispiace ha vinto " + ricevuta + "... sarà per la prossima volta!");
                    System.exit(0);
                }
            }
        } catch (IOException ex) {
            System.out.println("Errore: " + ex.getMessage());
        }
    }

    private List<Integer> matrixToList() {
        List<Integer> lista = new ArrayList<>();
        for (int i = 0; i < tabella.length; i++) {
            for (int j = 0; j < tabella[i].length; j++) {
                lista.addAll(Arrays.asList(tabella[i][j]));
            }
        }
        return lista;
    }

    private Integer ricerca(Integer eq) {
        return matrixToList().stream().filter(valore -> eq.equals(valore)).findAny().orElse(null);
    }

    private String stampaMatrice() {
        String out = "";
        for (int i = 0; i < tabella.length; i++) {
            for (int j = 0; j < tabella[i].length; j++) {
                out += tabella[i][j] + "\t";
            }
            out += "\n";
        }
        return out;
    }

    @Override
    public int compareTo(Integer o) {
        return (numeroTrovato < o) ? -1 : ((numeroTrovato == o) ? 0 : 1);
    }
}
