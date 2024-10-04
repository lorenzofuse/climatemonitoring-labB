package com.climatemonitoring.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

    /*
    utilizzare nel contesto dell'applicazione JavaFX
    e delle TableView, sarà necessario adattarla con il binding delle proprietà,
    Per fare in modo che i dati possano essere visualizzati e aggiornati dinamicamente nella GUI.
     */

public class CoordinateMonitoraggio {

    private IntegerProperty id;
    private StringProperty nome_citta;
    private StringProperty stato;
    private StringProperty paese;
    private DoubleProperty latitudine;
    private DoubleProperty longitudine;

    // Costruttore con parametri
    public CoordinateMonitoraggio(int id, String nome_citta, String stato, String paese, Double latitudine, Double longitudine) {
        this.id = new SimpleIntegerProperty(id);
        this.nome_citta = new SimpleStringProperty(nome_citta);
        this.stato = new SimpleStringProperty(stato);
        this.paese = new SimpleStringProperty(paese);
        this.latitudine = new SimpleDoubleProperty(latitudine);
        this.longitudine = new SimpleDoubleProperty(longitudine);
    }

    // Costruttore senza parametri (necessario per alcuni utilizzi)
    public CoordinateMonitoraggio() {
        this(0, "", "", "", 0.0, 0.0);
    }

    // Getter e Setter per 'id'
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // Getter e Setter per 'nome_citta'
    public String getNome_citta() {
        return nome_citta.get();
    }

    public void setNome_citta(String nome_citta) {
        this.nome_citta.set(nome_citta);
    }

    public StringProperty nome_cittaProperty() {
        return nome_citta;
    }

    // Getter e Setter per 'stato'
    public String getStato() {
        return stato.get();
    }

    public void setStato(String stato) {
        this.stato.set(stato);
    }

    public StringProperty statoProperty() {
        return stato;
    }

    // Getter e Setter per 'paese'
    public String getPaese() {
        return paese.get();
    }

    public void setPaese(String paese) {
        this.paese.set(paese);
    }

    public StringProperty paeseProperty() {
        return paese;
    }

    // Getter e Setter per 'latitudine'
    public Double getLatitudine() {
        return latitudine.get();
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine.set(latitudine);
    }

    public DoubleProperty latitudineProperty() {
        return latitudine;
    }

    // Getter e Setter per 'longitudine'
    public Double getLongitudine() {
        return longitudine.get();
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine.set(longitudine);
    }

    public DoubleProperty longitudineProperty() {
        return longitudine;
    }
}
