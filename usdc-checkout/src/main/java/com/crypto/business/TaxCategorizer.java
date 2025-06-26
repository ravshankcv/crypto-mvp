package com.crypto.business;

public class TaxCategorizer {
    public static String categorize(String description) {
        if (description.toLowerCase().contains("consulting"))
            return "Professional Services";
        if (description.toLowerCase().contains("hosting"))
            return "Cloud Expenses";
        return "Uncategorized";
    }
}
