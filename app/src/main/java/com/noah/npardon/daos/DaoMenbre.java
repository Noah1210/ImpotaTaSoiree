package com.noah.npardon.daos;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noah.npardon.beans.Menbre;
import com.noah.npardon.net.WSConnexionHTTPS;


import org.json.JSONException;
import org.json.JSONObject;

public class DaoMenbre {
    private static DaoMenbre instance = null;
    private final Context context;
    private final List<Menbre> etudiants;
    private final ObjectMapper om = new ObjectMapper();

    private DaoMenbre(Context context) {
        this.context = context;
    }

    private DaoMenbre() {
        etudiants = new ArrayList<>();
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new DaoMenbre(context);
        }
    }

    public static DaoMenbre getInstance() {
        return instance;
    }

    public void getConnexion(String login, String password, DelegateAsyncTask delegate) {
        String url = "requete=connexion&login=" + login + "&password=" + password;
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    traiterRetourGetConnexion(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourGetConnexion(String s, DelegateAsyncTask delegate) throws JSONException {
        JSONObject jo = new JSONObject(s);
        Menbre me = null;
        if (jo.getBoolean("success")) {
            JSONObject response = jo.getJSONObject("response");
            String login = response.getString("login");
            String nom = response.getString("nom");
            String prenom = response.getString("prenom");
            String ddn = response.getString("ddn");
            String mail = response.getString("mail");
            me = new Menbre(login, nom, prenom, ddn, mail, "");
        }
        delegate.whenWSIsTerminated(me);
    }

    public void getInscription(Menbre e, DelegateAsyncTask delegate) {
        String url = "requete=creerCompte&login=" + e.getLogin() + "&nom=" + e.getNom() + "&prenom=" + e.getPrenom() + "&ddn=" + e.getDdn() + "&mail=" + e.getMail() + "&password=" + e.getPassword();
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    traiterRetourGetInscription(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourGetInscription(String s, DelegateAsyncTask delegate) throws JSONException {
        JSONObject jo = new JSONObject(s);
        Boolean res = false;
        if (jo.getBoolean("success")) {
            res = true;
        }
        delegate.whenWSIsTerminated(res);

    }
}


