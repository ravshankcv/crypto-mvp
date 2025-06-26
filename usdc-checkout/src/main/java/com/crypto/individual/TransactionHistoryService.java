package com.crypto.individual;

import static spark.Spark.*;

import java.util.*;
import com.google.gson.Gson;

public class TransactionHistoryService {

    static class Transaction {
        String id;
        String date;
        String amount;
        String merchant;
        String category;

        Transaction(String id, String date, String amount, String merchant, String category) {
            this.id = id;
            this.date = date;
            this.amount = amount;
            this.merchant = merchant;
            this.category = category;
        }
    }

    public static void initRoutes() {
        get("/api/transactions", (req, res) -> {
            List<Transaction> transactions = Arrays.asList(
                    new Transaction("txn001", "2025-06-17", "$12.49", "Amazon", "Shopping"),
                    new Transaction("txn002", "2025-06-16", "$4.95", "Starbucks", "Food"),
                    new Transaction("txn003", "2025-06-15", "$89.00", "Electric Co", "Utilities"));
            res.type("application/json");
            return new Gson().toJson(transactions);
        });
    }
}
