package com.crypto.business;

import static spark.Spark.*;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.awt.image.BufferedImage;
import java.util.Base64;
import javax.imageio.ImageIO;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class BusinessService {
    public static void main(String[] args) {
        port(4567);
        staticFileLocation("/public");

        // QR Code route
        get("/generate-qr/:amount", (req, res) -> {
            String amount = req.params(":amount");
            String data = "pay:usdc:" + amount;

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(data, BarcodeFormat.QR_CODE, 200, 200);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());

            res.type("text/html");
            return "<img src='data:image/png;base64," + base64 + "'/>";
        });

        // Transaction Analysis Endpoint
        post("/analyze", (req, res) -> {
            String txData = req.body();
            String category = categorizeTransaction(txData);
            return "{ \"category\": \"" + category + "\" }";
        });

        // Fiat Conversion Endpoint (Manual JSON parsing)
        post("/convert", (req, res) -> {
            String json = req.body();
            FiatRequest fiatRequest = parseFiatRequest(json);

            // Dummy conversion rate
            double rate = 1.07;
            double converted = Double.parseDouble(fiatRequest.amount) * rate;

            return "{ \"convertedAmount\": \"" + converted + "\", \"currency\": \"EUR\" }";
        });

        AnalyticsService.registerRoutes();
    }

    // Helper Classes
    static class Transaction {
        private String description;
        private String tag;

        public String getDescription() {
            return description;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
    }

    private static String categorizeTransaction(String txData) {
        if (txData.contains("coffee") || txData.contains("starbucks")) {
            return "Food & Beverage";
        } else if (txData.contains("uber") || txData.contains("lyft")) {
            return "Transport";
        } else {
            return "Miscellaneous";
        }
    }

    // Minimal JSON parsing without libraries
    public static FiatRequest parseFiatRequest(String json) {
        String cleaned = json.replaceAll("[{}\"]", ""); // remove curly braces and quotes
        String[] pairs = cleaned.split(",");

        String amount = "";
        String currency = "";

        for (String pair : pairs) {
            String[] kv = pair.split(":");
            if (kv.length == 2) {
                String key = kv[0].trim();
                String value = kv[1].trim();

                if (key.equals("amount")) {
                    amount = value;
                } else if (key.equals("currency")) {
                    currency = value;
                }
            }
        }

        return new FiatRequest(amount, currency);
    }

    // Helper class for fiat conversion requests
    public static class FiatRequest {
        public String amount;
        public String currency;

        public FiatRequest(String amount, String currency) {
            this.amount = amount;
            this.currency = currency;
        }
    }
}
