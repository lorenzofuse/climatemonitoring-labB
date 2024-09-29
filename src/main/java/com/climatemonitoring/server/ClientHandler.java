package com.climatemonitoring.server;

import java.io.*;
import java.net.*;
import java.sql.*;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
                Connection conn = ServerCM.getDbConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String response = processRequest(inputLine, conn);
                out.println(response);
            }
        } catch (IOException | SQLException e) {
            System.err.println("Errore nella gestione del client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Errore nella chiusura del socket client: " + e.getMessage());
            }
        }
    }

    private String processRequest(String request, Connection conn) {
        // Implementa qui la logica per processare le richieste
        // Usa la connessione al database per eseguire query o aggiornamenti
        // Per ora, restituiamo solo un messaggio di conferma
        return "Server ha elaborato la richiesta: " + request;
    }
}