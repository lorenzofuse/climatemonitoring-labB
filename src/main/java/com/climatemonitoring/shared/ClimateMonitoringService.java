package com.climatemonitoring.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClimateMonitoringService extends Remote {

    boolean registrazione(String nome, String cognome, String codiceFiscale, String email, String userId, String password) throws RemoteException;

    boolean login (String userId, String password) throws RemoteException;

    boolean creaCentroMonitoraggio(String nomeCentro, String indirizzo,int cap,String comune, String provincia, List<String> areeInteresse) throws RemoteException;

    boolean inserisciParametriClimatici(String area, String data, int vento, int umidita, int pressione, int temperatura, int precipitazione, int altitudine, int massag,String note) throws RemoteException;

    List<String> cercaAreaGeografica(String query) throws RemoteException;

    String visualizzaParametriClimatici(String area) throws RemoteException;
}
