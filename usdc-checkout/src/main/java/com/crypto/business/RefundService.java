package com.crypto.business;

import static spark.Spark.*;
import com.google.gson.Gson;

import java.util.*;

public class RefundService {

    private static final Map<String, String> refundStatus = new LinkedHashMap<>();

    public static void registerRoutes() {
        post("/api/refund", (req, res) -> {
            String invoiceId = req.queryParams("invoiceId");
            if (invoiceId == null || invoiceId.isEmpty()) {
                res.status(400);
                return "Missing invoiceId";
            }

            refundStatus.put(invoiceId, "Initiated");
            // Simulate processing
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    refundStatus.put(invoiceId, "Completed");
                }
            }, 3000);

            return "Refund initiated for invoice " + invoiceId;
        });

        get("/api/refund-status", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(refundStatus);
        });
    }
}
