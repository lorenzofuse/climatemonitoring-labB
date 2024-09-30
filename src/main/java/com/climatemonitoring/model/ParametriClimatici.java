package com.climatemonitoring.model;

import java.time.LocalDate;

public class ParametriClimatici {
    private int idParametriClimatici;
    private OperatoreRegistrato centromonitoraggioid;
    private ParametriClimatici areaInteresse;
    private LocalDate dataRilevazione;
    private int vento;
    private int umidita;
    private int pressione;
    private int temperatura;
    private int precipitazioni;
    private int altitudineGhiacciai;
    private int massaGhiacciai;
    private String note;

    public ParametriClimatici(int idParametriClimatici, OperatoreRegistrato centromonitoraggioid, ParametriClimatici areaInteresse, LocalDate dataRilevazione, int vento, int umidita, int pressione, int temperatura, int precipitazioni, int altitudineGhiacciai, int massaGhiacciai, String note) {
        this.idParametriClimatici = idParametriClimatici;
        this.centromonitoraggioid = centromonitoraggioid;
        this.areaInteresse = areaInteresse;
        this.dataRilevazione = dataRilevazione;
        this.vento = vento;
        this.umidita = umidita;
        this.pressione = pressione;
        this.temperatura = temperatura;
        this.precipitazioni = precipitazioni;
        this.altitudineGhiacciai = altitudineGhiacciai;
        this.massaGhiacciai = massaGhiacciai;
        this.note = note;
    }

    public int getIdParametriClimatici() {
        return idParametriClimatici;
    }

    public void setIdParametriClimatici(int idParametriClimatici) {
        this.idParametriClimatici = idParametriClimatici;
    }

    public OperatoreRegistrato getCentromonitoraggioid() {
        return centromonitoraggioid;
    }

    public void setCentromonitoraggioid(OperatoreRegistrato centromonitoraggioid) {
        this.centromonitoraggioid = centromonitoraggioid;
    }

    public ParametriClimatici getAreaInteresse() {
        return areaInteresse;
    }

    public void setAreaInteresse(ParametriClimatici areaInteresse) {
        this.areaInteresse = areaInteresse;
    }

    public LocalDate getDataRilevazione() {
        return dataRilevazione;
    }

    public void setDataRilevazione(LocalDate dataRilevazione) {
        this.dataRilevazione = dataRilevazione;
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

    public int getAltitudineGhiacciai() {
        return altitudineGhiacciai;
    }

    public void setAltitudineGhiacciai(int altitudineGhiacciai) {
        this.altitudineGhiacciai = altitudineGhiacciai;
    }

    public int getMassaGhiacciai() {
        return massaGhiacciai;
    }

    public void setMassaGhiacciai(int massaGhiacciai) {
        this.massaGhiacciai = massaGhiacciai;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "ParametriClimatici{" +
                "id=" + idParametriClimatici+
                "centromonitoraggio=" + centromonitoraggioid+
                "areaInteresse=" + areaInteresse+
                ", dataRilevazione=" + dataRilevazione +
                ", vento=" + vento +
                ", umidita=" + umidita +
                ", pressione=" + pressione +
                ", temperatura=" + temperatura +
                ", precipitazioni=" + precipitazioni +
                ", altitudineGhiacciai=" + altitudineGhiacciai +
                ", massaGhiacciai=" + massaGhiacciai +
                ", note='" + note + '\'' +
                '}';
    }
}
