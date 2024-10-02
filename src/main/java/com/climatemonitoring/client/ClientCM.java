package com.climatemonitoring.client;

import com.climatemonitoring.server.ClimateMonitoringServiceImpl;
import com.climatemonitoring.shared.ClimateMonitoringService;
import com.climatemonitoring.model.CoordinateMonitoraggio;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class ClientCM {
    public static void main(String[] args) {
        try {
            ClimateMonitoringService service = (ClimateMonitoringService) Naming.lookup("rmi://localhost/ClimateMonitoringService");
            System.out.println("Connesione al servizio RMI effettuato con successo");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
