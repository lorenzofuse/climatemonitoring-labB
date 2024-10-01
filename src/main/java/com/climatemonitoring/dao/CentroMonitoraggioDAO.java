package com.climatemonitoring.dao;

import com.climatemonitoring.model.CentroMonitoraggio;
import com.climatemonitoring.util.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CentroMonitoraggioDAO {
    private DatabaseManager dbManager;

    public CentroMonitoraggioDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    // Metodo per creare un centro di monitoraggio
    public boolean creaCentroMonitoraggio(String nome, String indirizzo, String cap, String comune, String provincia) {
        String query = "INSERT INTO centrinmonitoraggio(nome, indirizzo, cap, comune, provincia) VALUES (?, ?, ?, ?, ?)";
        try {
            int rowsAffected = dbManager.executeUpdate(query, nome, indirizzo, cap, comune, provincia);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Metodo per recuperare tutti i centri di monitoraggio
    public List<CentroMonitoraggio> getAllCentriMonitoraggio() {
        String query = "SELECT * FROM centrinmonitoraggio";
        List<CentroMonitoraggio> centri = new ArrayList<>();
        try {
            ResultSet rs = dbManager.executeQuery(query);
            while (rs.next()) {
                CentroMonitoraggio centro = new CentroMonitoraggio(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("indirizzo"),
                        rs.getString("cap"),
                        rs.getString("comune"),
                        rs.getString("provincia")
                );
                centri.add(centro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return centri;
    }
}
