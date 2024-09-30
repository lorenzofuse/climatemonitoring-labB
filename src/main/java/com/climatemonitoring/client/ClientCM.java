package com.climatemonitoring.client;

import java.io.*;
import java.net.*;

public class ClientCM {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT =8888;

    public static void main(String [] args){
        try{
            Socket socket = new Socket("localhost",8888);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader strIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Connesso al server. Digita un messaggio o 'exit' per uscire");

            String userInput;
            while ((userInput = strIn.readLine()) != null) {
                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }
                out.println(userInput);
                System.out.println("Risposta del server: " + in.readLine());
            }
        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto: " + SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("Errore I/O nella connessione al server: " + e.getMessage());
        }
    }
}
