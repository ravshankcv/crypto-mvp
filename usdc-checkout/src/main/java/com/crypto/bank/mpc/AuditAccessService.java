package com.crypto.bank.mpc;

import java.util.*;
import com.crypto.bank.mpc.CustodyWalletService;
import com.crypto.bank.mpc.WalletAnnotationService;
import com.crypto.bank.scoring.WalletRiskScorer;
import com.google.gson.Gson;

public class AuditAccessService {

    private static final String VALID_TOKEN = "audit-token-123";

    public static boolean isValidToken(String token) {
        return VALID_TOKEN.equals(token);
    }

    public static String getAuditReport() {
        CustodyWalletService custodyWalletService = new CustodyWalletService();
        List<String> wallets = custodyWalletService.getAllWallets();
        List<Map<String, Object>> results = new ArrayList<>();

        for (String wallet : wallets) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("wallet", wallet);
            entry.put("score", WalletRiskScorer.calculateScore(wallet));
            entry.put("active", CustodyWalletService.isActive(wallet));
            entry.put("annotations", WalletAnnotationService.getAnnotations(wallet));
            results.add(entry);
        }

        return new Gson().toJson(results);
    }

    public static String getAuditCSVReport() {
        CustodyWalletService custodyWalletService = new CustodyWalletService();
        List<String> wallets = custodyWalletService.getAllWallets();
        StringBuilder sb = new StringBuilder("Wallet,Score,Status,Annotations\n");

        for (String wallet : wallets) {

            Map<String, Object> data = new HashMap<>();
            data.put("wallet", wallet);
            data.put("score", WalletRiskScorer.calculateScore(wallet));
            String status = CustodyWalletService.isActive(wallet) ? "Active" : "Idle";
            String annotations = String.join(";", WalletAnnotationService.getAnnotations(wallet));
            sb.append(String.format("%s,%d,%s,%s\n", data.get("wallet"), data.get("score"), status, annotations));
        }

        return sb.toString();
    }

}
