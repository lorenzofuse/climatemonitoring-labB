package com.climatemonitoring.client;

import java.io.*;
import java.net.*;

public class ClientCM {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8888;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void start() {
        try {
            connect();
            handleUserInterface();
        } catch (IOException e) {
            System.err.println("Errore del client: " + e.getMessage());
        } finally {
            close();
        }
    }

    private void connect() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Connesso al server " + SERVER_ADDRESS + ":" + SERVER_PORT);
    }

    private void handleUserInterface() throws IOException {
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String userChoice;

        while (true) {
            printMenu();
            userChoice = userInput.readLine();

            if ("0".equals(userChoice)) {
                break;
            }

            processUserChoice(userChoice);
        }
    }

    private void printMenu() {
        System.out.println("\nClimate Monitoring - Menu Principale");
        System.out.println("1. Cerca area geografica");
        System.out.println("2. Visualizza parametri climatici");
        System.out.println("3. Registra nuovo operatore");
        System.out.println("4. Crea centro di monitoraggio");
        System.out.println("5. Inserisci parametri climatici");
        System.out.println("0. Esci");
        System.out.print("Scelta: ");
    }

    private void processUserChoice(String choice) throws IOException {
        switch (choice) {
            case "1":
                cercaAreaGeografica();
                break;
            case "2":
                visualizzaParametriClimatici();
                break;
            case "3":
                registraOperatore();
                break;
            case "4":
                creaCentroMonitoraggio();
                break;
            case "5":
                inserisciParametriClimatici();
                break;
            default:
                System.out.println("Scelta non valida.");
        }
    }

    private void cercaAreaGeografica() throws IOException {
        // Implementa la logica per cercare un'area geografica
        System.out.print("Inserisci il nome dell'area: ");
        String area = new BufferedReader(new InputStreamReader(System.in)).readLine();
        String response = sendRequest("SEARCH:" + area);
        System.out.println("Risultato: " + response);
    }

    private void visualizzaParametriClimatici() throws IOException {
        // Implementa la logica per visualizzare i parametri climatici
    }

    private void registraOperatore() throws IOException {
        // Implementa la logica per registrare un nuovo operatore
    }

    private void creaCentroMonitoraggio() throws IOException {
        // Implementa la logica per creare un nuovo centro di monitoraggio
    }

    private void inserisciParametriClimatici() throws IOException {
        // Implementa la logica per inserire nuovi parametri climatici
    }

    private String sendRequest(String request) throws IOException {
        out.println(request);
        return in.readLine();
    }

    private void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Errore nella chiusura delle risorse: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ClientCM().start();
    }
}