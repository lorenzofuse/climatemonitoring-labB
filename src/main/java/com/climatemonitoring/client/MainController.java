package com.climatemonitoring.client;


import com.climatemonitoring.model.CoordinateMonitoraggio;
import com.climatemonitoring.model.OperatoriRegistrati;
import javafx.application.Preloader;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;


import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public class MainController {


    @FXML private TextField searchField;
    @FXML private TextField stateField;
    @FXML public TextField latitudeField;
    @FXML private TextField longitudeField;
    @FXML private Button searchButton;
    @FXML private TextArea resultArea;
    @FXML private TextArea coordinateResultArea;
    @FXML private Button logoutButton;
    @FXML private Tab operatorTab;
    @FXML private TextField paeseField;
    @FXML private TextArea paeseResultArea;
    @FXML public TextArea operatorResultArea;
    @FXML private TabPane mainTabPane;
    @FXML private ComboBox<CoordinateMonitoraggio> areaComboBox;
    @FXML private TextField areaNameField;
    @FXML private TextField areaStateField;
    @FXML private Button viewClimateDataButton;
    @FXML private TextArea climateDataResultArea;

    private OperatoriRegistrati currentUser;
    private ClientCM mainApp;

    public void setMainApp(ClientCM mainApp) {
        this.mainApp = mainApp;
    }

    //chiamato automaticamente dopo il caricamento del file FXML
    @FXML
    private void initialize() {
        searchButton.setOnAction(event -> handleSearchNome());
        logoutButton.setOnAction(event -> handleLogout());

        if (operatorTab != null) {
            operatorTab.setDisable(true);
        }

        updateAreaComboBox();
    }

    public void setCurrentUser(OperatoriRegistrati user) {
        this.currentUser = user;
        if (user != null && operatorTab != null) {
            operatorTab.setDisable(false);
            updateAreaComboBox();
        } else {
            operatorTab.setDisable(true);
        }
    }

    @FXML
    private void handleSearchNome() {
        String cityName = searchField.getText().trim();
        String stateName = stateField.getText().trim();

        if (cityName.isEmpty() || stateName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Errore di Ricerca", "Campi vuoti", "Inserisci sia la città che lo stato.");
            return;
        }

        try {
            List<CoordinateMonitoraggio> results = ClientCM.getService().cercaAreaGeograficaNome(cityName, stateName);

            if (results.isEmpty()) {
                resultArea.setText("Nessun risultato trovato.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (CoordinateMonitoraggio area : results) {
                    sb.append("Città: ").append(area.getNomeCitta())
                            .append("\nStato: ").append(area.getStato())
                            .append("\nPaese: ").append(area.getPaese())
                            .append("\nLatitudine: ").append(area.getLatitudine())
                            .append("\nLongitudine: ").append(area.getLongitudine())
                            .append("\n\n");
                }
                resultArea.setText(sb.toString());
            }
        } catch (RemoteException e) {
            showAlert(Alert.AlertType.ERROR, "Errore di Connessione", "Errore del Server",
                    "Si è verificato un errore durante la ricerca: " + e.getMessage());
        }
    }


    @FXML
    private void handleSearchByCoordinates() {
        String latStr = latitudeField.getText().trim();
        String lonStr = longitudeField.getText().trim();

        if (latStr.isEmpty() || lonStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Errore di input", "Campi vuoti", "Inserisci sia la latitudine che la longitudine");
            return;
        }

        try {
            // Convertiamo le stringhe in double
            double latitudine = parseCoordinate(latStr);
            double longitudine = parseCoordinate(lonStr);

            // Validazione dei range
            if (!isValidLatitude(latitudine) || !isValidLongitude(longitudine)) {
                showAlert(Alert.AlertType.ERROR, "Errore di input",
                        "Coordinate non valide",
                        "La latitudine deve essere tra -90 e 90, la longitudine tra -180 e 180.");
                return;
            }

            List<CoordinateMonitoraggio> results =
                    ClientCM.getService().cercaAreaGeograficaCoordinate(latitudine, longitudine);

            displayCoordinateResults(results, latitudine, longitudine);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Errore di input",
                    "Formato non valido",
                    "Inserisci le coordinate nel formato corretto (es: 15.03201 o 45.82832).");
        } catch (RemoteException e) {
            showAlert(Alert.AlertType.ERROR, "Errore di Connessione",
                    "Errore del Server",
                    "Si è verificato un errore durante la ricerca: " + e.getMessage());
        }
    }

    private double parseCoordinate(String coord) {
        // gestisco i != formati
        coord = coord.replace(',', '.').trim();
        return Double.parseDouble(coord);
    }

    private boolean isValidLatitude(double lat) {
        return lat >= -90 && lat <= 90;
    }

    private boolean isValidLongitude(double lon) {
        return lon >= -180 && lon <= 180;
    }

    private void displayCoordinateResults(List<CoordinateMonitoraggio> results, double searchLat, double searchLon) {
        if (results.isEmpty()) {
            coordinateResultArea.setText("Nessun risultato trovato nelle vicinanze delle coordinate specificate.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Risultati vicini a Lat: %.5f, Lon: %.5f\n\n", searchLat, searchLon));

            for (CoordinateMonitoraggio area : results) {
                double distanza = calcolaDistanzaKm(searchLat, searchLon,
                        area.getLatitudine(), area.getLongitudine());

                sb.append(String.format("Città: %s\n", area.getNomeCitta()))
                        .append(String.format("Stato: %s\n", area.getStato()))
                        .append(String.format("Paese: %s\n", area.getPaese()))
                        .append(String.format("Coordinate: %.5f, %.5f\n",
                                area.getLatitudine(), area.getLongitudine()))
                        .append(String.format("Distanza: %.1f km\n\n", distanza));
            }
            coordinateResultArea.setText(sb.toString());
        }
    }

    private double calcolaDistanzaKm(double lat1, double lon1, double lat2, double lon2) {
        // Implementazione della formula di Haversine per calcolare la distanza tra due punti sulla Terra
        final int R = 6371; // Raggio della Terra in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    @FXML
    private void handleSearchByCountry() {
        String paese = paeseField.getText().trim();

        if(paese.isEmpty()){
            showAlert(Alert.AlertType.ERROR,"Errore di ricerca","Campo vuoto","Inserisci il nome del paese");
            return;
        }

        try{
            List<CoordinateMonitoraggio> ris = ClientCM.getService().cercaAreaGeograficaPerPaese(paese);
            if (ris.isEmpty()) {
                paeseResultArea.setText("Nessun risultato trovato.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (CoordinateMonitoraggio area : ris) {
                    sb.append("Città: ").append(area.getNomeCitta())
                            .append("\nStato: ").append(area.getStato())
                            .append("\nPaese: ").append(area.getPaese())
                            .append("\nLatitudine: ").append(area.getLatitudine())
                            .append("\nLongitudine: ").append(area.getLongitudine())
                            .append("\n\n");
                }
                paeseResultArea.setText(sb.toString());
            }
        } catch (RemoteException e) {
            showAlert(Alert.AlertType.ERROR, "Errore di Connessione", "Errore del Server",
                    "Si è verificato un errore durante la ricerca: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewClimateData() {
        String nome = areaNameField.getText().trim();
        String stato = areaStateField.getText().trim();

        if (nome.isEmpty() || stato.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Errore di ricerca", "Campi vuoti", "Inserisci sia il nome dell'area che lo stato");
            return;
        }
        try {
            String ris = ClientCM.getService().visualizzaAreaGeografica(nome, stato);

            if (ris.isEmpty()) {
                climateDataResultArea.setText("Nessuna informazione disponibile per quest'area");
            } else {
                climateDataResultArea.setText(ris);
            }
        } catch (RemoteException e) {
            showAlert(Alert.AlertType.ERROR, "Errore di connessione", "Errore del server", "Si è verificato un errore durante la ricerca: " + e.getMessage());
        }
    }

    @FXML
    private void handleCreateMonitoringCenter() {
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Accesso negato", "Effettua il login come operatore");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Crea centro di monitoraggio");
        dialog.setHeaderText("Inserisci i dettagli del centro di monitoraggio");

        ButtonType createButtonType = new ButtonType("Crea", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nomeField = new TextField();
        TextField indirizzoField = new TextField();
        TextField capField = new TextField();
        TextField comuneField = new TextField();
        TextField provinciaField = new TextField();

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(nomeField, 1, 0);
        grid.add(new Label("Indirizzo:"), 0, 1);
        grid.add(indirizzoField, 1, 1);
        grid.add(new Label("CAP:"), 0, 2);
        grid.add(capField, 1, 2);
        grid.add(new Label("Comune:"), 0, 3);
        grid.add(comuneField, 1, 3);
        grid.add(new Label("Provincia:"), 0, 4);
        grid.add(provinciaField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                try {
                    boolean success = ClientCM.getService().creaCentroMonitoraggio(
                            currentUser.getId(),
                            nomeField.getText(),
                            indirizzoField.getText(),
                            capField.getText(),
                            comuneField.getText(),
                            provinciaField.getText()
                    );

                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Successo",
                                "Centro creato", "Il centro di monitoraggio è stato creato con successo.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Errore",
                                "Creazione fallita", "Non è stato possibile creare il centro di monitoraggio.");
                    }
                } catch (RemoteException e) {
                    showAlert(Alert.AlertType.ERROR, "Errore di connessione",
                            "Errore del server", "Si è verificato un errore durante la creazione del centro: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void handleCreateArea() {
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Accesso Negato", "Effettua il login come operatore");
            return;
        }
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Crea Area di Interesse");
        dialog.setHeaderText("Inserisci i dettagli dell'area di interesse");

        ButtonType createButtonType = new ButtonType("Crea", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField cittaField = new TextField();
        TextField statoField = new TextField();
        TextField paeseField = new TextField();
        TextField latitudineField = new TextField();
        TextField longitudineField = new TextField();

        grid.add(new Label("Città:"), 0, 0);
        grid.add(cittaField, 1, 0);
        grid.add(new Label("Stato:"), 0, 1);
        grid.add(statoField, 1, 1);
        grid.add(new Label("Paese:"), 0, 2);
        grid.add(paeseField, 1, 2);
        grid.add(new Label("Latitudine:"), 0, 3);
        grid.add(latitudineField, 1, 3);
        grid.add(new Label("Longitudine:"), 0, 4);
        grid.add(longitudineField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                try {
                    String citta = cittaField.getText();
                    String stato = statoField.getText();
                    double latitudine = Double.parseDouble(latitudineField.getText());
                    double longitudine = Double.parseDouble(longitudineField.getText());

                    boolean success = ClientCM.getService().creaAreaInteresse(
                            currentUser.getId(),
                            citta,
                            stato,
                            latitudine,
                            longitudine
                    );

                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Successo",
                                "Area creata", "L'area di interesse è stata creata con successo.");
                        updateAreaComboBox();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Errore",
                                "Creazione fallita", "Non è stato possibile creare l'area di interesse.");
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Errore di input",
                            "Formato non valido", "Inserisci coordinate valide.");
                } catch (RemoteException e) {
                    if (e.getMessage().contains("non ha un centro di monitoraggio")) {
                        showAlert(Alert.AlertType.ERROR, "Errore",
                                "Centro di Monitoraggio mancante",
                                "Devi prima creare un centro di monitoraggio prima di poter creare un'area di interesse.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Errore di connessione",
                                "Errore del server",
                                "Si è verificato un errore durante la creazione dell'area: " + e.getMessage());
                    }
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

    private void updateAreaComboBox() {
        if (areaComboBox != null && currentUser != null) {
            try {
                List<CoordinateMonitoraggio> aree = ClientCM.getService().getAreePerCentroMonitoraggio(currentUser.getId());

                areaComboBox.getItems().clear();
                areaComboBox.getItems().addAll(aree);
            } catch (RemoteException e) {
                showAlert(Alert.AlertType.ERROR, "Errore di aggiornamento",
                        "Impossibile aggiornare le aree",
                        "Si è verificato un errore durante l'aggiornamento delle aree: " + e.getMessage());
            }
        }
    }


    private void logClimateDataInsertion(CoordinateMonitoraggio selectedArea, LocalDate date,
                                         int vento, int umidita, int pressione, int temperatura,
                                         int precipitazioni, int altitudine, int massaGhiacciai, String note) {
        System.out.println("Tentativo di inserimento dati climatici:");
        System.out.println("Area: " + selectedArea.getNomeCitta() + ", " + selectedArea.getStato());
        System.out.println("Data: " + date);
        System.out.println("Vento (km/h): " + vento);
        System.out.println("Umidità (%): " + umidita);
        System.out.println("Pressione (hPa): " + pressione);
        System.out.println("Temperatura (°C): " + temperatura);
        System.out.println("Precipitazioni (mm): " + precipitazioni);
        System.out.println("Altitudine ghiacciai (m): " + altitudine);
        System.out.println("Massa ghiacciai (kg): " + massaGhiacciai);
        System.out.println("Note: " + note);
    }

    @FXML
    private void handlInsertClimateData() {
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Accesso negato", "Effettua il login come operatore");
            return;
        }


        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Inserisci Dati Climatici");
        dialog.setHeaderText("Inserisci i parametri climatici per un'area");

        ButtonType insertButtonType = new ButtonType("Inserisci", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(insertButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<CoordinateMonitoraggio> areaComboBox = new ComboBox<>();
        DatePicker dataPicker = new DatePicker();
        Spinner<Integer> ventoSpinner = new Spinner<>(0, 300, 0);
        Spinner<Integer> umiditaSpinner = new Spinner<>(0, 100, 50);
        Spinner<Integer> pressioneSpinner = new Spinner<>(900, 1100, 1013);
        Spinner<Integer> temperaturaSpinner = new Spinner<>(-50, 50, 20);
        Spinner<Integer> precipitazioniSpinner = new Spinner<>(0, 500, 0);
        Spinner<Integer> altitudineSpinner = new Spinner<>(0, 8000, 0);
        Spinner<Integer> massaGhiacciaiSpinner = new Spinner<>(0, 1000000, 0);
        TextArea noteArea = new TextArea();
        noteArea.setPrefRowCount(3);

        grid.add(new Label("Area:"), 0, 0);
        grid.add(areaComboBox, 1, 0);
        grid.add(new Label("Data:"), 0, 1);
        grid.add(dataPicker, 1, 1);
        grid.add(new Label("Vento (km/h):"), 0, 2);
        grid.add(ventoSpinner, 1, 2);
        grid.add(new Label("Umidità (%):"), 0, 3);
        grid.add(umiditaSpinner, 1, 3);
        grid.add(new Label("Pressione (hPa):"), 0, 4);
        grid.add(pressioneSpinner, 1, 4);
        grid.add(new Label("Temperatura (°C):"), 0, 5);
        grid.add(temperaturaSpinner, 1, 5);
        grid.add(new Label("Precipitazioni (mm):"), 0, 6);
        grid.add(precipitazioniSpinner, 1, 6);
        grid.add(new Label("Altitudine ghiacciai (m):"), 0, 7);
        grid.add(altitudineSpinner, 1, 7);
        grid.add(new Label("Massa ghiacciai (kg):"), 0, 8);
        grid.add(massaGhiacciaiSpinner, 1, 8);
        grid.add(new Label("Note:"), 0, 9);
        grid.add(noteArea, 1, 9);

        dialog.getDialogPane().setContent(grid);

        // Popola il ComboBox con le aree disponibili
        try {
            List<CoordinateMonitoraggio> aree = ClientCM.getService().getAreePerCentroMonitoraggio(currentUser.getId());
            areaComboBox.getItems().addAll(aree);
        } catch (RemoteException e) {
            showAlert(Alert.AlertType.ERROR, "Errore di caricamento",
                    "Impossibile caricare le aree", "Si è verificato un errore nel caricamento delle aree: " + e.getMessage());
        }

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == insertButtonType) {
                CoordinateMonitoraggio selectedArea = areaComboBox.getValue();
                LocalDate selectedDate = dataPicker.getValue();
                if (selectedArea == null || dataPicker.getValue() == null || selectedDate==null) {
                    showAlert(Alert.AlertType.ERROR, "Dati mancanti",
                            "Campi obbligatori", "Seleziona un'area e una data.");
                    return null;
                }

                //loggin dei dati
                logClimateDataInsertion(
                        selectedArea,
                        selectedDate,
                        ventoSpinner.getValue(),
                        umiditaSpinner.getValue(),
                        pressioneSpinner.getValue(),
                        temperaturaSpinner.getValue(),
                        precipitazioniSpinner.getValue(),
                        altitudineSpinner.getValue(),
                        massaGhiacciaiSpinner.getValue(),
                        noteArea.getText()
                );

                if (!validateClimateData(ventoSpinner.getValue(), umiditaSpinner.getValue(),
                        pressioneSpinner.getValue(), temperaturaSpinner.getValue(),
                        precipitazioniSpinner.getValue(), altitudineSpinner.getValue(),
                        massaGhiacciaiSpinner.getValue())) {
                    showAlert(Alert.AlertType.ERROR, "Dati non validi",
                            "I dati inseriti non sono validi", "Controlla i valori e assicurati che siano nel range corretto.");
                    return null;
                }


                try {
                    boolean success = ClientCM.getService().inserisciParametriClimatici(
                            currentUser.getId(),
                            null,
                            selectedArea.getId(),
                            java.sql.Date.valueOf(dataPicker.getValue()),
                            ventoSpinner.getValue(),
                            umiditaSpinner.getValue(),
                            pressioneSpinner.getValue(),
                            temperaturaSpinner.getValue(),
                            precipitazioniSpinner.getValue(),
                            altitudineSpinner.getValue(),
                            massaGhiacciaiSpinner.getValue(),
                            noteArea.getText()
                    );


                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Successo",
                                "Dati inseriti", "I parametri climatici sono stati inseriti con successo.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Errore",
                                "Inserimento fallito", "Non è stato possibile inserire i parametri climatici.");
                    }
                } catch (RemoteException e) {
                    showAlert(Alert.AlertType.ERROR, "Errore di connessione",
                            "Errore del server", "Si è verificato un errore durante l'inserimento dei dati: " + e.getMessage());
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Errore imprevisto",
                            "Si è verificato un errore inaspettato", "Dettagli: " + e.getMessage());
                    e.printStackTrace(); // Log dell'eccezione per il debugging
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private boolean validateClimateData(int vento, int umidita, int pressione, int temperatura,
                                        int precipitazioni, int altitudine, int massaGhiacciai) {
        if (vento < 0 || vento > 300) return false;
        if (umidita < 0 || umidita > 100) return false;
        if (pressione < 900 || pressione > 1100) return false;
        if (temperatura < -50 || temperatura > 50) return false;
        if (precipitazioni < 0 || precipitazioni > 500) return false;
        if (altitudine < 0 || altitudine > 8000) return false;
        if (massaGhiacciai < 0 || massaGhiacciai > 1000000) return false;
        return true;
    }
    private void handleLogout() {
        mainApp.showLoginView();
        setCurrentUser(null);
    }

    private void displayResult(List<CoordinateMonitoraggio> results, TextArea targetArea) {
        if (results.isEmpty()) {
            targetArea.setText("Nessun risultato trovato");
        } else {
            StringBuilder sb = new StringBuilder();
            for (CoordinateMonitoraggio area : results) {
                sb.append("Città: ").append(area.getNomeCitta())
                        .append("\nStato: ").append(area.getStato())
                        .append("\nPaese: ").append(area.getPaese())
                        .append("\nLatitudine: ").append(area.getLatitudine())
                        .append("\nLongitudine: ").append(area.getLongitudine())
                        .append("\n\n");
            }
            targetArea.setText(sb.toString());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
