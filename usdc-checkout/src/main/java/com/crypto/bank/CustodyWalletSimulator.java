package com.crypto.bank;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustodyWalletSimulator {
    private static final Map<String, String[]> custodyKeys = new HashMap<>();

    public static void registerWallet(String walletId) {
        String part1 = UUID.randomUUID().toString();
        String part2 = UUID.randomUUID().toString();
        custodyKeys.put(walletId, new String[] { part1, part2 });
    }

    public static boolean verifyAccess(String walletId, String part1, String part2) {
        String[] stored = custodyKeys.get(walletId);
        return stored != null && stored[0].equals(part1) && stored[1].equals(part2);
    }
}
