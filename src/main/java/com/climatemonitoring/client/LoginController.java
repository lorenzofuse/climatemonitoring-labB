package com.climatemonitoring.client;

import com.climatemonitoring.model.OperatoriRegistrati;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.rmi.RemoteException;

public class LoginController {

    public Button registerButton;
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
        registerButton.setOnAction(event -> showRegistrationDialog());

    }

    private void handleLogin() {
        String userId = userIdField.getText().trim();
        String password = passwordField.getText().trim();

        if (userId.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR,"Errore di Login", "Campi vuoti",
                    "Inserisci sia username che password.");
            return;
        }

        try {
            boolean authenticated = ClientCM.getService().autenticaOperatore(userId, password);
            if (authenticated) {
                mainApp.showMainView();
            } else {
                showAlert(Alert.AlertType.ERROR,"Errore di Login", "Credenziali non valide", "Username o password non corretti.");
            }
        } catch (RemoteException e) {
            showAlert(Alert.AlertType.ERROR,"Errore di Connessione", "Errore del Server",
                    "Si è verificato un errore durante il login: " + e.getMessage());
        }
    }


    private void handleGuestLogin() {
        mainApp.showMainView();
    }

    @FXML
    private void showRegistrationDialog(){
        Dialog<OperatoriRegistrati> dialog = new Dialog<>();
        dialog.setTitle("Registrazione nuovo operatore");
        dialog.setHeaderText("Inserisci i tuoi dati per registrarti");
        ButtonType registraButtonType = new ButtonType("Registra", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registraButtonType, ButtonType.CANCEL);

        VBox content = new VBox(10);
        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome");
        TextField cognomeField = new TextField();
        cognomeField.setPromptText("Cognome");
        TextField codiceFiscaleField = new TextField();
        codiceFiscaleField.setPromptText("Codice Fiscale");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField newUserIdField = new TextField();
        newUserIdField.setPromptText("User ID");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Password");
        TextField centroMonitoraggioIdField = new TextField();
        centroMonitoraggioIdField.setPromptText("ID Centro Monitoraggio");

        content.getChildren().addAll(
                new Label("Nome:"), nomeField,
                new Label("Cognome:"), cognomeField,
                new Label("Codice Fiscale:"), codiceFiscaleField,
                new Label("Email:"), emailField,
                new Label("User ID:"), newUserIdField,
                new Label("Password:"), newPasswordField,
                new Label("ID Centro Monitoraggio:"), centroMonitoraggioIdField
        );

        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registraButtonType) {
                try {
                    OperatoriRegistrati nuovoOperatore = new OperatoriRegistrati(
                            0, // l'ID verrà assegnato dal database
                            nomeField.getText(),
                            cognomeField.getText(),
                            codiceFiscaleField.getText(),
                            emailField.getText(),
                            newUserIdField.getText(),
                            newPasswordField.getText(),
                            Integer.parseInt(centroMonitoraggioIdField.getText())
                    );

                    boolean registrationSuccess = ClientCM.getService().registraOperatore(nuovoOperatore);
                    if (registrationSuccess) {
                        showAlert(Alert.AlertType.INFORMATION, "Registrazione Completata",
                                "Registrazione avvenuta con successo",
                                "Puoi ora effettuare il login con le tue credenziali.");
                        return nuovoOperatore;
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Errore Registrazione",
                                "Registrazione fallita",
                                "Si è verificato un errore durante la registrazione.");
                        return null;
                    }
                } catch (RemoteException e) {
                    showAlert(Alert.AlertType.ERROR, "Errore di Connessione",
                            "Errore del Server",
                            "Si è verificato un errore durante la registrazione: " + e.getMessage());
                    return null;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Errore Dati",
                            "ID Centro Monitoraggio non valido",
                            "Inserisci un numero valido per l'ID del Centro Monitoraggio.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }



    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
