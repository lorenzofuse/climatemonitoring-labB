package com.climatemonitoring.model;

import java.io.Serializable;

public class CentroMonitoraggio implements Serializable {

    private static final long serialVersionUID=1l;


    private int id;
    private String nome;
    private String indirizzo;
    private String cap;
    private String comune;
    private String provincia;

    public CentroMonitoraggio(int id, String nome, String indirizzo, String cap, String comune, String provincia) {
        this.id = id;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.cap = cap;
        this.comune = comune;
        this.provincia = provincia;
    }

    // Getter e setter

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    public String getCap() { return cap; }
    public void setCap(String cap) { this.cap = cap; }

    public String getComune() { return comune; }
    public void setComune(String comune) { this.comune = comune; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
}
