package com.hugo.mysimplefirebaseproject.certs;

import okhttp3.OkHttpClient;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;

public class OkHttpClientProvider {

    public static OkHttpClient getSecureClient() throws Exception {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);

        X509TrustManager defaultTrustManager = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];
        CustomAIATrustManager customTrustManager = new CustomAIATrustManager(defaultTrustManager);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new javax.net.ssl.TrustManager[]{customTrustManager}, new java.security.SecureRandom());

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), customTrustManager)
                .hostnameVerifier((hostname, session) -> true) // For testing purposes only!
                .build();
    }
}

