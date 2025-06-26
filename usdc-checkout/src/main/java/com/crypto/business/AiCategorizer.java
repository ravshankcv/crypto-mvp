package com.crypto.business;

public class AiCategorizer {
    public static String predictCategory(String description, double amount) {
        if (description.toLowerCase().contains("cloud") || amount > 500) {
            return "Software Services";
        } else if (description.toLowerCase().contains("coffee") || amount < 10) {
            return "Meals & Entertainment";
        } else {
            return "General Business Expense";
        }
    }
}