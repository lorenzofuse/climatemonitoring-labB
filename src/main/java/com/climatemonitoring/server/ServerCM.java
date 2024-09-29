package com.climatemonitoring.server;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.concurrent.*;

public class ServerCM {
    private static final int PORT = 8888;
    private static final int MAX_THREADS = 50;
    private static String dbUrl="jdbc:postgresql://localhost:5432/ClimateMonitoring";
    private static String dbUser="postgres";
    private static String dbPassword="postgre";

    public static void main(String[] args) {
        getDbCredentials();

        ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server in ascolto sulla porta " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuovo client connesso: " + clientSocket.getInetAddress().getHostAddress());

                pool.submit(new com.climatemonitoring.server.ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Errore del server: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    private static void getDbCredentials() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Inserisci l'URL del database PostgreSQL: ");
            dbUrl = reader.readLine();
            System.out.print("Inserisci il nome utente del database: ");
            dbUser = reader.readLine();
            System.out.print("Inserisci la password del database: ");
            dbPassword = reader.readLine();
        } catch (IOException e) {
            System.err.println("Errore nella lettura delle credenziali: " + e.getMessage());
            System.exit(1);
        }
    }

    public static Connection getDbConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}