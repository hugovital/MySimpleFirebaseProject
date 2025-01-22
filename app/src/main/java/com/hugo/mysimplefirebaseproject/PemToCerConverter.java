package com.hugo.mysimplefirebaseproject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.stream.Collectors;

public class PemToCerConverter {

    public static void convertPemToCer(String pemFilePath, String cerFilePath) {
        try {
            // Read the PEM file content
            String pemContent = Files.lines(Paths.get(pemFilePath), StandardCharsets.UTF_8)
                    .filter(line -> !line.startsWith("-----BEGIN CERTIFICATE-----") &&
                            !line.startsWith("-----END CERTIFICATE-----"))
                    .collect(Collectors.joining());

            // Decode Base64 PEM content to DER
            byte[] derBytes = Base64.getDecoder().decode(pemContent);

            // Write the DER bytes to a .cer file
            try (FileOutputStream fos = new FileOutputStream(cerFilePath)) {
                fos.write(derBytes);
                System.out.println("Certificate successfully converted to: " + cerFilePath);
            }
        } catch (IOException e) {
            System.err.println("Error reading or writing files: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid PEM file format: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Example usage
        convertPemToCer("certificate.pem", "certificate.cer");
    }
}

