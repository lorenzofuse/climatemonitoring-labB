package com.climatemonitoring.shared;

import com.climatemonitoring.model.CoordinateMonitoraggio;
import com.climatemonitoring.model.OperatoriRegistrati;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public interface ClimateMonitoringService extends Remote {
    List<CoordinateMonitoraggio> cercaAreaGeograficaNome(String nome, String stato) throws RemoteException;

    List<CoordinateMonitoraggio> cercaAreaGeograficaCoordinate(Double latitudine, Double longitudine) throws RemoteException;

    String visualizzaAreaGeografica(String nome, String stato) throws RemoteException; //display tab coordinatemonitoraggio (cerca i paesi dentro il file fornito dal prof)

    String visualizzaAreaCentroMonitoraggio(int areaId) throws RemoteException; //display area dentro centromonitoraggio (cerca dalle aree che l'operatore crea)

    boolean registrazione(String nome, String cognome,String codiceFiscale, String email,String userId, String password, int centroMonitoraggioId) throws RemoteException;

    boolean verificaUser(String userId, String password) throws RemoteException;

    OperatoriRegistrati getUserById(String userId) throws RemoteException;

    boolean creaCentroMonitoraggio(String nome, String indirizzo, String cap, String comune, String provincia, int operatoreId) throws RemoteException;

    boolean inserisciParametriClimatici(int centroMonitoraggioId, int areaInteresseId, Date dataRilevazione,
                                        int vento, int umidita, int pressione, int temperatura,
                                        int precipitazioni, int altitudine, int massaGhiacciai, String note) throws RemoteException;

    boolean autenticaOperatore(String userId, String password) throws RemoteException;

    boolean registraOperatore(OperatoriRegistrati operatore) throws RemoteException;

    List<CoordinateMonitoraggio> getAreePerCentroMonitoraggio(int centroMonitoraggioId) throws RemoteException;
}
