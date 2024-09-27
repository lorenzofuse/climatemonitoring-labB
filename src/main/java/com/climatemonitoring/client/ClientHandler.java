package com.climatemonitoring.client;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private Connection dbConnection;

    public ClientHandler(Socket socket, Connection conn) {
        this.clientSocket = socket;
        this.dbConnection = conn;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Richiesta dal client: " + inputLine);  // Log della richiesta
                String response = processRequest(inputLine);
                out.println(response);
            }

        } catch (IOException e) {
            System.err.println("Errore nella gestione del client: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (!clientSocket.isClosed()) {
                    clientSocket.close();  // Chiude il socket del client
                    System.out.println("Socket client chiuso correttamente.");
                }
            } catch (IOException e) {
                System.err.println("Errore nella chiusura del socket: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Metodo per processare le richieste ricevute dal client
    private String processRequest(String request) {
        System.out.println("Processo la richiesta: " + request);

        if (request.startsWith("SEARCH_AREA:")) {
            return searchArea(request.substring(12));
        } else if (request.startsWith("GET_CLIMATE_PARAMS:")) {
            return getClimateParams(request.substring(19));
        } else if (request.startsWith("LOGIN:")) {
            return loginOperator(request.substring(6));
        }
        return "Comando non riconosciuto";
    }

    // Implementazione fittizia di ricerca area, da completare con logica DB
    private String searchArea(String areaName) {
        System.out.println("Eseguo ricerca per l'area: " + areaName);
        // Aggiungi qui la logica per cercare l'area nel database
        return "Risultati per l'area: " + areaName;
    }

    // Implementazione fittizia per ottenere i parametri climatici, da completare con logica DB
    private String getClimateParams(String areaId) {
        System.out.println("Recupero parametri climatici per l'area ID: " + areaId);
        // Aggiungi qui la logica per ottenere i parametri climatici dal database
        return "Parametri climatici per l'area ID: " + areaId;
    }

    // Implementazione fittizia per il login operatore, da completare con logica DB
    private String loginOperator(String credentials) {
        System.out.println("Login in corso per: " + credentials);
        // Aggiungi qui la logica per gestire il login dell'operatore
        return "Login effettuato per: " + credentials;
    }
}
