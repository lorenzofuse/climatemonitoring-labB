package com.climatemonitoring.model;

public class OperatoreRegistrato {
    private String idOperatore;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String email;
    private String userId;
    private String password;
    private String centroMonitoraggioId;

    public OperatoreRegistrato(String idOperatore, String nome, String cognome, String codiceFiscale, String email, String userId, String password, String centroMonitoraggioId) {
        this.idOperatore = idOperatore;
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
        this.userId = userId;
        this.password = password;
        this.centroMonitoraggioId = centroMonitoraggioId;
    }

    public String getIdOperatore() {
        return idOperatore;
    }

    public void setIdOperatore(String idOperatore) {
        this.idOperatore = idOperatore;
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

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCentroMonitoraggioId() {
        return centroMonitoraggioId;
    }

    public void setCentroMonitoraggioId(String centroMonitoraggioId) {
        this.centroMonitoraggioId = centroMonitoraggioId;
    }

    @Override
    public String toString() {
        return "OperatoreRegistrato{" +
                "  id-operatore='" + idOperatore + '\'' +
                "  nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", codiceFiscale='" + codiceFiscale + '\'' +
                ", email='" + email + '\'' +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", centroMonitoraggio='" + centroMonitoraggioId + '\'' +
                '}';
    }
}
