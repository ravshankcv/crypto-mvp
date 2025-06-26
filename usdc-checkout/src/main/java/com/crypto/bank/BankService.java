package com.crypto.bank;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crypto.bank.mpc.AuditAccessService;
import com.crypto.bank.mpc.CustodyWalletService;
import com.crypto.bank.mpc.WalletActivityLog;
import com.crypto.bank.mpc.WalletAnnotationService;
import com.crypto.bank.scoring.WalletRiskScorer;
import com.google.gson.Gson;
import com.crypto.bank.monitor.LiveWalletMonitor;
import com.crypto.bank.monitor.USDCMonitor;

import org.web3j.protocol.core.methods.response.Log;

public class BankService {

    private static final CustodyWalletService walletService = new CustodyWalletService();
    public static final List<Map<String, String>> liveFeed = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        port(4571);
        staticFileLocation("/public");

        USDCMonitor monitor = new USDCMonitor();
        monitor.start(wallet -> {
            // Refresh score or mark wallet as active
            CustodyWalletService.markActive(wallet);
        });

        get("/register/:walletId", (req, res) -> {
            CustodyWalletSimulator.registerWallet(req.params(":walletId"));
            return "Custody wallet registered";
        });

        get("/verify/:walletId/:part1/:part2", (req, res) -> {
            boolean ok = CustodyWalletSimulator.verifyAccess(
                    req.params(":walletId"), req.params(":part1"), req.params(":part2"));
            return ok ? "Access Verified" : "Access Denied";
        });

        get("/risk/:wallet/:count/:amount", (req, res) -> {
            String score = EnhancedWalletRiskScorer.score(
                    req.params(":wallet"),
                    Integer.parseInt(req.params(":count")),
                    Double.parseDouble(req.params(":amount")));
            return "Wallet Risk: " + score;
        });

        get("/api/wallet-risks", (req, res) -> {
            List<String> wallets = walletService.getAllWallets(); // Stub or simulated wallet list
            List<Map<String, Object>> risks = new ArrayList<>();

            for (String wallet : wallets) {
                Map<String, Object> data = new HashMap<>();
                data.put("wallet", wallet);
                data.put("score", WalletRiskScorer.calculateScore(wallet));
                risks.add(data);
            }

            res.type("application/json");
            return new com.google.gson.Gson().toJson(risks);
        });

        get("/api/wallet-check", (req, res) -> {
            List<String> wallets = walletService.getAllWallets();
            List<Map<String, Object>> results = new ArrayList<>();

            for (String wallet : wallets) {
                Map<String, Object> riskInfo = WalletRiskScorer.calculateScore(wallet);

                Map<String, Object> entry = new HashMap<>();
                entry.put("wallet", wallet);
                entry.put("score", riskInfo.get("score"));
                entry.put("reasons", riskInfo.get("reasons"));
                entry.put("history", WalletActivityLog.getHistory(wallet));
                entry.put("active", CustodyWalletService.isActive(wallet));

                results.add(entry);
            }

            res.type("application/json");
            return new com.google.gson.Gson().toJson(results);
        });

        LiveWalletMonitor.load();

        // Start live USDC transfer monitoring
        LiveWalletMonitor.start((Log log) -> {
        });

        post("/api/tag-wallet", (req, res) -> {
            String wallet = req.queryParams("wallet");
            String tag = req.queryParams("tag");

            WalletAnnotationService.addAnnotation(wallet, tag);
            return "Tagged " + wallet + " with '" + tag + "'";
        });

        get("/api/wallet-annotations", (req, res) -> {
            res.type("application/json");
            return WalletAnnotationService.getAllAnnotationsJson();
        });

        get("/api/audit/wallets", (req, res) -> {
            String authHeader = req.headers("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                res.status(401);
                return "Unauthorized - Missing token";
            }

            String token = authHeader.substring("Bearer ".length());
            if (!AuditAccessService.isValidToken(token)) {
                res.status(403);
                return "Forbidden - Invalid token";
            }

            res.type("application/json");
            return AuditAccessService.getAuditReport();
        });

        get("/api/audit/wallets/csv", (req, res) -> {
            String authHeader = req.headers("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                res.status(401);
                return "Unauthorized";
            }
            String token = authHeader.substring("Bearer ".length());
            if (!AuditAccessService.isValidToken(token)) {
                res.status(403);
                return "Forbidden";
            }

            res.type("text/csv");
            res.header("Content-Disposition", "attachment; filename=audit-report.csv");
            return AuditAccessService.getAuditCSVReport();
        });

    }
}
