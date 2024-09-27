package com.climatemonitoring.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientCM {

    private static final String SERVER_HOST = "localhost";  // Indirizzo del server
    private static final int SERVER_PORT = 12345;  // Porta del server

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void connect() throws IOException {
        socket = new Socket(SERVER_HOST, SERVER_PORT);
        out = new PrintWriter(socket.getOutputStream(), true);  // Attivato l'auto-flush
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Connesso al server");
    }

    public String sendRequest(String request) throws IOException {
        out.println(request);  // Invia la richiesta al server
        return in.readLine();  // Legge la risposta del server
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();  // Chiude la connessione
        }
    }

    public static void main(String[] args) {
        ClientCM client = new ClientCM();
        try {
            client.connect();

            // Menu principale
            boolean running = true;
            Scanner scanner = new Scanner(System.in);

            while (running) {
                System.out.println("\nMenu Principale:");
                System.out.println("1. Ricerca Area");
                System.out.println("2. Visualizza Parametri Climatici");
                System.out.println("3. Login Operatore");
                System.out.println("4. Esci");
                System.out.print("Scelta: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consuma il newline

                switch (choice) {
                    case 1:
                        ricercaArea(client, scanner);
                        break;
                    case 2:
                        visualizzaParametriClimatici(client, scanner);
                        break;
                    case 3:
                        loginOperatore(client, scanner);
                        break;
                    case 4:
                        running = false;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            }

            client.disconnect();
        } catch (IOException e) {
            System.err.println("Errore di comunicazione: " + e.getMessage());
        }
    }

    private static void ricercaArea(ClientCM client, Scanner scanner) throws IOException {
        System.out.print("Inserisci il nome dell'area da cercare: ");
        String areaName = scanner.nextLine();
        String response = client.sendRequest("SEARCH_AREA:" + areaName);
        System.out.println("Risultato ricerca: " + response);
    }

    private static void visualizzaParametriClimatici(ClientCM client, Scanner scanner) throws IOException {
        System.out.print("Inserisci l'ID dell'area: ");
        String areaId = scanner.nextLine();
        String response = client.sendRequest("GET_CLIMATE_PARAMS:" + areaId);
        System.out.println("Parametri climatici: " + response);
    }

    private static void loginOperatore(ClientCM client, Scanner scanner) throws IOException {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String response = client.sendRequest("LOGIN:" + username + ":" + password);
        System.out.println("Risultato login: " + response);
    }
}
