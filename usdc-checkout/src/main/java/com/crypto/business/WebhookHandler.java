package com.crypto.business;

import static spark.Spark.*;

public class WebhookHandler {
    public static void main(String[] args) {
        port(4570);
        staticFileLocation("/public");
        post("/webhook", (req, res) -> {
            String payload = req.body();
            String signature = req.headers("X-Signature");
            String secret = "your-secret";

            if (HmacVerifier.verify(payload, signature, secret)) {
                String vendor = extract(payload, "vendor");
                double amount = Double.parseDouble(extract(payload, "amount"));

                String category = AiCategorizer.predictCategory(vendor, amount);
                QuickBooksSimulator.syncToQuickBooks(category, vendor, amount);
                return "✅ Webhook processed and synced.";
            } else {
                res.status(401);
                return "❌ Invalid signature";
            }
        });
    }

    private static String extract(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1)
            return null;

        start += search.length();
        // Skip whitespace
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        // Determine if it's a quoted string
        if (json.charAt(start) == '"') {
            int end = json.indexOf('"', start + 1);
            if (end == -1)
                return null;
            return json.substring(start + 1, end);
        }

        // Else assume it's a number or boolean
        int end = start;
        while (end < json.length() &&
                (Character.isLetterOrDigit(json.charAt(end)) || json.charAt(end) == '.')) {
            end++;
        }
        return json.substring(start, end).trim();
    }
}