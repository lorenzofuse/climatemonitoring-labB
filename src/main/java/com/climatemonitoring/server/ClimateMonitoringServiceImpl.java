package com.climatemonitoring.server;

import com.climatemonitoring.model.OperatoriRegistrati;
import com.climatemonitoring.shared.ClimateMonitoringService;
import com.climatemonitoring.util.DatabaseManager;
import com.climatemonitoring.model.CoordinateMonitoraggio;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ClimateMonitoringServiceImpl extends UnicastRemoteObject implements ClimateMonitoringService {

    private final DatabaseManager dbManager;

    public ClimateMonitoringServiceImpl(DatabaseManager dbManager) throws RemoteException {
        super();
        this.dbManager = dbManager;
    }

    @Override
    public List<CoordinateMonitoraggio> cercaAreaGeograficaNome(String nome, String stato) throws RemoteException {
        List<CoordinateMonitoraggio> aree = new ArrayList<>();

        if(nome == null || nome.trim().isEmpty() || stato == null || stato.trim().isEmpty()){
            throw new IllegalArgumentException("Nome e stato non possono essere nulli o vuoti");
        }

        String sql = "SELECT * FROM coordinatemonitoraggio WHERE nome_citta = ? AND stato = ?";

        try (Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nome + "%");
            pstmt.setString(2, stato);

            try (ResultSet rs = pstmt.executeQuery()){

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
            }
        }catch (Exception e) {
                throw new RemoteException("Errore durante la ricerca delle aree geografiche", e);
            }
        return aree;
    }

    @Override
    public List<CoordinateMonitoraggio> cercaAreaGeograficaPerPaese(String paese) throws RemoteException{
        List<CoordinateMonitoraggio> aree = new ArrayList<>();

        if(paese ==null || paese.trim().isEmpty()){
            throw new IllegalArgumentException("Il paese non può essere nullo");
        }

        String sql = "SELECT * FROM coordinatemonitoraggio WHERE paese LIKE ?";

        try{
            Connection conn = dbManager.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setString(1, "%" + paese + "%");

            try{
                ResultSet rs = p.executeQuery();
                while(rs.next()){
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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RemoteException("Errore durante la ricerca delle aree geografiche per paese", e);

        }
    return aree;
    }


    @Override
    public List<CoordinateMonitoraggio> cercaAreaGeograficaCoordinate(Double latitudine, Double longitudine) throws RemoteException {

        if(latitudine==null||longitudine==null){
            throw new IllegalArgumentException("Latitudine e longitudine non possono essere nulli");
        }

        List<CoordinateMonitoraggio> aree = new ArrayList<>();
        final double TOLLERANZA = 0.5;

        String sql = "SELECT * FROM coordinatemonitoraggio WHERE latitudine BETWEEN ? - ? AND ? + ? AND longitudine BETWEEN ? - ? AND ? + ? ";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setDouble(1, latitudine);
            pstmt.setDouble(2, TOLLERANZA);
            pstmt.setDouble(3, latitudine);
            pstmt.setDouble(4, TOLLERANZA);
            pstmt.setDouble(5, longitudine);
            pstmt.setDouble(6, TOLLERANZA);
            pstmt.setDouble(7, longitudine);
            pstmt.setDouble(8, TOLLERANZA);

            try{
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    CoordinateMonitoraggio area = new CoordinateMonitoraggio();
                    area.setId(rs.getInt("id"));
                    area.setNomeCitta(rs.getString("nome_citta"));
                    area.setStato(rs.getString("stato"));
                    area.setPaese(rs.getString("paese"));
                    area.setLatitudine(rs.getDouble("latitudine"));
                    area.setLongitudine(rs.getDouble("longitudine"));
                    aree.add(area);
                }
            }catch(Exception e1){
                e1.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Errore durante la ricerca nel database", e);
        }

        //ordino ris dopo averli presi tramite la dist + vicina
        if (!aree.isEmpty()) {
            aree.sort((a1, a2) -> {
                double dist1 = calcolaDistanzaKm(latitudine, longitudine, a1.getLatitudine(), a1.getLongitudine());
                double dist2 = calcolaDistanzaKm(latitudine, longitudine, a2.getLatitudine(), a2.getLongitudine());
                return Double.compare(dist1, dist2);
            });

        }

        return aree;
    }

    private double calcolaDistanzaKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    @Override
    public String visualizzaAreaGeografica(String nome, String stato) throws RemoteException {
        if (nome == null || nome.trim().isEmpty() || stato == null || stato.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome e stato non possono essere nulli o vuoti");
        }

        String sql = "SELECT * FROM coordinatemonitoraggio WHERE nome = ? AND stato = ?";

        StringBuilder result = new StringBuilder();

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, nome);
            pstmt.setString(2, stato);

            try {
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    result.append("Informazioni area geografica:\n");
                    result.append("ID: ").append(rs.getInt("id")).append("\n");
                    result.append("Nome città: ").append(rs.getString("nome_citta")).append("\n");
                    result.append("Stato: ").append(rs.getString("stato")).append("\n");
                    result.append("Paese: ").append(rs.getString("paese")).append("\n");
                    result.append("Latitudine: ").append(rs.getDouble("latitudine")).append("\n");
                    result.append("Longitudine: ").append(rs.getDouble("longitudine")).append("\n");

                    int areaId = rs.getInt("id");
                    boolean hasParametri = appendParametriClimatici(result, areaId);

                    if (hasParametri) {
                        appendCommentiOperatori(result, areaId);
                    } else {
                        result.append("\nNessun dato climatico disponibile per questa area.\n");
                    }
                } else {
                    result.append("Area non trovata.");
                }
            }catch (Exception e1){
                e1.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la visualizzazione dell'area geografica", e);
        }

        return result.toString();
    }

    private boolean appendParametriClimatici(StringBuilder result, int coordinateMonitoraggioId) throws SQLException {
        String sql = "SELECT COUNT(*) AS num_rilevazioni, " +
                "AVG(vento) AS avg_vento, " +
                "AVG(umidita) AS avg_umidita, " +
                "AVG(pressione) AS avg_pressione, " +
                "AVG(temperatura) AS avg_temperatura, " +
                "AVG(precipitazioni) AS avg_precipitazioni, " +
                "AVG(altitudine) AS avg_altitudine, " +
                "AVG(massa_ghiacciai) AS avg_massa_ghiacciai " +
                "FROM parametriclimatici WHERE coordinate_monitoraggio_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, coordinateMonitoraggioId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int numRilevazioni = rs.getInt("num_rilevazioni");
                    if (numRilevazioni > 0) {
                        result.append("\nDati climatici:\n");
                        result.append("Numero di rilevazioni: ").append(numRilevazioni).append("\n");
                        result.append("Medie dei parametri climatici:\n");
                        result.append("Vento: ").append(String.format("%.2f", rs.getDouble("avg_vento"))).append("\n");
                        result.append("Umidità: ").append(String.format("%.2f", rs.getDouble("avg_umidita"))).append("\n");
                        result.append("Pressione: ").append(String.format("%.2f", rs.getDouble("avg_pressione"))).append("\n");
                        result.append("Temperatura: ").append(String.format("%.2f", rs.getDouble("avg_temperatura"))).append("\n");
                        result.append("Precipitazioni: ").append(String.format("%.2f", rs.getDouble("avg_precipitazioni"))).append("\n");
                        result.append("Altitudine: ").append(String.format("%.2f", rs.getDouble("avg_altitudine"))).append("\n");
                        result.append("Massa ghiacciai: ").append(String.format("%.2f", rs.getDouble("avg_massa_ghiacciai"))).append("\n");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void appendCommentiOperatori(StringBuilder result, int coordinateMonitoraggioId) throws SQLException {
        String sql = "SELECT note FROM parametriclimatici WHERE coordinate_monitoraggio_id = ? AND note IS NOT NULL AND note != '' ORDER BY data_rilevazione DESC LIMIT 5";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, coordinateMonitoraggioId);

            try (ResultSet rs = pstmt.executeQuery()) {
                result.append("\nCommenti recenti degli operatori:\n");
                boolean hasComments = false;
                while (rs.next()) {
                    result.append("- ").append(rs.getString("note")).append("\n");
                    hasComments = true;
                }
                if (!hasComments) {
                    result.append("Nessun commento disponibile.\n");
                }
            }
        }
    }


    @Override
    public String visualizzaAreaCentroMonitoraggio(int areaId) throws RemoteException {
        String sql = "SELECT ai.*, cm.nome AS centro_nome " +
                "FROM areeinteresse ai " +
                "JOIN centrimonitoraggio cm ON ai.centro_monitoraggio_id = cm.id " +
                "WHERE ai.id = ?";

        StringBuilder result = new StringBuilder();

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, areaId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result.append("Informazioni Area di Interesse:\n");
                    result.append("ID: ").append(rs.getInt("id")).append("\n");
                    result.append("Nome: ").append(rs.getString("nome")).append("\n");
                    result.append("Centro Monitoraggio: ").append(rs.getString("centro_nome")).append("\n");
                    result.append("Centro Monitoraggio ID: ").append(rs.getInt("centro_monitoraggio_id")).append("\n");
                    result.append("Operatore ID: ").append(rs.getInt("operatore_id")).append("\n");
                    result.append("Stato: ").append(rs.getString("stato")).append("\n");
                    result.append("Latitudine: ").append(rs.getDouble("latitudine")).append("\n");
                    result.append("Longitudine: ").append(rs.getDouble("longitudine")).append("\n");
                } else {
                    result.append("Area di interesse non trovata.");
                }
            }
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la visualizzazione dell'area di interesse", e);
        }

        return result.toString();
    }


    @Override
    public boolean registrazione(String nome, String cognome, String codiceFiscale, String email, String userId, String password) throws RemoteException {
        String sql = "INSERT INTO operatoriregistrati (nome, cognome, codice_fiscale, email, userid, password, centro_monitoraggio_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            int centromonitoraggioid = getNextCentroMonitoraggio(conn);

            pstmt.setString(1, nome);
            pstmt.setString(2, cognome);
            pstmt.setString(3, codiceFiscale);
            pstmt.setString(4, email);
            pstmt.setString(5, userId);
            pstmt.setString(6, password);

            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;  //true se l'inserimento è andato a buon fine

        } catch (SQLException e) {
            throw new RemoteException("Errore durante la registrazione", e);
        }
    }

    //metodo per incrementare gli id, il primo operatore che si registra è l'1, il secondo il 2 ecc
    private int getNextCentroMonitoraggio(Connection conn) throws SQLException{
        String sql = "SELECT COALESCE(MAX(centro_monitoraggio_id), 0) + 1 AS next_id FROM operatoriregistrati";
        try {
            PreparedStatement ptsmt = conn.prepareStatement(sql);
            ResultSet rs = ptsmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("next_id");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return 1; //default se non entra
    }

    public boolean isValidCF(String cf){
        if(cf==null || !cf.matches("^[A-Z]{6}[0-9]{2}[ABCDEHLMPRST][0-9]{2}[A-Z][0-9]{3}[A-Z]$")){
            return false;
        }
        return true;
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
                            rs.getString("password")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RemoteException("Error retrieving user", e);
        }
    }

    @Override
    public boolean creaCentroMonitoraggio(int operatoreId, String nome, String indirizzo, String cap, String comune, String provincia) throws RemoteException {
        // Verifica se l'operatore ha già un centro di monitoraggio
        try {
            // Query per verificare se l'operatore ha già un centro
            String verificaQuery = "SELECT id FROM centrimonitoraggio WHERE operatore_id = ?";
            PreparedStatement verificaStmt = dbManager.getConnection().prepareStatement(verificaQuery);
            verificaStmt.setInt(1, operatoreId);
            ResultSet rs = verificaStmt.executeQuery();

            if (rs.next()) {
                // Se l'operatore ha già un centro, restituisci un errore
                System.out.println("L'operatore con ID " + operatoreId + " ha già un centro di monitoraggio.");
                return false;
            }

            // Se l'operatore non ha un centro, procedi con la creazione
            String query = "INSERT INTO centrimonitoraggio (operatore_id, nome, indirizzo, cap, comune, provincia) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = dbManager.getConnection().prepareStatement(query);
            stmt.setInt(1, operatoreId);
            stmt.setString(2, nome);
            stmt.setString(3, indirizzo);
            stmt.setString(4, cap);
            stmt.setString(5, comune);
            stmt.setString(6, provincia);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Centro di monitoraggio creato con successo per operatore ID: " + operatoreId);
                return true;
            } else {
                System.out.println("Nessun centro di monitoraggio creato");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Errore SQL durante la creazione del centro: " + e.getMessage());
            throw new RemoteException("Errore durante la creazione del centro di monitoraggio", e);
        }
    }


    @Override
    public boolean creaAreaInteresse(int operatoreId, String citta, String stato, double latitudine, double longitudine) throws RemoteException {
        int centroId = getCentroMonitoraggio(operatoreId);


        if(centroId == -1){
            throw new RemoteException("Centro di monitoraggio mancante");
        }

        try {
            String query = "INSERT INTO areeinteresse (nome, stato, centro_monitoraggio_id, latitudine, longitudine) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement stmt = dbManager.getConnection().prepareStatement(query);
            stmt.setString(1, citta);
            stmt.setString(2, stato);
            stmt.setInt(3, centroId);
            stmt.setDouble(4, latitudine);
            stmt.setDouble(5, longitudine);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RemoteException("Errore durante la creazione dell'area di interesse", e);
        }

    }

    public int getCentroMonitoraggio(int operatoreId) throws RemoteException{
        try{
            String query = "SELECT id FROM centrimonitoraggio WHERE operatore_id = ?";
            PreparedStatement stmt = dbManager.getConnection().prepareStatement(query);
            stmt.setInt(1, operatoreId);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                int centroId = rs.getInt("id");
                System.out.println("Trovato centro di monitoraggio con ID " +centroId+" per operatore ID "+operatoreId );
                return centroId;
            }else{
                System.out.println("Nessun centro di monitoraggio trovato per l'operatore con ID " +operatoreId);
                return -1;
            }
        } catch (SQLException e) {
            System.err.println("Errore SQL durante la ricerca del centro: " + e.getMessage());
            throw new RemoteException("Errore durante la ricerca del centro di monitoraggio", e);
        }
    }


    public boolean inserisciParametriClimatici(int centroMonitoraggioId, Integer areaInteresseId, Integer coordinateMonitoraggioId, Date dataRilevazione,
                                               int vento, int umidita, int pressione, int temperatura,
                                               int precipitazioni, int altitudine, int massaGhiacciai, String note) throws RemoteException {

        if(dataRilevazione == null || dataRilevazione.after(new Date())){
            throw new IllegalArgumentException("La data di rilevazione non può essere null oppure nel futuro");
        }

        if (vento < 0 || umidita < 0 || umidita > 100 || pressione < 0 || temperatura < -273 ||
                precipitazioni < 0 || altitudine < -420 || massaGhiacciai < 0) {
            throw new IllegalArgumentException("Parametri inseriti non validi");
        }

        String sql = "INSERT INTO parametriclimatici (centro_monitoraggio_id, area_interesse_id, coordinate_monitoraggio_id, data_rilevazione, vento, umidita, pressione, temperatura, precipitazioni, altitudine, massa_ghiacciai, note) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, centroMonitoraggioId);

            if (areaInteresseId != null) {
                pstmt.setInt(2, areaInteresseId);
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }

            if (coordinateMonitoraggioId != null) {
                pstmt.setInt(3, coordinateMonitoraggioId);
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }

            pstmt.setDate(4, new java.sql.Date(dataRilevazione.getTime()));
            pstmt.setInt(5, vento);
            pstmt.setInt(6, umidita);
            pstmt.setInt(7, pressione);
            pstmt.setInt(8, temperatura);
            pstmt.setInt(9, precipitazioni);
            pstmt.setInt(10, altitudine);
            pstmt.setInt(11, massaGhiacciai);
            pstmt.setString(12, note);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'inserimento dei parametri climatici: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Errore nell'inserimento dei parametri climatici", e);
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
    public List<CoordinateMonitoraggio> getAreePerCentroMonitoraggio(int centroMonitoraggioId) throws RemoteException {
        List<CoordinateMonitoraggio> aree = new ArrayList<>();
        String query = "SELECT a.* FROM areeinteresse a " +
                "JOIN centrimonitoraggio c ON a.centro_monitoraggio_id = c.id " +
                "WHERE c.operatore_id = ?";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, centroMonitoraggioId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CoordinateMonitoraggio area = new CoordinateMonitoraggio(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("stato"),
                            null, // paese non presente nella tabella areeinteresse
                            rs.getDouble("latitudine"),
                            rs.getDouble("longitudine")
                    );
                    aree.add(area);
                }
            }
        } catch (SQLException e) {
             e.printStackTrace();
             throw new RemoteException("Errore nel recupero delle aree di interesse", e);
        }

        return aree;
    }

}
