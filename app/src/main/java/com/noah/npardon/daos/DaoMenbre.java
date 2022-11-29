package com.noah.npardon.daos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noah.npardon.beans.Menbre;
import com.noah.npardon.beans.Soiree;
import com.noah.npardon.net.WSConnexionHTTPS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DaoMenbre {
    private static DaoMenbre instance = null;
    private final List<Menbre> menbres;
    //private final ObjectMapper om = new ObjectMapper();

    private DaoMenbre() {
        menbres = new ArrayList<>();
    }

    public List<Menbre> getLocalMenbres() {
        return menbres;
    }

    public static DaoMenbre getInstance() {
        if (instance == null) {
            instance = new DaoMenbre();
        }
        return instance;
    }
    /**
     * Envoie une requête au serveur web afin de récupérer la liste des participants
     * @param s La Soiree en dont ont veut connaitre les participants
     * @param delegate La methode whenWSIsTerminated de la classe abstraite DelegateAsyncTask
     * @see DelegateAsyncTask
     */
    public void getParticipants(Soiree s, DelegateAsyncTask delegate) {
        String url = "requete=getLesParticipants&soiree="+s.getId();
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                traiterRetourGetParticipants(s, delegate);
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    /**
     * Gère le retour de getParticipants et crée des Menbres participants avant de l'ajouter à la liste des menbres
     * @param s Le resultat de getParticipants en String
     * @param delegate La methode whenWSIsTerminated de la classe abstraite DelegateAsyncTask
     * @see DelegateAsyncTask
     */
    private void traiterRetourGetParticipants(String s, DelegateAsyncTask delegate) {
        menbres.clear();
        //Arrays.asList(om.readValue(s, Soiree[].class)).forEach(soiree -> soirees.add(soiree));
        try {
            JSONObject jsGlobal = new JSONObject(s);
            JSONArray ja = jsGlobal.getJSONArray("response");
            for(int i = 0; i< ja.length() ; i++){
                JSONObject jsResult = ja.getJSONObject(i);

                String login = jsResult.getString("login");
                String nom = jsResult.getString("nom");
                String prenom = jsResult.getString("prenom");
                String ddn = jsResult.getString("ddn");
                String mail = jsResult.getString("mail");

                Menbre me = new Menbre(login, nom, prenom, ddn, mail);
                menbres.add(me);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        delegate.whenWSIsTerminated(s);
    }
    /**
     * Envoie une requête au serveur web afin de vérifier le login et mdp passé en paramètres
     * @param login Le login de l'utilisateur esseyant de se connecter
     * @param password Le mdp de l'utilisateur esseyant de se connecter
     * @param delegate La methode whenWSIsTerminated de la classe abstraite DelegateAsyncTask
     * @see DelegateAsyncTask
     */
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
    /**
     * Gère le retour de getConnexion et crée un Menbre utilisateurConnecter
     * @param s Le resultat de getConnexion en String
     * @param delegate La methode whenWSIsTerminated de la classe abstraite DelegateAsyncTask
     * @see DelegateAsyncTask
     */
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
    /**
     * Envoie une requête au serveur web afin de crée un nouveau compte
     * @param e Le Menbre qu'il faut crée
     * @param delegate La methode whenWSIsTerminated de la classe abstraite DelegateAsyncTask
     * @see DelegateAsyncTask
     */
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
    /**
     * Gère le retour de getInscription et renvoie un Boolean du success de la requête
     * @param s Le resultat de getInscription en String
     * @param delegate La methode whenWSIsTerminated de la classe abstraite DelegateAsyncTask
     * @see DelegateAsyncTask
     */
    private void traiterRetourGetInscription(String s, DelegateAsyncTask delegate) throws JSONException {
        JSONObject jo = new JSONObject(s);
        Boolean res = false;
        if (jo.getBoolean("success")) {
            res = true;
        }
        delegate.whenWSIsTerminated(res);

    }
    /**
     * Envoie une requête au serveur web afin de déconnecter le menbre actuellement connecté
     * @param delegate La methode whenWSIsTerminated de la classe abstraite DelegateAsyncTask
     * @see DelegateAsyncTask
     */
    public void getDeconnexion(DelegateAsyncTask delegate) {
        String url = "requete=deconnexion";
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    traiterRetourDeconnexion(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url);
    }
    /**
     * Gère le retour de getDeconnexion et renvoie un Boolean du success de la requête
     * @param s Le resultat de getInscription en String
     * @param delegate La methode whenWSIsTerminated de la classe abstraite DelegateAsyncTask
     * @see DelegateAsyncTask
     */
    private void traiterRetourDeconnexion(String s, DelegateAsyncTask delegate) throws JSONException {
        JSONObject jo = new JSONObject(s);
        Boolean res = false;
        if (jo.getBoolean("success")) {
            res = true;
        }
        delegate.whenWSIsTerminated(res);
    }
    /**
     * Envoie une requête au serveur web afin de supprimer le compte du menbre actuellement connecté
     * @param delegate La methode whenWSIsTerminated de la classe abstraite DelegateAsyncTask
     * @see DelegateAsyncTask
     */
    public void getDelAccount(DelegateAsyncTask delegate) {
        String url = "requete=supprimerCompte";
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    traiterGetDelAccount(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url);
    }
    /**
     * Gère le retour de getDelAccount et renvoie un Boolean du success de la requête
     * @param s Le resultat de getInscription en String
     * @param delegate La methode whenWSIsTerminated de la classe abstraite DelegateAsyncTask
     * @see DelegateAsyncTask
     */
    private void traiterGetDelAccount   (String s, DelegateAsyncTask delegate) throws JSONException {
        JSONObject jo = new JSONObject(s);
        Boolean res = false;
        if (jo.getBoolean("success")) {
            res = true;
        }
        delegate.whenWSIsTerminated(res);
    }
    /**
     * Envoie une requête au serveur web afin de récuperer le menbre dont le login est passer en paramètre
     * @param login Le login du menbre que l'on veut récuperer
     * @param delegate La methode whenWSIsTerminated de la classe abstraite DelegateAsyncTask
     * @see DelegateAsyncTask
     */
    public void getMembreByLogin(String login, DelegateAsyncTask delegate) {
        String url = "requete=getMembreByLogin&login=" + login;
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    traiterRetourGetMembreByLogin(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url);
    }
    /**
     * Gère le retour de getMembreByLogin et renvoie un Menbre au success de la requête
     * @param s Le resultat de getMembreByLogin en String
     * @param delegate La methode whenWSIsTerminated de la classe abstraite DelegateAsyncTask
     * @see DelegateAsyncTask
     */
    private void traiterRetourGetMembreByLogin(String s, DelegateAsyncTask delegate) throws JSONException {
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

    public void postPhoto(String photoB64) {
        String url = "requete=uploadPhoto";
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS();
        wsConnexionHTTPS.execute(url,photoB64);
    }
}


