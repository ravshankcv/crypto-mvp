package com.crypto.business;

import static spark.Spark.*;
import com.google.gson.Gson;

import java.util.*;

public class AnalyticsService {

    private static List<Map<String, Object>> anomalyTransactions = new ArrayList<>();
    private static List<Map<String, Object>> matchedInvoices = new ArrayList<>();

    static {
        // Sample Anomalies
        anomalyTransactions.add(Map.of("txHash", "0xabc123", "amount", 1500, "reason", "Unusual volume"));
        anomalyTransactions.add(Map.of("txHash", "0xdef456", "amount", 2000, "reason", "Time of day"));

        // Sample Matches
        matchedInvoices.add(Map.of("invoiceId", "INV-001", "txHash", "0xaaa111", "amount", 1000));
        matchedInvoices.add(Map.of("invoiceId", "INV-002", "txHash", "0xbbb222", "amount", 750));
    }

    public static void registerRoutes() {
        get("/api/analytics", (req, res) -> {
            Map<String, Object> summary = new HashMap<>();
            summary.put("conversionRate", 1.00);
            summary.put("anomaliesDetected", anomalyTransactions.size());
            summary.put("invoiceMatchPercent", 86.7);

            res.type("application/json");
            return new Gson().toJson(summary);
        });

        get("/api/analytics/anomalies", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(anomalyTransactions);
        });

        get("/api/analytics/matches", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(matchedInvoices);
        });
    }
}
