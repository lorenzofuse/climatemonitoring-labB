package com.climatemonitoring.client;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;

public class LoginController {

    @FXML private TextField userIdField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button guestButton;

    private ClientCM mainApp;

    // Collegamento a ClientCM per cambiare le viste
    public void setMainApp(ClientCM mainApp) {
        this.mainApp = mainApp;
    }
    @FXML
    private void initialize(){
        loginButton.setOnAction(event->handleLogin());
        guestButton.setOnAction(event->handleGuestLogin());
    }

    private void handleLogin() {
        String userId = userIdField.getText().trim();
        String password = passwordField.getText().trim();

        if (userId.isEmpty() || password.isEmpty()) {
            showAlert("Errore di Login", "Campi vuoti", "Inserisci sia username che password.");
            return;
        }

        try {
            boolean authenticated = ClientCM.getService().autenticaOperatore(userId, password);
            if (authenticated) {
                mainApp.showMainView();
            } else {
                showAlert("Errore di Login", "Credenziali non valide", "Username o password non corretti.");
            }
        } catch (RemoteException e) {
            showAlert("Errore di Connessione", "Errore del Server",
                    "Si è verificato un errore durante il login: " + e.getMessage());
        }
    }


    private void handleGuestLogin() {
        mainApp.showMainView();
    }


    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
