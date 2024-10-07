package com.climatemonitoring.model;

import java.io.Serializable;
import java.util.Date;

public class ParametriClimatici implements Serializable {

    private static final long serialVersionUID=1l;

    private int id;
    private Date data_rilevazione;
    private int vento;
    private int umidita;
    private int pressione;
    private int temperatura;
    private int precipitazioni;
    private int altitudine;
    private int massa_ghiacciai;
    private String note;

    public ParametriClimatici(int id, Date data_rilevazione, int vento, int umidita, int pressione, int temperatura, int precipitazioni, int altitudine, int massa_ghiacciai, String note) {
        this.id = id;
        this.data_rilevazione = data_rilevazione;
        this.vento = vento;
        this.umidita = umidita;
        this.pressione = pressione;
        this.temperatura = temperatura;
        this.precipitazioni = precipitazioni;
        this.altitudine = altitudine;
        this.massa_ghiacciai = massa_ghiacciai;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData_rilevazione() {
        return data_rilevazione;
    }

    public void setData_rilevazione(Date data_rilevazione) {
        this.data_rilevazione = data_rilevazione;
    }

    public int getVento() {
        return vento;
    }

    public void setVento(int vento) {
        this.vento = vento;
    }

    public int getUmidita() {
        return umidita;
    }

    public void setUmidita(int umidita) {
        this.umidita = umidita;
    }

    public int getPressione() {
        return pressione;
    }

    public void setPressione(int pressione) {
        this.pressione = pressione;
    }

    public int getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(int temperatura) {
        this.temperatura = temperatura;
    }

    public int getPrecipitazioni() {
        return precipitazioni;
    }

    public void setPrecipitazioni(int precipitazioni) {
        this.precipitazioni = precipitazioni;
    }

    public int getAltitudine() {
        return altitudine;
    }

    public void setAltitudine(int altitudine) {
        this.altitudine = altitudine;
    }

    public int getMassa_ghiacciai() {
        return massa_ghiacciai;
    }

    public void setMassa_ghiacciai(int massa_ghiacciai) {
        this.massa_ghiacciai = massa_ghiacciai;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
