package com.fishbuddy.servicesparsing;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by shrythi on 3/2/2018.
 */

public class CustomHttpClient extends DefaultHttpClient {

    public CustomHttpClient() {
        super();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier(new CustomHostnameVerifier());
        Scheme scheme = (new Scheme("https", socketFactory, 443));
        getConnectionManager().getSchemeRegistry().register(scheme);


     /*   HttpsURLConnection.setDefaultHostnameVerifier(new CustomHostnameVerifier());
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //context.init(null, new X509TrustManager[]{new NullX509TrustManager()}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());*/

    }


}