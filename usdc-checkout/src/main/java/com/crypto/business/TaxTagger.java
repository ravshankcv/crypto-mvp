package com.crypto.business;

public class TaxTagger {
    public static boolean isTaxDeductible(String category) {
        switch (category) {
            case "Travel":
            case "Cloud Services":
            case "Office Supplies":
                return true;
            default:
                return false;
        }
    }

    public static String tag(String description) {
        // Simple mock logic for tax tagging
        if (description.toLowerCase().contains("meal")) {
            return "Meal Deduction";
        } else if (description.toLowerCase().contains("software")) {
            return "Software Expense";
        } else if (description.toLowerCase().contains("travel")) {
            return "Travel Deduction";
        } else {
            return "General Expense";
        }
    }
}
