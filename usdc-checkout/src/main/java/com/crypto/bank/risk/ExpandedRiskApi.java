package com.crypto.bank.risk;

import static spark.Spark.*;

import com.crypto.bank.risk.ExpandedWalletRiskScorer;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ExpandedRiskApi {
    public static void main(String[] args) {
        port(4572);
        staticFileLocation("/public");

        Gson gson = new Gson();

        post("/risk/expanded", (req, res) -> {
            Map<String, Object> metrics = gson.fromJson(req.body(), Map.class);

            ExpandedWalletRiskScorer.RiskReport report = ExpandedWalletRiskScorer.scoreWallet(
                    (String) metrics.getOrDefault("wallet", "unknown"),
                    metrics);

            res.type("application/json");
            return gson.toJson(report);
        });
    }
}
