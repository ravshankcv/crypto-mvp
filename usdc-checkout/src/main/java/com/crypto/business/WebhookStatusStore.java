package com.crypto.business;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebhookStatusStore {
    private static final List<WebhookEvent> events = new CopyOnWriteArrayList<>();

    public static void addEvent(WebhookEvent event) {
        events.add(event);
    }

    public static List<WebhookEvent> getEvents() {
        return events;
    }
}
