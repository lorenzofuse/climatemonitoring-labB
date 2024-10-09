package com.climatemonitoring.model;

import java.io.Serializable;

public class CoordinateMonitoraggio implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nomeCitta;
    private String stato;
    private String paese;
    private double latitudine;
    private double longitudine;

    // Costruttore vuoto necessario per la serializzazione
    public CoordinateMonitoraggio() {}

    // Costruttore con tutti i campi
    public CoordinateMonitoraggio(int id, String nomeCitta, String stato, String paese,
                                  double latitudine, double longitudine) {
        this.id = id;
        this.nomeCitta = nomeCitta;
        this.stato = stato;
        this.paese = paese;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    // Getter e setter standard
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCitta() {
        return nomeCitta;
    }

    public void setNomeCitta(String nomeCitta) {
        this.nomeCitta = nomeCitta;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getPaese() {
        return paese;
    }

    public void setPaese(String paese) {
        this.paese = paese;
    }

    public double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }

    @Override
    public String toString() {
        return "CoordinateMonitoraggio{" +
                "id=" + id +
                ", nomeCitta='" + nomeCitta + '\'' +
                ", stato='" + stato + '\'' +
                ", paese='" + paese + '\'' +
                ", latitudine=" + latitudine +
                ", longitudine=" + longitudine +
                '}';
    }

    public void add(CoordinateMonitoraggio area) { area.add(area);
    }
}