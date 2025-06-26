package com.crypto.individual;

import static spark.Spark.*;

import java.util.*;
import com.google.gson.Gson;

public class TransactionExplainService {

    static class Explanation {
        String transactionId;
        String explanation;

        Explanation(String transactionId, String explanation) {
            this.transactionId = transactionId;
            this.explanation = explanation;
        }
    }

    private static final Map<String, String> explanations = new HashMap<>();

    public static void initRoutes() {
        // Preloaded explanations
        explanations.put("txn001", "Amazon purchase part of a recurring subscription.");
        explanations.put("txn002", "Starbucks transaction identified as daily coffee habit.");
        explanations.put("txn003", "This is your usual monthly electricity payment.");

        get("/api/explain/:id", (req, res) -> {
            String id = req.params(":id");
            String explanation = explanations.getOrDefault(id, "No explanation available.");
            res.type("application/json");
            return new Gson().toJson(new Explanation(id, explanation));
        });
    }
}
