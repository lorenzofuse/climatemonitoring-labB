package com.climatemonitoring.client;

import com.climatemonitoring.model.OperatoriRegistrati;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class LoginController {

    @FXML public Button registerButton;
    @FXML private TextField userIdField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button guestButton;

    private ClientCM mainApp;

    public void setMainApp(ClientCM mainApp) {
        this.mainApp = mainApp;
    }
    @FXML
    private void initialize(){
        loginButton.setOnAction(event->handleLogin());
        guestButton.setOnAction(event->handleGuestLogin());
        registerButton.setOnAction(event -> showRegistrationDialog());
    }

    @FXML
    private void handleLogin() {
        String userId = userIdField.getText().trim();
        String password = passwordField.getText().trim();

        if (userId.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR,"Errore di Login", "Campi vuoti", "Inserisci sia username che password.");
            return;
        }

        try {
            boolean authenticated = ClientCM.getService().autenticaOperatore(userId, password);
            if (authenticated) {
                OperatoriRegistrati user = ClientCM.getService().getUserById(userId);
                if (user != null) {
                    MainController mainController = mainApp.showMainView();
                    mainController.setCurrentUser(user);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Errore di Login",
                            "Utente non trovato", "Impossibile recuperare i dati dell'utente.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Errore di Login",
                        "Credenziali non valide", "Username o password non corretti.");
            }
        } catch (RemoteException e) {
            showAlert(Alert.AlertType.ERROR, "Errore di Connessione",
                    "Errore del Server", "Si è verificato un errore durante il login: " + e.getMessage());
        }
    }

    @FXML
    private void handleGuestLogin() {
        mainApp.showMainView();
    }

    @FXML
    private void showRegistrationDialog() {
        Dialog<OperatoriRegistrati> dialog = new Dialog<>();
        dialog.setTitle("Registrazione nuovo operatore");
        dialog.setHeaderText("Inserisci i tuoi dati per registrarti");

        ButtonType registraButtonType = new ButtonType("Registra", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registraButtonType, ButtonType.CANCEL);

        VBox content = new VBox(10); //box da 10
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

        content.getChildren().addAll(
                new Label("Nome:"), nomeField,
                new Label("Cognome:"), cognomeField,
                new Label("Codice Fiscale:"), codiceFiscaleField,
                new Label("Email:"), emailField,
                new Label("User ID:"), newUserIdField,
                new Label("Password:"), newPasswordField
        );

        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registraButtonType) {
                try {
                    String nome = nomeField.getText();
                    String cognome = cognomeField.getText();
                    String codiceFiscale = codiceFiscaleField.getText();
                    String email = emailField.getText();
                    String userId = newUserIdField.getText();
                    String password = newPasswordField.getText();


                    if (nome.isEmpty() || cognome.isEmpty() || codiceFiscale.isEmpty() || email.isEmpty() || userId.isEmpty() || password.isEmpty()) {
                        showAlert(Alert.AlertType.ERROR, "Errore Registrazione", "Campi vuoti", "Compila tutti i campi per registrarti.");
                        return null;
                    }


                    boolean registrationSuccess = ClientCM.getService().registrazione(nome, cognome, codiceFiscale, email, userId, password);
                    if (registrationSuccess) {
                        showAlert(Alert.AlertType.INFORMATION, "Registrazione Completata",
                                "Registrazione avvenuta con successo", "Puoi ora effettuare il login con le tue credenziali.");
                        return new OperatoriRegistrati(0, nome, cognome, codiceFiscale, email, userId, password);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Errore Registrazione", "Registrazione fallita", "Si è verificato un errore durante la registrazione.");
                        return null;
                    }
                } catch (RemoteException e) {
                    showAlert(Alert.AlertType.ERROR, "Errore di Connessione", "Errore del Server", "Si è verificato un errore durante la registrazione: " + e.getMessage());
                    return null;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
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
