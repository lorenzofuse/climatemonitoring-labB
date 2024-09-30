package com.climatemonitoring.model;
import java.util.List;

public class CentroMonitoraggio {
    private int idCentro;
    private String nome;
    private String indirizzo;
    private String cap;
    private String comune;
    private String provincia;
    private List<CoordinateMonitoraggio> areeInteresse;

    public CentroMonitoraggio(int idCentro, String nome, String indirizzo, String cap, String comune, String provincia,List<CoordinateMonitoraggio> areeInteresse) {
        this.idCentro = idCentro;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.cap = cap;
        this.comune = comune;
        this.provincia = provincia;
        this.areeInteresse = areeInteresse;
    }

    public int getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(int idCentro) {
        this.idCentro = idCentro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    @Override
    public String toString() {
        return "CentroMonitoraggio{" +
                "nome='" + nome + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", comune='" + comune + '\'' +
                ", provincia='" + provincia + '\'' +
                ", cap='" + cap + '\'' +
                ", areeInteresse=" + areeInteresse +
                '}';
    }
}
