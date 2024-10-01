package server;

import com.climatemonitoring.server.ClimateMonitoringServiceImpl;
import com.climatemonitoring.shared.ClimateMonitoringService;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServerCM {
    public static void main(String[] args) {
        try {
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
