package com.climatemonitoring.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

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

    // Metodo per gestire l'azione del pulsante di ricerca
    @FXML
    private void handleSearch() {
        // Qui implementerai la logica per la ricerca dei dati climatici
        System.out.println("Funzione di ricerca in sviluppo...");
    }

    // Metodo per gestire il logout
    @FXML
    private void handleLogout() {
        // Esegue il logout e ritorna alla schermata di login
        mainApp.showLoginView();
    }
}
