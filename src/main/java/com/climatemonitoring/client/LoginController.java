package com.climatemonitoring.client;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private ClientCM mainApp;

    // Collegamento a ClientCM per cambiare le viste
    public void setMainApp(ClientCM mainApp) {
        this.mainApp = mainApp;
    }

    // Gestione del login
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            // Simulazione di autenticazione, potresti collegarlo al tuo servizio reale
            boolean loginSuccess = ClientCM.getService().autenticaOperatore(username, password);

            if (loginSuccess) {
                // Se il login ha successo, cambia la vista principale
                mainApp.showMainView();
            } else {
                // Mostra un messaggio di errore se username o password sono errati
                showAlert("Login Failed", "Invalid username or password");
            }
        } catch (Exception e) {
            // Mostra un errore in caso di problemi
            showAlert("Error", "Could not perform login: " + e.getMessage());
        }
    }

    // Metodo per mostrare la vista principale
    @FXML
    private void showMainView() {
        mainApp.showMainView(); // Questo chiama il metodo in ClientCM
    }

    // Metodo per gestire la registrazione
    @FXML
    private void handleRegister() {
        // Implementa la logica per la registrazione di nuovi utenti
        System.out.println("Funzione di registrazione in sviluppo...");
    }

    // Metodo per gestire l'accesso come ospite
    @FXML
    private void handleGuest() {
        // Implementa la logica per l'accesso come ospite
        System.out.println("Accesso come ospite in sviluppo...");
    }

    // Metodo per mostrare un avviso di errore
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
