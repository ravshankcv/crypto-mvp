package com.crypto.business;

public class WebhookEvent {
    public String type;
    public String from;
    public String to;
    public double value;
    public String txId;
    public String status;
    public long timestamp;

    public WebhookEvent(String type, String from, String to, double value, String txId, String status, long timestamp) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.value = value;
        this.txId = txId;
        this.status = status;
        this.timestamp = timestamp;

    }
}
