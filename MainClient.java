/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TombolaClient;

import java.util.Scanner;

/**
 *
 * @author INTEL
 */
public class MainClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scan=new Scanner(System.in);
        System.out.println("Inserisci il tuo nome: ");
        String nome=scan.nextLine();
        Client c=new Client("localhost", 6789,nome);
        c.connetti();
        c.comunica();
    }
    
}
