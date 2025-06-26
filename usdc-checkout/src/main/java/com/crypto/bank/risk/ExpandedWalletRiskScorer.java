package com.crypto.bank.risk;

import java.math.BigInteger;
import java.util.*;

public class ExpandedWalletRiskScorer {

    public static class RiskReport {
        public double score;
        public String grade;
        public List<String> riskFlags;

        public RiskReport(double score, String grade, List<String> flags) {
            this.score = score;
            this.grade = grade;
            this.riskFlags = flags;
        }

        @Override
        public String toString() {
            return "Score: " + score + "\nGrade: " + grade + "\nFlags: " + riskFlags;
        }
    }

    public static RiskReport scoreWallet(String walletAddress, Map<String, Object> metrics) {
        double score = 100.0;
        List<String> flags = new ArrayList<>();

        // Metric 1: Wallet Age
        double ageInDays = (double) metrics.getOrDefault("ageDays", 0);
        if (ageInDays < 30) {
            score -= 20;
            flags.add("New wallet");
        }

        // Metric 2: Transaction Count
        double txCount = (double) metrics.getOrDefault("txCount", 0);
        if (txCount < 5) {
            score -= 10;
            flags.add("Low transaction activity");
        }

        // Metric 3: Token Diversity
        double tokenTypes = (double) metrics.getOrDefault("tokenTypes", 0);
        if (tokenTypes > 20) {
            score -= 10;
            flags.add("High token churn");
        }

        // Metric 4: Known Bad Actor
        boolean flagged = (boolean) metrics.getOrDefault("isFlagged", false);
        if (flagged) {
            score -= 50;
            flags.add("Known suspicious entity");
        }

        // Metric 5: Night-time transfers
        boolean oddHour = (boolean) metrics.getOrDefault("oddHourTransfer", false);
        if (oddHour) {
            score -= 5;
            flags.add("Unusual transfer hours");
        }

        // Metric 6: DeFi Interaction
        boolean isDeFi = (boolean) metrics.getOrDefault("interactedWithDeFi", false);
        if (isDeFi) {
            score -= 5;
            flags.add("High DeFi exposure");
        }

        // Clamp score to 0-100
        score = Math.max(0, Math.min(100, score));
        String grade = classify(score);

        return new RiskReport(score, grade, flags);
    }

    private static String classify(double score) {
        if (score > 80)
            return "Low Risk";
        else if (score > 50)
            return "Moderate Risk";
        else
            return "High Risk";
    }
}
