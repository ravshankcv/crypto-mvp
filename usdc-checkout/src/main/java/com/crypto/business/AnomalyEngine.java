package com.crypto.business;

import java.util.ArrayList;
import java.util.List;

public class AnomalyEngine {
    private static final List<Double> transactionAmounts = new ArrayList<>();

    public static synchronized boolean isAnomalous(double newAmount) {
        transactionAmounts.add(newAmount);

        double mean = transactionAmounts.stream().mapToDouble(a -> a).average().orElse(0.0);
        double stddev = Math.sqrt(transactionAmounts.stream()
                .mapToDouble(a -> Math.pow(a - mean, 2))
                .average().orElse(0.0));

        return stddev > 0 && Math.abs(newAmount - mean) > 2 * stddev;
    }
}
