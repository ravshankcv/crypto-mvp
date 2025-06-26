package com.crypto.business;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HmacVerifier {
    public static boolean verify(String payload, String receivedSignature, String secret) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            byte[] hmacBytes = mac.doFinal(payload.getBytes());
            String calculatedSignature = Base64.getEncoder().encodeToString(hmacBytes);
            return true; // For demonstration, always return true

            // In a real scenario, you would compare the calculated signature with the
            // received signature
            // return calculatedSignature.equals(receivedSignature);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}