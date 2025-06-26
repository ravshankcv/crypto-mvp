package com.crypto.business;

import static spark.Spark.*;

import com.google.gson.Gson;

public class WebhookListener {
    public static void main(String[] args) {
        port(8080);
        staticFiles.location("/public"); // loads dashboard.html

        post("/webhook", (req, res) -> {
            Gson gson = new Gson();
            WebhookEvent event = gson.fromJson(req.body(), WebhookEvent.class);
            WebhookStatusStore.addEvent(event);
            return "Received";
        });

        get("/webhook-status", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(WebhookStatusStore.getEvents());
        });
    }
}
