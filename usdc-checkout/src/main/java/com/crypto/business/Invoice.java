package com.crypto.business;

public class Invoice {
    public String invoiceId;
    public String walletAddress;
    public String amount;
    public long timestamp;

    public Invoice(String invoiceId, String walletAddress, String amount, long timestamp) {
        this.invoiceId = invoiceId;
        this.walletAddress = walletAddress;
        this.amount = amount;
        this.timestamp = timestamp;
    }
}
