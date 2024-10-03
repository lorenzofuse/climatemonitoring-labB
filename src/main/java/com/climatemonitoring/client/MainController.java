package com.climatemonitoring.client;

import com.climatemonitoring.model.CoordinateMonitoraggio;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;
import java.util.List;

public class MainController {

    // Riferimento al mainApp per la gestione delle viste
    private ClientCM mainApp;

    // Riferimenti agli elementi FXML
    @FXML
    private TabPane mainTabPane;
    @FXML
    private Button searchButton;
    @FXML
    private Button logoutButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField stateField;
    @FXML
    private TextField latitudeField;
    @FXML
    private TextField longitudeField;

    // Iniezione dell'istanza di ClientCM
    public void setMainApp(ClientCM mainApp) {
        this.mainApp = mainApp;
    }

    // Metodo di inizializzazione chiamato automaticamente dopo il caricamento del file FXML
    @FXML
    private void initialize() {
        // Esempio di inizializzazione delle tab
        Tab tab1 = new Tab("Dati climatici");
        Tab tab2 = new Tab("Storico");

        mainTabPane.getTabs().addAll(tab1, tab2);
    }

    // cercaAreaGeograficaPerNome
    @FXML
    private void cercaAreaGeograficaPerNome() {
        String nome = nameField.getText();

        try{
            List<CoordinateMonitoraggio> res = ClientCM.getService().cercaAreaGeograficaNome(nome);

        } catch (RemoteException e) {

        }
    }

    // Metodo per gestire il logout
    @FXML
    private void handleLogout() {
        // Esegue il logout e ritorna alla schermata di login
        mainApp.showLoginView();
    }
}
