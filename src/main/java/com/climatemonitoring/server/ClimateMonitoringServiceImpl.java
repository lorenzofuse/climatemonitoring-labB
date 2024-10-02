package com.climatemonitoring.server;

import com.climatemonitoring.shared.ClimateMonitoringService;
import com.climatemonitoring.util.DatabaseManager;

import javax.swing.text.rtf.RTFEditorKit;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClimateMonitoringServiceImpl extends UnicastRemoteObject implements ClimateMonitoringService {

    private Connection conn;
    private DatabaseManager dbManager;
    public ClimateMonitoringServiceImpl() throws RemoteException {
        super();
        try{
            //connessione al db
             dbManager = DatabaseManager.getInstance();

             //la connessione viene gestita direttamente dentro la classe DataBaseManager
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean registrazione(String nome, String cognome, String codiceFiscale, String email, String userId, String password) throws RemoteException {
        try{
            String query = "INSERT INTO operatoriregistrati(nome, cognome, codiceFiscale, email, userId, password) VALUES (?, ?, ?, ?, ?, ?)";
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
        try {
            // Avvio della transazione
            dbManager.beginTransaction();

            // Inserimento nel CentroMonitoraggio
            String query = "INSERT INTO centrimonitoraggio(nome, indirizzo, cap, comune, provincia) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = dbManager.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nomeCentro);
            stmt.setString(2, indirizzo);
            stmt.setInt(3, cap);
            stmt.setString(4, comune);
            stmt.setString(5, provincia);
            stmt.executeUpdate();

            // Ottenere l'ID del centro di monitoraggio appena creato
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Errore nella creazione del CentroMonitoraggio, nessun ID ottenuto.");
            }
            int centroId = generatedKeys.getInt(1);

            // Inserimento nelle AreeInteresse
            query = "INSERT INTO areeinteresse(nome, centro_monitoraggio_id, coordinate_monitoraggio_id) VALUES (?, ?, ?)";
            stmt = dbManager.getConnection().prepareStatement(query);

            for (String area : areeInteresse) {
                // Supponiamo che l'area contenga il nome dell'area e le sue coordinate siano già state create
                // Devi mappare il nome dell'area con le coordinate esistenti in "coordinatemonitoraggio"
          //      int coordinateId = getCoordinateIdByNome(area);  // Metodo per ottenere l'ID delle coordinate
                stmt.setString(1, area);
                stmt.setInt(2, centroId);
                stmt.setInt(3, coordinateId);
                stmt.executeUpdate();
            }

            dbManager.commitTransaction();
            return true;
        } catch (Exception e) {
            dbManager.rollbackTransaction();
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public boolean inserisciParametriClimatici(String area, String data, int vento, int umidita, int pressione, int temperatura, int precipitazione, int altitudine, int massag, String note) throws RemoteException {
        try {
            dbManager.beginTransaction();  // Avvio della transazione

            // Trova l'ID dell'area in base al nome
        //    int areaId = getAreaIdByNome(area);  // Metodo per ottenere l'ID dell'area

            // Inserisci i parametri climatici
            String query = "INSERT INTO parametriclimatici(area_interesse_id, data_rilevazione, vento, umidita, pressione, temperatura, precipitazioni, altitudine, massa_ghiacciai, note) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = dbManager.getConnection().prepareStatement(query);
            stmt.setInt(1, areaId);
            stmt.setString(2, data);
            stmt.setInt(3, vento);
            stmt.setInt(4, umidita);
            stmt.setInt(5, pressione);
            stmt.setInt(6, temperatura);
            stmt.setInt(7, precipitazione);
            stmt.setInt(8, altitudine);
            stmt.setInt(9, massag);
            stmt.setString(10, note);
            stmt.executeUpdate();

            dbManager.commitTransaction();  // Commit della transazione
            return true;
        } catch (SQLException e) {
            dbManager.rollbackTransaction();  // Rollback in caso di errore
            e.printStackTrace();
            return false;
        }
    }




    //controllare da qua
    @Override
    public List<String> cercaAreaGeografica(String query) throws RemoteException {
        List<String> result = new ArrayList<>();
        try {
            // Ricerca in base al nome dell'area o al nome della città dalle coordinate
            String sql = "SELECT ai.nome, cm.nome_citta FROM areeinteresse ai " +
                    "JOIN coordinatemonitoraggio cm ON ai.coordinate_monitoraggio_id = cm.id " +
                    "WHERE ai.nome LIKE ? OR cm.nome_citta LIKE ?";
            PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql);
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.add("Area: " + rs.getString("nome") + ", Città: " + rs.getString("nome_citta"));
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
            String sql = "SELECT * FROM parametriclimatici pc " +
                    "JOIN areeinteresse ai ON pc.area_interesse_id = ai.id " +
                    "WHERE ai.nome = ?";
            PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql);
            stmt.setString(1, area);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append("Data: ").append(rs.getString("data_rilevazione"))
                        .append(", Vento: ").append(rs.getInt("vento"))
                        .append(", Umidità: ").append(rs.getInt("umidita"))
                        .append(", Pressione: ").append(rs.getInt("pressione"))
                        .append(", Temperatura: ").append(rs.getInt("temperatura"))
                        .append(", Precipitazioni: ").append(rs.getInt("precipitazioni"))
                        .append(", Altitudine: ").append(rs.getInt("altitudine"))
                        .append(", Massa Ghiacciai: ").append(rs.getInt("massa_ghiacciai"))
                        .append(", Note: ").append(rs.getString("note")).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

}
