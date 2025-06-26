package com.crypto.individual;

import static spark.Spark.*;
import java.util.*;
import com.google.gson.Gson;

public class BehaviorAnalyzerService {

    private static final List<Map<String, String>> alertLog = new ArrayList<>();

    public static void initRoutes() {
        get("/api/behavior-analyzer", (req, res) -> {
            int historicalAvgTxPerDay = 2;
            Set<String> knownMerchants = new HashSet<>(Arrays.asList("Amazon", "Starbucks", "Uber"));
            double historicalAvgAmount = 30.0;

            List<Map<String, Object>> recentTx = Arrays.asList(
                    Map.of("merchant", "Amazon", "amount", 25.0),
                    Map.of("merchant", "Electric Co", "amount", 89.0),
                    Map.of("merchant", "Tesla", "amount", 210.0));

            int txPerDay = recentTx.size();
            boolean hasNewMerchant = false;
            boolean hasHighValue = false;

            for (Map<String, Object> tx : recentTx) {
                String merchant = (String) tx.get("merchant");
                Double amount = (Double) tx.get("amount");

                if (!knownMerchants.contains(merchant))
                    hasNewMerchant = true;
                if (amount > 3 * historicalAvgAmount)
                    hasHighValue = true;
            }

            String riskLevel = "Low";
            List<String> messages = new ArrayList<>();

            if (txPerDay > historicalAvgTxPerDay * 2) {
                riskLevel = "Medium";
                messages.add("Unusually high transaction frequency.");
            }
            if (hasNewMerchant) {
                riskLevel = "Medium";
                messages.add("New merchant(s) detected.");
            }
            if (hasHighValue) {
                riskLevel = "High";
                messages.add("High value transaction anomaly.");
            }

            Map<String, String> logEntry = new HashMap<>();
            logEntry.put("timestamp", new Date().toString());
            logEntry.put("risk", riskLevel);
            logEntry.put("summary", String.join("; ", messages));
            alertLog.add(logEntry);

            res.type("application/json");
            return new Gson().toJson(Map.of("riskLevel", riskLevel, "messages", messages));
        });

        get("/api/behavior-alerts-log", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(alertLog);
        });
    }
}
