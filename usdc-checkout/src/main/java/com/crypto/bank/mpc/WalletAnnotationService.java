package com.crypto.bank.mpc;

import java.util.*;
import com.google.gson.Gson;

public class WalletAnnotationService {
    private static final Map<String, List<String>> annotations = new HashMap<>();

    public static void addAnnotation(String wallet, String tag) {
        annotations.computeIfAbsent(wallet, k -> new ArrayList<>()).add(tag);
    }

    public static List<String> getAnnotations(String wallet) {
        return annotations.getOrDefault(wallet, new ArrayList<>());
    }

    public static String getAllAnnotationsJson() {
        return new Gson().toJson(annotations);
    }
}
