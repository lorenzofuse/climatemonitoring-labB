package com.climatemonitoring.client;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

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

            boolean loginSuccess = ClientCM.getService().autenticaOperatore(username, password);

            if (loginSuccess) {
                //cambia la vista principale
                mainApp.showMainView();
                ((MainController) mainApp.getPrimaryStage().getScene().getUserData()).setLoggedIn(true);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password");
        }
    }

    @FXML
    private void handleCitizen(){
        mainApp.showMainView();
        ((MainController) mainApp.getPrimaryStage().getScene().getUserData()).setLoggedIn(false);
    }
    @FXML
    private void handleRegister(){
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if(username.isEmpty() || password.isEmpty()){
            showAlert(Alert.AlertType.INFORMATION, "Registrazione andata a buon fine", "Operatore Registrato "+username);
            usernameField.clear();
            passwordField.clear();
        }else{
            showAlert(Alert.AlertType.ERROR, "Registrazione fallita", "Username may already exist or there was an error during registration.");

        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
