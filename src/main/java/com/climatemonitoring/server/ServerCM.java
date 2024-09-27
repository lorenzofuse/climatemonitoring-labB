package com.climatemonitoring.server;

import com.climatemonitoring.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServerCM implements Runnable {

    private static final int PORT = 12345;  // Porta di ascolto del server
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/ClimateMonitoring";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgre";
    private boolean running = true;

    @Override
    public void run() {
        // Connessione al DB
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server avviato sulla porta " + PORT);
            System.out.println("Connessione al database riuscita");

            // Loop per accettare nuove connessioni
            while (running) {
                try {
                    Socket cliSocket = serverSocket.accept();
                    System.out.println("Nuovo client connesso: " + cliSocket.getInetAddress());
                    new Thread(new ClientHandler(cliSocket, conn)).start();
                } catch (IOException e) {
                    System.err.println("Errore durante l'accettazione di un client: " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore di connessione al database: " + e.getMessage());
            e.printStackTrace();  // Stampa lo stack trace completo per la diagnosi
        } catch (IOException e) {
            System.err.println("Errore durante l'avvio del server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopServer() {
        running = false;
        System.out.println("Server in fase di arresto");
    }

    public static void main(String[] args) {
        ServerCM server = new ServerCM();
        new Thread(server).start();
    }
}
