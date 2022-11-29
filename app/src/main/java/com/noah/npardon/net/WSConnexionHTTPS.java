package com.noah.npardon.net;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.noah.npardon.ui.Connexion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class WSConnexionHTTPS extends AsyncTask<String,Integer,String> {
    private final String base_url = "https://sio.jbdelasalle.com/~amedassi/soirees/ws.php?";
    public static final MediaType JSON = MediaType.get("text/plain; charset=utf-8");
    private static OkHttpClient client = null;
    Request request;

    public WSConnexionHTTPS() {
        if( client==null) {
            try {
                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
                newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
                newBuilder.hostnameVerifier((hostname, session) -> true);
                newBuilder.cookieJar(new CookieJar() {
                    List<Cookie> cookies;

                    @Override
                    public void saveFromResponse(HttpUrl hu, List<Cookie> list) {
                        this.cookies = list;
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl hu) {
                        if (cookies != null) {
                            return cookies;
                        } else {
                            return new ArrayList<>();
                        }
                    }
                });
                client = newBuilder.build();
            }catch (Exception ex){
                Log.e("WSHTTPS",ex.getMessage()) ;
            }
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        if (strings.length==2){
            Log.d("yep", "yep yep " + strings[0]);
            String b64 = strings[1] ;
            //String b64 = "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" ;
            String json = "{\"photo\":\""+b64+"\"}" ;
            Log.d("json", "String json = " + json);
            RequestBody body = RequestBody.create(json, JSON);
            request = new Request.Builder()
                    .url(base_url + strings[0])
                    .post(body)
                    .build();
        }else{
            Log.d("not yep yep", "yep yep" + strings[0]);
            request = new Request.Builder()
                    .url(base_url + strings[0])
                    .build();
        }

        Log.d("REQUEST",base_url + strings[0]) ;
        try ( Response response = client.newCall(request).execute()) {
            String resp = response.body().string();
            Log.d("RESPONSE",resp) ;
            return resp ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }
}