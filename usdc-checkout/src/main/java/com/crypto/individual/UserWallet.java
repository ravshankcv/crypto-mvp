package com.crypto.individual;

import static spark.Spark.*;

import com.crypto.individual.TransactionHistoryService.Transaction;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserWallet {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        port(4580);
        staticFiles.location("/public");
        Gson gson = new Gson();

        /*
         * options("/*", (request, response) -> {
         * String accessControlRequestHeaders =
         * request.headers("Access-Control-Request-Headers");
         * if (accessControlRequestHeaders != null) {
         * response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
         * }
         * 
         * String accessControlRequestMethod =
         * request.headers("Access-Control-Request-Method");
         * if (accessControlRequestMethod != null) {
         * response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
         * }
         * 
         * return "OK";
         * });
         * 
         * before((request, response) -> {
         * response.header("Access-Control-Allow-Origin", "*");
         * response.header("Access-Control-Request-Method", "*");
         * response.header("Access-Control-Allow-Headers", "*");
         * response.type("application/json");
         * });
         */

        get("/security-alerts/:walletId", (req, res) -> {
            String walletId = req.params(":walletId");
            List<String> alerts = new ArrayList<>();

            // Simulated behavior checks
            if (walletId.endsWith("99"))
                alerts.add("Unusual login location detected.");
            if (walletId.startsWith("0xabc"))
                alerts.add("Large USDC transfer to new address.");

            res.type("application/json");
            return new Gson().toJson(alerts);
        });

        get("/explain-tx/:txHash", (req, res) -> {
            String txHash = req.params(":txHash");

            // Simulate explanation
            String explanation = switch (txHash) {
                case "0xtx1" -> "You paid $25 to Starbucks.";
                case "0xtx2" -> "Received 50 USDC from Coinbase.";
                default -> "Standard ERC20 transfer.";
            };

            res.type("application/json");
            return new Gson().toJson(Map.of("txHash", txHash, "explanation", explanation));
        });

        get("/wallet-summary/:walletId", (req, res) -> {
            String walletId = req.params(":walletId");

            Map<String, Object> summary = Map.of(
                    "totalReceived", "500 USDC",
                    "totalSent", "200 USDC",
                    "topSpendingCategory", "Dining",
                    "numTransactions", 12);

            res.type("application/json");
            return new Gson().toJson(summary);
        });

        TransactionExplainService.initRoutes();
        TransactionHistoryService.initRoutes();
        BehaviorAnalyzerService.initRoutes();

    }
}
