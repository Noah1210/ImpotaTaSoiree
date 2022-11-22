package com.noah.npardon.beans;

public class Menbre {
    private String login;
    private String nom;
    private String prenom;
    private String ddn;
    private String mail;
    private String password;

    public Menbre(String login, String nom, String prenom, String ddn, String mail, String password) {
        this.login = login;
        this.nom = nom;
        this.prenom = prenom;
        this.ddn = ddn;
        this.mail = mail;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getDdn() {
        return ddn;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }
}
