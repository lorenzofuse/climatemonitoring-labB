package com.climatemonitoring.client;

import com.climatemonitoring.shared.ClimateMonitoringService;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ClientCM extends Application{

    private static ClimateMonitoringService service;
    private Stage primaryStage;
    private BorderPane mainLayout;


    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.primaryStage.setTitle("ClimateMonitoring");
        initRMIService();
        initMainLayout();
        showLoginView();
    }

    private void initRMIService(){
        try{
            Registry reg = LocateRegistry.getRegistry("localhost",1099);
            service = (ClimateMonitoringService) reg.lookup("ClimateMonitoringService");
        } catch (Exception e) {
            System.err.println("Error initRMIService < "+e.getMessage()+" >");
        }
    }

    private void initMainLayout(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientCM.class.getResource("/fxml/MainView.fxml"));
            mainLayout = loader.load();

            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/MainView.fxml"));
            BorderPane mainLayout = loader.load();

            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Collegamento al controller della vista principale
            MainController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void showLoginView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientCM.class.getResource("/fxml/LoginView.fxml"));
            BorderPane mainView = loader.load();

            //Sset the main view into center of main layout
            mainLayout.setCenter(mainView);

            //get controller n set main app reference
            LoginController controller = loader.getController();
            controller.setMainApp(this);
        } catch (Exception e) {
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