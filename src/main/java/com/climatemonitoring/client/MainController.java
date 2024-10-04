package com.climatemonitoring.client;

import com.climatemonitoring.model.CoordinateMonitoraggio;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    // Riferimento al mainApp per la gestione delle viste
    private ClientCM mainApp;

    // Riferimenti agli elementi FXML

    @FXML private TabPane mainTabPane;
    @FXML private TextField nameField, stateField, latitudeField, longitudeField;
    @FXML private TableView<CoordinateMonitoraggio> resultsTable;
    @FXML private TableColumn<CoordinateMonitoraggio, String> nameColumn, stateColumn;
    @FXML private TableColumn<CoordinateMonitoraggio, Double> latitudeColumn, longitudeColumn;
    @FXML private Button loginButton, searchButton;
    @FXML private VBox operatorMenu;
    public void setMainApp(ClientCM mainApp) {
        this.mainApp = mainApp;
    }

    // Metodo di inizializzazione chiamato automaticamente dopo il caricamento del file FXML
    @FXML
    private void initialize() {
       initializeColumn();
       operatorMenu.setVisible(true);
    }

    //inizializzo le colonne della tableview
    //Ogni volta che i dati di un oggetto CoordinateMonitoraggio
    //vengono aggiornati, anche la TableView si aggiornerà automaticament
    @FXML
    private void initializeColumn(){
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nome_cittaProperty());
        stateColumn.setCellValueFactory(cellData -> cellData.getValue().statoProperty());
        latitudeColumn.setCellValueFactory(cellData -> cellData.getValue().latitudineProperty().asObject());
        longitudeColumn.setCellValueFactory(cellData -> cellData.getValue().longitudineProperty().asObject());
    }

    // cercaAreaGeograficaPerNome
    @FXML
    private void cercaAreaGeograficaPerNome() {
        String nome = nameField.getText().trim();
        String stato = stateField.getText().trim();

        try{
            List<CoordinateMonitoraggio> resN = new ArrayList<>();
            if(!nome.isEmpty() && !stato.isEmpty()){
                resN = ClientCM.getService().cercaAreaGeograficaNome(nome,stato);
                displaySearchResults(resN);
            } else {
               showAlert(Alert.AlertType.INFORMATION, "RICERCA PER NOME ", "Inserire il nome e lo stato da ricercare" );
            }

        } catch (RemoteException e) {
            showAlert(Alert.AlertType.ERROR, "Errore di ricerca", "An error occurred while searching: " + e.getMessage());
        }
    }


    @FXML
    private void cercaAreaGeograficaCoordinate() throws RemoteException {
       try{
           if(latitudeField.getText().isEmpty() || longitudeField.getText().isEmpty()){
               showAlert(Alert.AlertType.INFORMATION, "RICERCA PER COORDINATE", "Inserire le coordinate da ricercare");
               return;
           }

           double latitudine = Double.parseDouble(latitudeField.getText());
           double longitudine = Double.parseDouble(longitudeField.getText());

           List<CoordinateMonitoraggio> resC = ClientCM.getService().cercaAreaGeograficaCoordinate(latitudine,longitudine);
           displaySearchResults(resC);
       } catch (NumberFormatException e) {
           showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numbers for latitude and longitude");
       } catch (RemoteException e) {
           showAlert(Alert.AlertType.ERROR, "Search Error", "An error occurred while searching: " + e.getMessage());
       }
    }

    private void displaySearchResults(List<CoordinateMonitoraggio> area){
        //clear dei ris precedenti
        resultsTable.getItems().clear();
        //aggiungo i nuovi ris
        resultsTable.getItems().addAll(area);
    }

    @FXML
    private void handleLogin() {
        mainApp.showLoginView();
    }

    public void setLoggedIn(boolean isLoggedIn){
        operatorMenu.setVisible(isLoggedIn);
        loginButton.setVisible(isLoggedIn);
    }

    @FXML
    private void handleLogout() {
        operatorMenu.setVisible(false);
        mainApp.showLoginView();
    }

    private void showAlert(Alert.AlertType information, String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
