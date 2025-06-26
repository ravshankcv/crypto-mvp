package com.crypto.bank;

import java.util.Set;

public class EnhancedWalletRiskScorer {
    private static final Set<String> blacklisted = Set.of("0xBadWallet123", "0xScamWalletABC");

    public static String score(String walletAddress, int txCount, double avgAmount) {
        if (blacklisted.contains(walletAddress))
            return "High Risk";

        if (txCount > 100 || avgAmount > 10_000)
            return "Medium Risk";

        return "Low Risk";
    }
}
