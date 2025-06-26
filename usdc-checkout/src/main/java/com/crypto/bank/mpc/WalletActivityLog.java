package com.crypto.bank.mpc;

import java.util.*;

public class WalletActivityLog {
    private static final Map<String, List<String>> history = new HashMap<>();

    static {
        history.put("0xb71f1f585c70f23503cb617ba525ef7947deecd1", Arrays.asList(
                "2025-06-12: Transfer 100 USDC to 0xReceiver",
                "2025-06-13: Received 50 USDC from 0xSender"));
        history.put("0xWallet2", Arrays.asList(
                "2025-06-10: No activity",
                "2025-06-13: Login attempt from unknown IP"));
    }

    public static List<String> getHistory(String wallet) {
        return history.getOrDefault(wallet, Arrays.asList("No records found."));
    }
}
