package com.noah.npardon.daos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noah.npardon.beans.Soiree;
import com.noah.npardon.net.WSConnexionHTTPS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DaoSoiree {
    private static DaoSoiree instance = null;
    private final List<Soiree> soirees;
    private final ObjectMapper om = new ObjectMapper();

    private DaoSoiree() {
        soirees = new ArrayList<>();
    }

    public List<Soiree> getLocalSoirees() {
        return soirees;
    }

    public static DaoSoiree getInstance() {
        if (instance == null) {
            instance = new DaoSoiree();
        }
        return instance;
    }

    public void getSoirees(DelegateAsyncTask delegate) {
        String url = "requete=getLesSoirees";
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                traiterRetourGetSoirees(s, delegate);
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourGetSoirees(String s, DelegateAsyncTask delegate) {
        soirees.clear();
        //Arrays.asList(om.readValue(s, Soiree[].class)).forEach(soiree -> soirees.add(soiree));
        try {
            JSONObject jsGlobal = new JSONObject(s);
            JSONArray ja = jsGlobal.getJSONArray("response");
            for(int i = 0; i< ja.length() ; i++){
                JSONObject jsResult = ja.getJSONObject(i);
                String id = jsResult.getString("id");
                String lib = jsResult.getString("libelleCourt");
                String desc = jsResult.getString("descriptif");
                String date = jsResult.getString("dateDebut");
                String heure = jsResult.getString("heureDebut");
                String lat = jsResult.getString("latitude");
                double lati = Double.parseDouble(lat);
                String longi = jsResult.getString("longitude");
                double longit = Double.parseDouble(longi);
                String login = jsResult.getString("login");
                Soiree so = new Soiree(id, lib, desc, date, heure, lati, longit, login);
                soirees.add(so);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        delegate.whenWSIsTerminated(s);

    }

    public void addSoiree(Soiree so, DelegateAsyncTask delegate) {
        String url = "requete=addSoiree&libelleCourt=" + so.getLibelleCourt() + "&descriptif=" + so.getDescriptif() + "&dateDebut=" + so.getDateDebut()
                +"&heureDebut=" + so.getHeureDebut() + "&adresse=" + so.getAdresse();
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    traiterRetouraddSoiree(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetouraddSoiree(String s, DelegateAsyncTask delegate) throws JSONException {
        JSONObject jo = new JSONObject(s);
        Boolean res = false;
        if (jo.getBoolean("success")) {
            res = true;
        }
        delegate.whenWSIsTerminated(res);
    }

    public void delSoiree(Soiree so, DelegateAsyncTask delegate) {
        String url = "requete=delSoiree&soiree=" + so.getId();
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    traiterRetourDelSoiree(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourDelSoiree(String s, DelegateAsyncTask delegate) throws JSONException {
        JSONObject jo = new JSONObject(s);
        Boolean res = false;
        if (jo.getBoolean("success")) {
            res = true;
        }
        delegate.whenWSIsTerminated(res);
    }

    public void inscrireSoiree(Soiree so, DelegateAsyncTask delegate) {
        String url = "requete=inscrire&soiree=" + so.getId();
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    traiterRetourInscrireSoiree(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourInscrireSoiree(String s, DelegateAsyncTask delegate) throws JSONException {
        JSONObject jo = new JSONObject(s);
        Boolean res = false;
        if (jo.getBoolean("response")) {
            res = true;
        }
        delegate.whenWSIsTerminated(res);
    }

    public void desinscrireSoiree(Soiree so, DelegateAsyncTask delegate) {
        String url = "requete=desinscrire&soiree=" + so.getId();
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    traiterRetourDesinscrireSoiree(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourDesinscrireSoiree(String s, DelegateAsyncTask delegate) throws JSONException {
        JSONObject jo = new JSONObject(s);
        Boolean res = false;
        if (jo.getBoolean("response")) {
            res = true;
        }
        delegate.whenWSIsTerminated(res);
    }

    public void verifInscription(Soiree so, DelegateAsyncTask delegate) {
        String url = "requete=isInscrit&soiree=" + so.getId();
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    traiterRetourVerifInscriptionSoiree(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourVerifInscriptionSoiree(String s, DelegateAsyncTask delegate) throws JSONException {
        JSONObject jo = new JSONObject(s);
        Boolean res = false;
        if (jo.getBoolean("response")) {
            res = true;
        }
        delegate.whenWSIsTerminated(res);
    }
}
