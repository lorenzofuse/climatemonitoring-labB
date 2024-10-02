package com.climatemonitoring.model;
public class CoordinateMonitoraggio {
    private int id;
    private String nome_citta;
    private String stato;
    private String paese;
    private Double latitudine;
    private Double longitudine;

    public CoordinateMonitoraggio(int id, String nome_citta, String stato, String paese, Double latitudine, Double longitudine) {
        this.id = id;
        this.nome_citta = nome_citta;
        this.stato = stato;
        this.paese = paese;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome_citta() {
        return nome_citta;
    }

    public void setNome_citta(String nome_citta) {
        this.nome_citta = nome_citta;
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

    public Double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine = latitudine;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine = longitudine;
    }
}
