package com.noah.npardon.net;

import android.os.AsyncTask;
import android.util.Log;

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
import okhttp3.Response;


public class WSConnexionHTTPS extends AsyncTask<String,Integer,String> {
    private final String base_url = "https://sio.jbdelasalle.com/~amedassi/soirees/ws.php?";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static OkHttpClient client = null;

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
        Request request = new Request.Builder()
                .url(base_url + strings[0])
                .build();
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