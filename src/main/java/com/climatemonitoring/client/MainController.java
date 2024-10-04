package com.climatemonitoring.client;

import com.climatemonitoring.model.CoordinateMonitoraggio;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    @FXML
    private TableView<CoordinateMonitoraggio> resultsTable;

    @FXML
    private TableColumn<CoordinateMonitoraggio, String> nameColumn;

    @FXML
    private TableColumn<CoordinateMonitoraggio, String> stateColumn;

    @FXML
    private TableColumn<CoordinateMonitoraggio, Double> latitudeColumn;

    @FXML
    private TableColumn<CoordinateMonitoraggio, Double> longitudeColumn;


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
        String nome = nameField.getText();

        try{
            List<CoordinateMonitoraggio> res = ClientCM.getService().cercaAreaGeograficaNome(nome);

        } catch (RemoteException e) {

        }
    }

    @FXML
    private void cercaAreaGeograficaCoordinate(){
        try{
            double latitudine = Double.parseDouble(latitudeField.getText());
            double longitudine = Double.parseDouble(longitudeField.getText());

            //richiamo il service per cercare tramite coordinate
           // List<CoordinateMonitoraggio> res = ClientCM.getService().cercaAreaGeograficaCoordinate(latitudine,longitudine);

          //  displaySearchResults(res);

        } catch (NumberFormatException e) {
            System.err.println("formato inserito non corretto, per favore reinserire < "+e.getMessage()+" >");
       // }catch (RemoteException e1){
           // System.err.println("ricerca per coordinate non andata a buon fine < "+e1.getMessage()+" >");
        }

    }

    private void displaySearchResults(List<CoordinateMonitoraggio> area){
        //clear dei ris precedenti
        resultsTable.getItems().clear();
        //aggiungo i nuovi ris
        resultsTable.getItems().addAll(area);
    }

    // Metodo per gestire il logout
    @FXML
    private void handleLogout() {
        // Esegue il logout e ritorna alla schermata di login
        mainApp.showLoginView();
    }
}
