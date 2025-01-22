package com.hugo.mysimplefirebaseproject;

import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;

public class OkHttpCertificateFetcher {

    public static void runSSLCall() {
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory(), (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)  // Ignore hostname verification for testing
                .eventListener(new EventListener() {
                    @Override
                    public void secureConnectEnd(Call call, Handshake handshake) {
                        List<Certificate> certificates = handshake.peerCertificates();
                        System.out.println("CAWABANGA: Handshake successful. Certificates: " + certificates.size());
                        for (Certificate cert : handshake.peerCertificates()) {
                            System.out.println("CAWABANGA: " + cert.toString());
                        }
                    }

                    @Override
                    public void callFailed(Call call, IOException ioe) {
                        if (ioe instanceof SSLHandshakeException) {
                            System.out.println("SSL Handshake failed. Trying to retrieve certificates...");
                        }
                        System.out.println("Request failed: " + ioe.getMessage());
                    }
                })
                .build();

        Request request = new Request.Builder()
                .url("https://incomplete-chain.badssl.com/")
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println("Response: " + response.code());
        } catch (IOException e) {
            System.out.println("Request failed: " + e.getMessage());
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SSLSocketFactory", e);
        }
    }

    private static final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    System.out.println("Captured server certificates:");
                    for (X509Certificate cert : chain) {
                        System.out.println(cert.toString());
                    }
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
    };
}

