package com.climatemonitoring.client;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.climatemonitoring.shared.ClimateMonitoringService;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientCM extends Application {

    private static ClimateMonitoringService service;
    private Stage primaryStage;
    private BorderPane rootLayout;


    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.primaryStage.setTitle("ClimateMonitoring");
        if (initRMIService()) {
            initRootLayout();
            showLoginView();
        } else {
            // Se la connessione RMI fallisce, mostra un errore e chiudi l'applicazione
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore di Connessione");
                alert.setHeaderText("Impossibile connettersi al server");
                alert.setContentText("Assicurati che il server sia in esecuzione e riprova.");
                alert.showAndWait();
                Platform.exit();
            });
        }
    }

    private boolean initRMIService(){
        try{
            Registry reg = LocateRegistry.getRegistry("localhost",1099);
            service = (ClimateMonitoringService) reg.lookup("ClimateMonitoringService");
            System.out.println("Connesione RMI stabilita con successo");
            return true;
        } catch (Exception e) {
            System.err.println("Error durante la connessione RMI < "+e.getMessage()+" >");
            e.printStackTrace();
            return false;
        }
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootLayout.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            BorderPane loginView = loader.load();
            rootLayout.setCenter(loginView);

            LoginController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            BorderPane mainView = loader.load();
            rootLayout.setCenter(mainView);

            MainController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ClimateMonitoringService getService() {
        return service;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }



    public static void main(String[] args) {
        launch(args);
    }
}