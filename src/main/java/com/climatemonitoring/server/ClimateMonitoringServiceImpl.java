package com.climatemonitoring.server;

import com.climatemonitoring.shared.ClimateMonitoringService;

import javax.swing.text.rtf.RTFEditorKit;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClimateMonitoringServiceImpl extends UnicastRemoteObject implements ClimateMonitoringService {

    private Connection conn;
    public ClimateMonitoringServiceImpl() throws RemoteException {
        super();
        try{
            //connessione al db
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ClimateMonitoring", "postgres", "postgre");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean registrazione(String nome, String cognome, String codiceFiscale, String email, String userId, String password) throws RemoteException {
        try{
            String query = "INSERT INTO operatoriregistrati(nome, cognome, codiceFiscale, String email, String userId, String password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nome);
            stmt.setString(2, cognome);
            stmt.setString(3, codiceFiscale);
            stmt.setString(4, email);
            stmt.setString(5, userId);
            stmt.setString(6, password);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean login(String userId, String password) throws RemoteException {
        try{
            String query = "SELECT * FROM operatoriregistrati WHERE userId = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,userId);
            stmt.setString(2,password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean creaCentroMonitoraggio(String nomeCentro, String indirizzo, int cap, String comune, String provincia, List<String> areeInteresse) throws RemoteException {
        try{
            String query = "INSERT INTO centrimonitoraggio(nomeCentro, indirizzo, cap, comune, provincia) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,nomeCentro);
            stmt.setString(2,indirizzo);
            stmt.setInt(3, cap);
            stmt.setString(4,comune);
            stmt.setString(5,provincia);
            stmt.executeUpdate();


            //aggiungo le arre di interesse a questo centro
            for(String area : areeInteresse){
                query = "INSERT INTO centrimonitoraggio(nomeCentro, indirizzo, cap, comune, provincia) VALUES (?, ?, ?, ?, ?)";
                stmt.setString(1,nomeCentro);
                stmt.setString(2,indirizzo);
                stmt.setInt(3, cap);
                stmt.setString(4,comune);
                stmt.setString(5,provincia);
                stmt.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean inserisciParametriClimatici(String area, String data, int vento, int umidita, int pressione, int temperatura, int precipitazione, int altitudine, int massag, String note) throws RemoteException {
        try {
            String query = "INSERT INTO parametriclimatici(data_rilevazione, vento, umidita, pressione, temperatura, precipitazioni, altitudine, massa_ghiacciai, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, data);
            stmt.setInt(2, vento);
            stmt.setInt(3, umidita);
            stmt.setInt(4, pressione);
            stmt.setInt(5, temperatura);
            stmt.setInt(6,precipitazione);
            stmt.setInt(7,altitudine);
            stmt.setInt(8,massag);
            stmt.setString(9, note);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    //controllare da qua
    @Override
    public List<String> cercaAreaGeografica(String query) throws RemoteException {
        List<String> result = new ArrayList<>();
        try {
            String sql = "SELECT nomeArea FROM AreeInteresse WHERE nomeArea LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("nomeArea"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String visualizzaParametriClimatici(String area) throws RemoteException {
        StringBuilder result = new StringBuilder();
        try {
            String sql = "SELECT * FROM ParametriClimatici WHERE area = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, area);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.append("Data: ").append(rs.getString("data"))
                        .append(", Parametro 1: ").append(rs.getInt("parametro1"))
                        .append(", Parametro 2: ").append(rs.getInt("parametro2"))
                        .append(", Note: ").append(rs.getString("note")).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
