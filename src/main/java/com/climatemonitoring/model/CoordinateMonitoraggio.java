package com.climatemonitoring.model;

public class CoordinateMonitoraggio {
    private int idn;
    private String nome_citta;
    private String stato;
    private String paese;
    private double latitudine;
    private double longitudine;

    public CoordinateMonitoraggio(int id, String nome_citta, String stato, String paese,Double latitudine, Double longitudine){
        this.idn=id;
        this.nome_citta=nome_citta;
        this.stato=stato;
        this.paese=paese;
        this.latitudine=latitudine;
        this.longitudine=longitudine;
    }

    public int getId() {
        return idn;
    }

    public void setId(int id) {
        this.idn = id;
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

    public String toString(){
        return "CoordinateMonitoraggio{" +
                ", id='" + idn + '\'' +
                ", citta='" + nome_citta + '\'' +
                ", stato='" + stato + '\'' +
                ", paese='" + paese + '\'' +
                "  latitudine=" + latitudine +
                ", longitudine=" + longitudine +
                '}';
    }
}
