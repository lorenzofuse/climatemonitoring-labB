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
    boolean isAreaInteresse;

    public CoordinateMonitoraggio() {}

    public CoordinateMonitoraggio(int id, String nomeCitta, String stato, String paese,
                                  double latitudine, double longitudine) {
        this.id = id;
        this.nomeCitta = nomeCitta;
        this.stato = stato;
        this.paese = paese;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    public CoordinateMonitoraggio(int id, String nome, String stato, String paese,
                                  double latitudine, double longitudine, boolean isAreaInteresse) {
        this.id = id;
        this.nomeCitta = nome;
        this.stato = stato;
        this.paese = paese;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.isAreaInteresse = isAreaInteresse;
    }
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
        return String.format("%s, %s (Lat: %.2f, Long: %.2f)", nomeCitta, stato, latitudine, longitudine);
    }

    public boolean isAreaInteresse() {
        return isAreaInteresse;
    }

    public void setAreaInteresse(boolean areaInteresse) {
        isAreaInteresse = areaInteresse;
    }
}