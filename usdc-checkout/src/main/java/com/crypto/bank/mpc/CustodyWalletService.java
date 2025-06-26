package com.crypto.bank.mpc;

import static spark.Spark.*;

import java.math.BigInteger;
import java.util.*;

public class CustodyWalletService {
    private static final BigInteger PRIME = new BigInteger("104729"); // A large prime
    private static final int THRESHOLD = 3;
    private static final int PARTICIPANTS = 5;

    private static BigInteger privateKey;
    private static Map<Integer, BigInteger> shares;
    private static SimulatedHSM hsm;

    public static void main(String[] args) {
        port(4569);
        staticFileLocation("/public");

        privateKey = new BigInteger("12345"); // Mock private key
        shares = SecretShare.splitSecret(privateKey, PARTICIPANTS, THRESHOLD, PRIME);
        hsm = new SimulatedHSM(privateKey);

        get("/shares", (req, res) -> shares.toString());

        post("/sign", (req, res) -> {
            // Accept 3 shares to reconstruct the key
            Map<Integer, BigInteger> partials = new HashMap<>();
            for (int i = 1; i <= THRESHOLD; i++) {
                partials.put(i, shares.get(i));
            }

            BigInteger reconstructed = SecretShare.reconstructSecret(partials, PRIME);
            String signed = hsm.signTransaction("Send 100 USDC", reconstructed);
            return "Transaction Result: " + signed;
        });
    }

    // Simulated list of monitored wallets
    public List<String> getAllWallets() {
        return Arrays.asList(
                "0xabc1234567890fedcba1234567890abcdef0001",
                "0xb71f1f585c70f23503cb617ba525ef7947deecd1",
                "0xdeadbeefdeadbeefdeadbeefdeadbeefdeadbeef",
                "0x1234567890abcdef1234567890abcdef1234fff");
    }

    /*
     * private static List<String> wallets = new ArrayList<>(List.of(
     * "0xabc123...", "0xdef456...", "0xghi789..."));
     * 
     * public List<String> getAllWallets() {
     * return wallets;
     * }
     */

    private static Set<String> activeWallets = new HashSet<>();

    public static void markActive(String wallet) {
        activeWallets.add(wallet);
        System.out.println("Marked wallet as active: " + wallet);
    }

    public static boolean isActive(String wallet) {
        System.out.println("Checking if wallet is active: " + wallet);
        return activeWallets.contains(wallet);
    }
}
