package com.crypto.bank.scoring;

import java.util.*;

public class WalletRiskScorer {

    private static final Set<String> knownBadActors = Set.of("0xabc123...", "0xdef456...");
    private static final Map<String, Integer> transferFrequency = new HashMap<>();

    public static Map<String, Object> calculateScore(String wallet) {
        double baseScore = 0.0;
        List<String> reasons = new ArrayList<>();

        if (knownBadActors.contains(wallet.toLowerCase())) {
            baseScore += 0.6;
            reasons.add("Known malicious wallet");
        }

        int freq = transferFrequency.getOrDefault(wallet, 0);
        if (freq > 10) {
            baseScore += 0.3;
            reasons.add("High transaction volume");
        }

        // Placeholder for future AI model integration
        if (wallet.startsWith("0x9")) {
            baseScore += 0.1;
            reasons.add("Heuristic: Unusual prefix");
        }

        double cappedScore = Math.min(1.0, baseScore);

        Map<String, Object> result = new HashMap<>();
        result.put("wallet", wallet);
        result.put("score", cappedScore);
        result.put("reasons", reasons);

        return result;
    }

    public static void registerTransfer(String wallet) {
        transferFrequency.put(wallet, transferFrequency.getOrDefault(wallet, 0) + 1);
    }
}
