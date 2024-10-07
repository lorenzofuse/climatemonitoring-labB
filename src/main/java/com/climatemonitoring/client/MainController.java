package com.climatemonitoring.client;


import com.climatemonitoring.model.CoordinateMonitoraggio;
import javafx.fxml.FXML;
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
       searchButton.setOnAction(event -> handleSearch());
    }

    private void handleSearch() {
        String cityName = searchField.getText().trim();
        String stateName = stateField.getText().trim();

        if (cityName.isEmpty() || stateName.isEmpty()) {
            showAlert("Errore di Ricerca", "Campi vuoti", "Inserisci sia la città che lo stato.");
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
            showAlert("Errore di Connessione", "Errore del Server",
                    "Si è verificato un errore durante la ricerca: " + e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
