package com.climatemonitoring.server;

import com.climatemonitoring.server.ClimateMonitoringServiceImpl;
import com.climatemonitoring.shared.ClimateMonitoringService;
import com.climatemonitoring.util.DatabaseManager;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class ServerCM {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Inserisci l'host del database: ");
            String host = scanner.nextLine();

            System.out.print("Inserisci l'username del database: ");
            String username = scanner.nextLine();

            System.out.print("Inserisci la password del database: ");
            String password = scanner.nextLine();

            // Inizializzazione del DatabaseManager con le credenziali inserite
            DatabaseManager dbManager = DatabaseManager.getInstance();
            dbManager.getConnection();

            // Creare il registro RMI sulla porta 1099
            LocateRegistry.createRegistry(1099);

            // Creare l'implementazione del servizio
            ClimateMonitoringService service = new ClimateMonitoringServiceImpl();

            // Registrare il servizio nel registro RMI
            Naming.rebind("rmi://localhost/ClimateMonitoringService", service);

            System.out.println("Server avviato e servizio RMI registrato...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
