package com.noah.npardon.beans;

import java.io.Serializable;

public class Soiree implements Serializable {
    private String id;
    private String libelleCourt;
    private String descriptif;
    private String dateDebut;
    private String heureDebut;
    private String adresse;
    private double latitude;
    private double longitude;
    private String login;

    public Soiree(){}

    public Soiree(String id, String libelleCourt, String descriptif, String dateDebut, String heureDebut, double latitude, double longitude, String login) {
        this.id = id;
        this.libelleCourt = libelleCourt;
        this.descriptif = descriptif;
        this.dateDebut = dateDebut;
        this.heureDebut = heureDebut;
        this.latitude = latitude;
        this.longitude = longitude;
        this.login = login;
    }

    public Soiree(String id, String libelleCourt, String descriptif, String dateDebut, String heureDebut, String adresse, String login) {
        this.id = id;
        this.libelleCourt = libelleCourt;
        this.descriptif = descriptif;
        this.dateDebut = dateDebut;
        this.heureDebut = heureDebut;
        this.adresse = adresse;
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public String getLibelleCourt() {
        return libelleCourt;
    }

    public String getDescriptif() {
        return descriptif;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public String getAdresse() {
        return adresse;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return libelleCourt + " "+ "("+ dateDebut + ")";
    }
}
