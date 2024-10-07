package com.climatemonitoring.server;

import com.climatemonitoring.model.OperatoriRegistrati;
import com.climatemonitoring.shared.ClimateMonitoringService;
import com.climatemonitoring.util.DatabaseManager;
import com.climatemonitoring.model.CoordinateMonitoraggio;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClimateMonitoringServiceImpl extends UnicastRemoteObject implements ClimateMonitoringService {

    private DatabaseManager dbManager;

    public ClimateMonitoringServiceImpl(DatabaseManager dbManager) throws RemoteException {
        super();
        this.dbManager = dbManager;
    }

    @Override
    public List<CoordinateMonitoraggio> cercaAreaGeograficaNome(String nome, String stato) throws RemoteException {
        List<CoordinateMonitoraggio> aree = new ArrayList<>();
        String sql = "SELECT * FROM coordinatemonitoraggio WHERE nome_citta = ? AND stato = ?";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, nome);
            pstmt.setString(2, stato);

            try {
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    CoordinateMonitoraggio area = new CoordinateMonitoraggio(
                            rs.getInt("id"),
                            rs.getString("nome_citta"),
                            rs.getString("stato"),
                            rs.getString("paese"),
                            rs.getDouble("latitudine"),
                            rs.getDouble("longitudine")
                    );
                    aree.add(area);
                }
            } catch (Exception e) {
            }
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la ricerca delle aree geografiche", e);
        }
        return aree;  // Ritorna la lista delle aree trovate

    }


    @Override
    public   List<CoordinateMonitoraggio> cercaAreaGeograficaCoordinate(Double latitudine, Double longitudine) throws RemoteException {
        List<CoordinateMonitoraggio> aree = null;
        String sql = "SELECT * FROM coordinatemonitoraggio WHERE latitudine = ? AND longitudine = ?";

        try {
            Connection conn = dbManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setDouble(1, latitudine);
            pstmt.setDouble(2, longitudine);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                CoordinateMonitoraggio area = new CoordinateMonitoraggio();
                area.setId(rs.getInt("id"));
                area.setNomeCitta(rs.getString("nome_citta"));
                area.setStato(rs.getString("stato"));
                area.setPaese(rs.getString("paese"));
                area.setLatitudine(rs.getDouble("latitudine"));
                area.setLongitudine(rs.getDouble("longitudine"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aree;  // Ritorna l'area trovata (o null se non trovata)
    }


    @Override
    public String visualizzaAreaGeografica(String nome, String stato) throws RemoteException {
        String sql = "SELECT * FROM coordinatemonitoraggio WHERE nome = ? AND stato = ?";
        StringBuilder result = new StringBuilder();

        try{
            Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, nome);
            pstmt.setString(2,stato);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                result.append("ID: ").append(rs.getInt("id")).append("\n");
                result.append("Nome: ").append(rs.getString("nome_citta")).append("\n");
                result.append("Stato: ").append(rs.getString("stato")).append("\n");
                result.append("Paese: ").append(rs.getString("paese")).append("\n");
                result.append("Latitudine: ").append(rs.getDouble("latitudine")).append("\n");
                result.append("Longitudine: ").append(rs.getDouble("longitudine")).append("\n");
            } else {
                result.append("Area non trovata.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toString();
    }


    @Override
    public String visualizzaAreaCentroMonitoraggio(int areaId) throws RemoteException {
        String sql = "SELECT a.nome, a.descrizione, cm.nome AS centro_nome FROM areeinteresse a "
                + "JOIN centriMonitoraggio cm ON a.centro_monitoraggio_id = cm.id "
                + "WHERE a.id = ?";

        StringBuilder result = new StringBuilder();

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, areaId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                result.append("ID: ").append(rs.getInt("id")).append("\n");
                result.append("Nome: ").append(rs.getString("nome_citta")).append("\n");
                result.append("centro monitoraggio id: ").append(rs.getString("centro_monitoragggio_id")).append("\n");
                result.append("operatore id: ").append(rs.getString("operatore_id")).append("\n");
                result.append("stato: ").append(rs.getDouble("stato")).append("\n");
                result.append("paese: ").append(rs.getDouble("paese")).append("\n");
                result.append("Latitudine: ").append(rs.getDouble("latitudine")).append("\n");
                result.append("Longitudine: ").append(rs.getDouble("longitudine")).append("\n");
            } else {
                result.append("Area non trovata.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toString();
    }


    @Override
    public boolean registrazione(String nome, String cognome, String codiceFiscale, String email, String userId, String password, int centroMonitoraggioId) throws RemoteException {
        String sql = "INSERT INTO operatoriregistrati (nome, cognome, codice_fiscale, email, userid, password, centro_monitoraggio_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, nome);
            pstmt.setString(2, cognome);
            pstmt.setString(3, codiceFiscale);
            pstmt.setString(4, email);
            pstmt.setString(5, userId);
            pstmt.setString(6, password);
            pstmt.setInt(7, centroMonitoraggioId);

            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;  // Ritorna true se l'inserimento è andato a buon fine

        } catch (SQLException e) {
            throw new RemoteException("Errore durante la registrazione", e);
        }
    }

    @Override
    public boolean verificaUser(String userId, String password) throws RemoteException {
        String sql = "SELECT * FROM operatoriregistrati WHERE userid = ? AND password = ?";

        try{
            Connection conn = dbManager.getConnection();
            PreparedStatement ptsmt = conn.prepareStatement(sql);

            ptsmt.setString(1,userId);
            ptsmt.setString(2,password);

            try{
                ResultSet rs = ptsmt.executeQuery();
                return rs.next();
            }catch (Exception e1){
                e1.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error verifying user"+e.getMessage());
        }
        return false;
    }


    @Override
    public OperatoriRegistrati getUserById(String userId) throws RemoteException {
        String sql = "SELECT * FROM operatoriregistrati WHERE userid = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new OperatoriRegistrati(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("codice_fiscale"),
                            rs.getString("email"),
                            rs.getString("userid"),
                            rs.getString("password"),
                            rs.getInt("centro_monitoraggio_id")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RemoteException("Error retrieving user", e);
        }
    }

    @Override
    public boolean creaCentroMonitoraggio(String nome, String indirizzo, String cap, String comune, String provincia, int operatoreId) throws RemoteException {
        String sql = "INSERT INTO centriMonitoraggio (nome, indirizzo, cap, comune, provincia, operatore_id) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, nome);
            pstmt.setString(2, indirizzo);
            pstmt.setString(3, cap);
            pstmt.setString(4, comune);
            pstmt.setString(5, provincia);
            pstmt.setInt(6, operatoreId);

            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean inserisciParametriClimatici(int centroMonitoraggioId, int areaInteresseId, Date dataRilevazione,
                                               int vento, int umidita, int pressione, int temperatura,
                                               int precipitazioni, int altitudine, int massaGhiacciai, String note) throws RemoteException {
        String sql = "INSERT INTO parametriclimatici (centro_monitoraggio_id, area_interesse_id, data_rilevazione, vento, umidita, pressione, temperatura, precipitazioni, altitudine, massa_ghiacciai, note) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, centroMonitoraggioId);
            pstmt.setInt(2, areaInteresseId);
            pstmt.setDate(3, new java.sql.Date(dataRilevazione.getTime()));
            pstmt.setInt(4, vento);
            pstmt.setInt(5, umidita);
            pstmt.setInt(6, pressione);
            pstmt.setInt(7, temperatura);
            pstmt.setInt(8, precipitazioni);
            pstmt.setInt(9, altitudine);
            pstmt.setInt(10, massaGhiacciai);
            pstmt.setString(11, note);

            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean autenticaOperatore(String userId, String password) throws RemoteException {
        String sql = "SELECT * FROM operatoriregistrati WHERE userid = ? AND password = ?";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            // Se c'è un risultato, l'autenticazione ha successo
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean registraOperatore(OperatoriRegistrati operatore) throws RemoteException {
        String sql = "INSERT INTO operatoriregistrati (nome, cognome, codice_fiscale, email, userid, password, centro_monitoraggio_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, operatore.getNome());
            pstmt.setString(2, operatore.getCognome());
            pstmt.setString(3, operatore.getCodice_fiscale());
            pstmt.setString(4, operatore.getEmail());
            pstmt.setString(5, operatore.getUserid());
            pstmt.setString(6, operatore.getPassword());
            pstmt.setInt(7, operatore.getCentromonitoraggioId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la registrazione dell'operatore", e);
        }
    }


    @Override
    public List<CoordinateMonitoraggio> getAreePerCentroMonitoraggio(int centroMonitoraggioId) throws RemoteException {
        List<CoordinateMonitoraggio> aree = new ArrayList<>();
        String sql = "SELECT * FROM areeinteresse WHERE centro_monitoraggio_id = ?";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, centroMonitoraggioId);

            ResultSet rs = pstmt.executeQuery();

            // Itera sui risultati della query
            while (rs.next()) {
                CoordinateMonitoraggio area = new CoordinateMonitoraggio();
                area.setId(rs.getInt("id"));
                area.setNomeCitta( rs.getString("nome"));
                area.setLatitudine(rs.getDouble("latitudine"));
                area.setLongitudine(rs.getDouble("longitudine"));

                // Aggiungi l'area alla lista
                aree.add(area);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aree;  // Ritorna la lista delle aree trovate
    }

}
