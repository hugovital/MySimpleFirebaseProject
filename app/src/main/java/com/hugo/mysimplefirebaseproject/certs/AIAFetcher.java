package com.hugo.mysimplefirebaseproject.certs;

import java.io.InputStream;
import java.net.URL;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class AIAFetcher {

    public static X509Certificate fetchIntermediateCertificate(X509Certificate cert) throws Exception {
        // Get the AIA extension value
        byte[] aiaBytes = cert.getExtensionValue("1.3.6.1.5.5.7.1.1"); // OID for AIA

        if (aiaBytes == null) {
            throw new Exception("No AIA extension found in certificate");
        }

        // Convert the byte array to string and parse to find the URL
        String aiaString = new String(aiaBytes);
        String caIssuersUrl = extractHttpUrlFromAIA(aiaString);

        if (caIssuersUrl == null) {
            throw new Exception("No CA Issuer URL found in AIA extension");
        }

        System.out.println("Fetching intermediate certificate from: " + caIssuersUrl);
        return downloadCertificate(caIssuersUrl);
    }

    private static String extractHttpUrlFromAIA(String aiaString) {

        String[] partes = aiaString.split("http");
        String cert = partes[1].substring( 0, partes[1].indexOf("crt") );
        cert = "https" + cert + "crt";

        System.out.println("CAWABANGA: Certificado Encontrado: " + cert);

        /*
        try {
            LdapName ldapName = new LdapName(aiaString);
            for (Rdn rdn : ldapName.getRdns()) {
                String value = (String) rdn.getValue();
                if (value.startsWith("http")) {
                    return value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         */
        return cert;
    }

    private static X509Certificate downloadCertificate(String certUrl) throws Exception {
        try (InputStream in = new URL(certUrl).openStream()) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(in);
        }
    }
}

