package com.climatemonitoring.server;

import com.climatemonitoring.shared.ClimateMonitoringService;
import com.climatemonitoring.util.DatabaseManager;
import com.climatemonitoring.model.CoordinateMonitoraggio;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClimateMonitoringServiceImpl extends UnicastRemoteObject implements ClimateMonitoringService {

    private DatabaseManager dbManager;

    public ClimateMonitoringServiceImpl() throws RemoteException {
        super();
        try {
            dbManager = DatabaseManager.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean registrazione(String nome, String cognome, String codiceFiscale, String email, String userId, String password) throws RemoteException {
        try {
            String query = "INSERT INTO operatoriregistrati(nome, cognome, codiceFiscale, email, userId, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = dbManager.getConnection().prepareStatement(query);
            stmt.setString(1, nome);
            stmt.setString(2, cognome);
            stmt.setString(3, codiceFiscale);
            stmt.setString(4, email);
            stmt.setString(5, userId);
            stmt.setString(6, password);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean login(String userId, String password) throws RemoteException {
        try {
            String query = "SELECT * FROM operatoriregistrati WHERE userId = ? AND password = ?";
            PreparedStatement stmt = dbManager.getConnection().prepareStatement(query);
            stmt.setString(1, userId);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean creaCentroMonitoraggio(String nomeCentro, String indirizzo, int cap, String comune, String provincia, List<String> areeInteresse) throws RemoteException {
        return false;
    }

    @Override
    public boolean creaCentroMonitoraggio(String nomeCentro, String indirizzo, int cap, String comune, String provincia, List<CoordinateMonitoraggio> areeInteresse) throws RemoteException {
        try {
            dbManager.beginTransaction();
            String queryCentro = "INSERT INTO centrimonitoraggio(nome, indirizzo, cap, comune, provincia) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmtCentro = dbManager.getConnection().prepareStatement(queryCentro, Statement.RETURN_GENERATED_KEYS);
            stmtCentro.setString(1, nomeCentro);
            stmtCentro.setString(2, indirizzo);
            stmtCentro.setInt(3, cap);
            stmtCentro.setString(4, comune);
            stmtCentro.setString(5, provincia);
            stmtCentro.executeUpdate();
            ResultSet generatedKeys = stmtCentro.getGeneratedKeys();
            if (!generatedKeys.next()) throw new SQLException("Errore nella creazione del CentroMonitoraggio.");
            int centroId = generatedKeys.getInt(1);
            String queryArea = "INSERT INTO areeinteresse(nome, centro_monitoraggio_id, coordinate_monitoraggio_id) VALUES (?, ?, ?)";
            PreparedStatement stmtArea = dbManager.getConnection().prepareStatement(queryArea);
            String queryCoordinate = "INSERT INTO coordinatemonitoraggio(latitudine, longitudine, nome_citta) VALUES (?, ?, ?)";
            PreparedStatement stmtCoordinate = dbManager.getConnection().prepareStatement(queryCoordinate, Statement.RETURN_GENERATED_KEYS);
            for (CoordinateMonitoraggio area : areeInteresse) {
                stmtCoordinate.setDouble(1, area.getLatitudine());
                stmtCoordinate.setDouble(2, area.getLongitudine());
                stmtCoordinate.setString(3, area.getNomeCitta());
                stmtCoordinate.executeUpdate();
                ResultSet coordinateKeys = stmtCoordinate.getGeneratedKeys();
                if (!coordinateKeys.next()) throw new SQLException("Errore nella creazione delle Coordinate.");
                int coordinateId = coordinateKeys.getInt(1);
                stmtArea.setString(1, area.getNomeArea());
                stmtArea.setInt(2, centroId);
                stmtArea.setInt(3, coordinateId);
                stmtArea.executeUpdate();
            }
            dbManager.commitTransaction();
            return true;
        } catch (SQLException e) {
            dbManager.rollbackTransaction();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean inserisciParametriClimatici(String area, String data, int vento, int umidita, int pressione, int temperatura, int precipitazione, int altitudine, int massag, String note) throws RemoteException {
        try {
            dbManager.beginTransaction();
            String queryAreaId = "SELECT id FROM areeinteresse WHERE nome = ?";
            PreparedStatement stmtAreaId = dbManager.getConnection().prepareStatement(queryAreaId);
            stmtAreaId.setString(1, area);
            ResultSet rsAreaId = stmtAreaId.executeQuery();
            if (!rsAreaId.next()) throw new SQLException("Area di interesse non trovata: " + area);
            int areaId = rsAreaId.getInt("id");
            String query = "INSERT INTO parametriclimatici(area_interesse_id, data_rilevazione, vento, umidita, pressione, temperatura, precipitazioni, altitudine, massa_ghiacciai, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            dbManager.commitTransaction();
            return true;
        } catch (SQLException e) {
            dbManager.rollbackTransaction();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> cercaAreaGeografica(String query) throws RemoteException {
        List<String> result = new ArrayList<>();
        try {
            String sql = "SELECT ai.nome, cm.nome_citta FROM areeinteresse ai JOIN coordinatemonitoraggio cm ON ai.coordinate_monitoraggio_id = cm.id WHERE ai.nome LIKE ? OR cm.nome_citta LIKE ?";
            PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql);
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add("Area: " + rs.getString("nome") + ", Città: " + rs.getString("nome_citta"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String visualizzaParametriClimatici(String area) throws RemoteException {
        StringBuilder result = new StringBuilder();
        try {
            String sql = "SELECT * FROM parametriclimatici pc JOIN areeinteresse ai ON pc.area_interesse_id = ai.id WHERE ai.nome = ?";
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
