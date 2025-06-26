package com.crypto.bank;
// This class is a simple API security filter that checks for a static token.

// It can be extended to use HMAC or other security measures in the future.

public class ApiSecurityFilter {
    private static final String SECRET = "supersecret";

    public static boolean validate(String token) {
        return SECRET.equals(token); // Can be extended to HMAC later
    }
}
