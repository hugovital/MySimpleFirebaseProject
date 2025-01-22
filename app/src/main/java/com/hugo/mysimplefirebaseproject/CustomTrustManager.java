package com.hugo.mysimplefirebaseproject;

import okhttp3.*;
import javax.net.ssl.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

public class CustomTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
        // No-op
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {

        String localCertFilePath = "/data/data/com.hugo.mysimplefirebaseproject/files/cert_file";

        log("Server certificates: " + chain.length);

        for(int i = 0 ; i < chain.length; i++){
            X509Certificate cert = chain[i];
            log("Inside array: " + cert.toString());
            String path = localCertFilePath + "_" + i;
            saveCertificateToFile(cert, path);
            saveCertificateAsCer(cert, path);
        }

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }

    public static void runSSLCall(String url) {

        try {

            TrustManager[] trustManagers = new TrustManager[]{new CustomTrustManager()};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new java.security.SecureRandom());

            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0])
                    .hostnameVerifier((hostname, session) -> true) // Ignore hostname verification
                    .build();

            //ALB-Externo-NF2-MobPF-AzA-HTTP2-1377044082.sa-east-1.elb.amazonaws.com:9443
            Request request = new Request.Builder()
                    //.url("https://incomplete-chain.badssl.com/")
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                log("Response code: " + response.code());
            } catch (IOException e) {
                log("Request failed: " + e.getMessage());
            }

        } catch (Exception ex){
            ex.printStackTrace();
            log("ERROR: " + ex.getMessage());
        }
    }

    public static void log(String msg){
        System.out.println("CAWABANGA: " + msg);
    }

    public static void saveCertificateToFile(X509Certificate certificate, String filePath) {

        filePath += ".pem";

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8)) {
            // Convert the certificate to Base64 encoded string
            String encodedCertificate = Base64.getEncoder().encodeToString(certificate.getEncoded());

            // Write PEM formatted certificate to file
            writer.write("-----BEGIN CERTIFICATE-----\n");
            writer.write(encodedCertificate.replaceAll("(.{64})", "$1\n")); // Break lines every 64 chars
            writer.write("\n-----END CERTIFICATE-----\n");

            System.out.println("Certificate saved successfully to: " + filePath);
        } catch (CertificateEncodingException e) {
            System.err.println("Error encoding certificate: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error writing certificate to file: " + e.getMessage());
        }
    }

    public static void saveCertificateAsCer(X509Certificate certificate, String filePath) {

        filePath += ".cer";

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            // Write the certificate in DER-encoded binary format
            fos.write(certificate.getEncoded());
            System.out.println("Certificate saved successfully to: " + filePath);
        } catch (CertificateEncodingException e) {
            System.err.println("Error encoding certificate: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error writing certificate to file: " + e.getMessage());
        }
    }

}

