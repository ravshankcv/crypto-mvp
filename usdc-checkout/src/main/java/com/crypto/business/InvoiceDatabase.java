package com.crypto.business;

import java.util.*;

public class InvoiceDatabase {
    public static List<Invoice> getPendingInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        if (invoices.isEmpty()) {
            invoices.add(new Invoice("INV-101", "0xReceiverWallet1", "100", System.currentTimeMillis() - 10000));
            invoices.add(new Invoice("INV-102", "0xB428b0eef777a19dD27f8D5e640Ba0e1D865bca1", "1000000000000000000",
                    System.currentTimeMillis() - 20000));
        }
        return invoices;
    }
}
// This class simulates a database of invoices. In a real application, this
// would connect to a database.