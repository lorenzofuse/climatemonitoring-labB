package com.climatemonitoring.client;


import com.climatemonitoring.model.CoordinateMonitoraggio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.rmi.RemoteException;
import java.util.List;

public class MainController {

    public TextField latitudeField;
    @FXML private TextField searchField;
    @FXML private TextField stateField;
    @FXML private TextField longitudeField;
    @FXML private Button searchButton;
    @FXML private TextArea resultArea;
    @FXML private TextArea coordinateResultArea;
    @FXML private Button logoutButton;

    private ClientCM mainApp;

    public void setMainApp(ClientCM mainApp) {
        this.mainApp = mainApp;
    }

    // Metodo di inizializzazione chiamato automaticamente dopo il caricamento del file FXML
    @FXML
    private void initialize() {
        searchButton.setOnAction(event -> handleSearchNome());
        logoutButton.setOnAction(event -> handleLogout());
    }


    @FXML
    private void handleSearchNome() {
        String cityName = searchField.getText().trim();
        String stateName = stateField.getText().trim();

        if (cityName.isEmpty() || stateName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR,"Errore di Ricerca", "Campi vuoti", "Inserisci sia la città che lo stato.");
            return;
        }

        try {
            List<CoordinateMonitoraggio> results =
                    ClientCM.getService().cercaAreaGeograficaNome(cityName, stateName);

            if (results.isEmpty()) {
                resultArea.setText("Nessun risultato trovato.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (CoordinateMonitoraggio area : results) {
                    sb.append("Città: ").append(area.getNomeCitta())
                            .append("\nStato: ").append(area.getStato())
                            .append("\nPaese: ").append(area.getPaese())
                            .append("\nLatitudine: ").append(area.getLatitudine())
                            .append("\nLongitudine: ").append(area.getLongitudine())
                            .append("\n\n");
                }
                resultArea.setText(sb.toString());
            }
        } catch (RemoteException e) {
            showAlert(Alert.AlertType.ERROR,"Errore di Connessione", "Errore del Server",
                    "Si è verificato un errore durante la ricerca: " + e.getMessage());
        }
    }


    @FXML
    private void handleSearchByCoordinates() {
        String latStr = latitudeField.getText().trim();
        String lonStr = longitudeField.getText().trim();

        if (latStr.isEmpty() || lonStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Errore di input", "Campi vuoti", "Inserisci sia la latitudine che la longitudine");
            return;
        }

        try {
            // Convertiamo le stringhe in double
            double latitudine = parseCoordinate(latStr);
            double longitudine = parseCoordinate(lonStr);

            // Validazione dei range
            if (!isValidLatitude(latitudine) || !isValidLongitude(longitudine)) {
                showAlert(Alert.AlertType.ERROR, "Errore di input",
                        "Coordinate non valide",
                        "La latitudine deve essere tra -90 e 90, la longitudine tra -180 e 180.");
                return;
            }

            List<CoordinateMonitoraggio> results =
                    ClientCM.getService().cercaAreaGeograficaCoordinate(latitudine, longitudine);

            displayCoordinateResults(results, latitudine, longitudine);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Errore di input",
                    "Formato non valido",
                    "Inserisci le coordinate nel formato corretto (es: 15.03201 o 45.82832).");
        } catch (RemoteException e) {
            showAlert(Alert.AlertType.ERROR, "Errore di Connessione",
                    "Errore del Server",
                    "Si è verificato un errore durante la ricerca: " + e.getMessage());
        }
    }

    private double parseCoordinate(String coord) {
        // gestisco i != formati
        coord = coord.replace(',', '.').trim();
        return Double.parseDouble(coord);
    }

    private boolean isValidLatitude(double lat) {
        return lat >= -90 && lat <= 90;
    }

    private boolean isValidLongitude(double lon) {
        return lon >= -180 && lon <= 180;
    }

    private void displayCoordinateResults(List<CoordinateMonitoraggio> results, double searchLat, double searchLon) {
        if (results.isEmpty()) {
            coordinateResultArea.setText("Nessun risultato trovato nelle vicinanze delle coordinate specificate.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Risultati vicini a Lat: %.5f, Lon: %.5f\n\n", searchLat, searchLon));

            for (CoordinateMonitoraggio area : results) {
                double distanza = calcolaDistanzaKm(searchLat, searchLon,
                        area.getLatitudine(), area.getLongitudine());

                sb.append(String.format("Città: %s\n", area.getNomeCitta()))
                        .append(String.format("Stato: %s\n", area.getStato()))
                        .append(String.format("Paese: %s\n", area.getPaese()))
                        .append(String.format("Coordinate: %.5f, %.5f\n",
                                area.getLatitudine(), area.getLongitudine()))
                        .append(String.format("Distanza: %.1f km\n\n", distanza));
            }
            coordinateResultArea.setText(sb.toString());
        }
    }

    private double calcolaDistanzaKm(double lat1, double lon1, double lat2, double lon2) {
        // Implementazione della formula di Haversine per calcolare la distanza tra due punti sulla Terra
        final int R = 6371; // Raggio della Terra in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    @FXML
    private void handleViewByClimate() {
        // Qui implementeremo la logica per visualizzare le aree per clima
        // Per ora, mostriamo solo un messaggio
        resultArea.setText("Funzionalità di visualizzazione per clima non ancora implementata.");
    }

    @FXML
    private void handleOperatorMenu() {
        // Qui implementeremo la logica per mostrare il menu dell'operatore
        // Per ora, mostriamo solo un messaggio
        resultArea.setText("Menu operatore non ancora implementato.");
    }

    private void handleLogout(){
        mainApp.showLoginView();
    }

    private void displayResult(List<CoordinateMonitoraggio> results, TextArea targetArea){
        if(results.isEmpty()){
            targetArea.setText("Nessun risultato trovato");
        }else{
            StringBuilder sb = new StringBuilder();
            for(CoordinateMonitoraggio area : results){
                sb.append("Città: ").append(area.getNomeCitta())
                        .append("\nStato: ").append(area.getStato())
                        .append("\nPaese: ").append(area.getPaese())
                        .append("\nLatitudine: ").append(area.getLatitudine())
                        .append("\nLongitudine: ").append(area.getLongitudine())
                        .append("\n\n");
            }
            targetArea.setText(sb.toString());
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
