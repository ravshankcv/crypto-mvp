package com.crypto.business;

import static spark.Spark.*;
import com.google.gson.Gson;

import java.util.*;

public class QuickBooksSyncStatusService {

    private static final Map<String, String> syncStatus = new HashMap<>();

    static {
        // Simulated status entries
        syncStatus.put("INV-001", "Synced");
        syncStatus.put("INV-002", "Pending");
        syncStatus.put("INV-003", "Failed");
    }

    public static void registerRoutes() {
        get("/api/quickbooks/status", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(syncStatus);
        });
    }

    // Optionally: simulate updating sync status over time
    public static void updateStatus(String invoiceId, String status) {
        syncStatus.put(invoiceId, status);
    }
}
