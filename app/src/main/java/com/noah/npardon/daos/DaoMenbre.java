package com.noah.npardon.daos;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noah.npardon.beans.Menbre;
import com.noah.npardon.net.WSConnexionHTTPS;
import com.noah.npardon.ui.Connexion;
import com.noah.npardon.ui.MainActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DaoMenbre   {
    private static DaoMenbre instance = null;
    private final Context context;
    private final ObjectMapper om = new ObjectMapper();

    private DaoMenbre(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new DaoMenbre(context);
        }
    }

    public static DaoMenbre getInstance() {
        return instance;
    }

    public void getConnexion(String login, String password) {
        String url = "requete=connexion&login=" + login + "&password=" + password;
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    traiterRetourGetConnexion(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourGetConnexion(String s) throws JSONException {
        JSONObject jo = new JSONObject(s);
        if (jo.getBoolean("success")) {
            JSONObject response = jo.getJSONObject("response");
            String nom = response.getString("prenom");
            Toast.makeText(context, "Bonjour " + nom, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

        } else {
            Toast.makeText(context, "Vous n'avez pas pu vous connecter", Toast.LENGTH_SHORT).show();

        }
    }

    public void getInscription(Menbre e) {
        String url = "requete=connexion&login=" + e.getLogin() + "&password=" + e.getPassword();
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    traiterRetourGetConnexion(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url);
    }
}


