package com.hugo.mysimplefirebaseproject.certs;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomAIATrustManager implements X509TrustManager {

    private X509TrustManager defaultTrustManager = null;

    public CustomAIATrustManager(X509TrustManager defaultTrustManager) {
        this.defaultTrustManager = defaultTrustManager;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        defaultTrustManager.checkClientTrusted(chain, authType);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        try {
            defaultTrustManager.checkServerTrusted(chain, authType);
        } catch (CertificateException e) {
            System.out.println("Certificate chain is incomplete. Trying AIA fetch...");

            List<X509Certificate> certList = new ArrayList<>(Arrays.asList(chain));

            try {
                X509Certificate intermediateCert = AIAFetcher.fetchIntermediateCertificate(chain[0]);
                if (intermediateCert != null) {
                    certList.add(intermediateCert);
                }
            } catch (Exception aiaException) {
                throw new CertificateException("CAWABANGA: Failed to fetch intermediate certificate", aiaException);
            }

            // Retry trust validation with the new chain
            defaultTrustManager.checkServerTrusted(certList.toArray(new X509Certificate[0]), authType);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return defaultTrustManager.getAcceptedIssuers();
    }
}
