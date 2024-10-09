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

    @FXML private TextField searchField;
    @FXML private TextField stateField;
    @FXML private Button searchButton;
    @FXML private TextArea resultArea;

    private ClientCM mainApp;

    public void setMainApp(ClientCM mainApp) {
        this.mainApp = mainApp;
    }

    // Metodo di inizializzazione chiamato automaticamente dopo il caricamento del file FXML
    @FXML
    private void initialize() {
       searchButton.setOnAction(event -> handleSearchNome());
    }

    private void loadCSS(Scene scene){
        if(scene!=null){
            String csspath = getClass().getResource("/fxml/styles.css").toExternalForm();
            scene.getStylesheets().add(csspath);
        }
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
        // Qui implementeremo la logica per mostrare l'interfaccia di ricerca per coordinate
        // Per ora, mostriamo solo un messaggio
        resultArea.setText("Funzionalità di ricerca per coordinate non ancora implementata.");
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


    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
