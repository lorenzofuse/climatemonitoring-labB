package com.climatemonitoring.model;

import java.io.Serializable;

public class OperatoriRegistrati implements Serializable {
    private static final long serialVersionUID=1l;

    private int id;
    private String nome;
    private String cognome;
    private String codice_fiscale;
    private String email;
    private String userid;
    private String password;


    public OperatoriRegistrati(int id, String nome, String cognome, String codice_fiscale, String email, String userid, String password) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.codice_fiscale = codice_fiscale;
        this.email = email;
        this.userid = userid;
        this.password = password;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCodice_fiscale() {
        return codice_fiscale;
    }

    public void setCodice_fiscale(String codice_fiscale) {
        this.codice_fiscale = codice_fiscale;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

