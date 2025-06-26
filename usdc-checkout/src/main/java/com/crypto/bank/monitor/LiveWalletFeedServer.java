package com.crypto.bank.monitor;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;

import org.web3j.protocol.core.methods.response.Log;

import com.crypto.bank.BankService;
import com.google.gson.Gson;

public class LiveWalletFeedServer {
    public static void main(String[] args) {
        port(4572); // Choose a different port to avoid collision
        staticFileLocation("/public");

        LiveWalletMonitor.load();
        System.out.println("✅ LiveWalletFeedServer running at http://localhost:4572/api/live-feed");

        // Start live USDC transfer monitoring
        LiveWalletMonitor.start((Log log) -> {
        });
    }
}