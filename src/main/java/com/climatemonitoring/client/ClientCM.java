package com.climatemonitoring.client;

import com.climatemonitoring.shared.ClimateMonitoringService;
import java.rmi.Naming;
import java.util.Scanner;

public class ClientCM {
    public static void main(String[] args) {
        try {
            // Collegarsi al servizio RMI
            ClimateMonitoringService service = (ClimateMonitoringService) Naming.lookup("rmi://localhost/ClimateMonitoringService");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Benvenuto nel sistema Climate Monitoring!");

            // Esempio: Registrazione utente
            System.out.println("Registrazione: Inserisci i dati");
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Cognome: ");
            String cognome = scanner.nextLine();
            System.out.print("Codice Fiscale: ");
            String codiceFiscale = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Userid: ");
            String userid = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            boolean registrato = service.registrazione(nome, cognome, codiceFiscale, email, userid, password);
            if (registrato) {
                System.out.println("Registrazione avvenuta con successo!");
            } else {
                System.out.println("Errore nella registrazione!");
            }

            // Altri comandi del client, come ricerca e inserimento dati
            // ...

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
