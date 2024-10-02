package com.climatemonitoring.server;

import com.climatemonitoring.util.DatabaseManager;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerCM {
    public static void main(String [] args){
        try{
            LocateRegistry.createRegistry(1099);
            System.out.println("Registry RMI avviato sulla porta 1099");

            //creo l'ogg dbmanager per la gestione delle connessioni al db
            DatabaseManager dbManager = new DatabaseManager();

            //creo l'istanza del servizio RMI
            ClimateMonitoringServiceImpl climateService = new ClimateMonitoringServiceImpl(dbManager);

            Naming.rebind("rmi://localhost/ClimateMonitoringService", climateService);
            System.out.println("Servizio ClimateMonitoring registrato nel Registry RMI");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
