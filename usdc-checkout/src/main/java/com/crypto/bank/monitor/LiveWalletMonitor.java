package com.crypto.bank.monitor;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.Event;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.utils.Async;
import static spark.Spark.*;
import com.crypto.bank.BankService;
import com.crypto.business.AnomalyEngine;
import com.crypto.business.WebhookEvent;
import com.crypto.business.WebhookStatusStore;
import com.google.gson.Gson;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LiveWalletMonitor {

    public static void start(Consumer<Log> eventHandler) {
        Web3j web3 = Web3j.build(new HttpService("https://sepolia.infura.io/v3/26f29e34a90e4ab1ba3b18fe5c326fde"), 2000,
                Async.defaultExecutorService());

        String contractAddress = "0xd98bf8653e9c0d337e23ed1cb868a4a76d1a2c81";

        Event transferEvent = new Event("Transfer",
                Arrays.asList(
                        new org.web3j.abi.TypeReference<Address>(true) {
                        },
                        new org.web3j.abi.TypeReference<Address>(true) {
                        },
                        new org.web3j.abi.TypeReference<Uint256>() {
                        }));

        EthFilter filter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST,
                contractAddress);
        filter.addSingleTopic(EventEncoder.encode(transferEvent));

        // web3.ethLogFlowable(filter).subscribe((io.reactivex.functions.Consumer<?
        // super Log>) eventHandler);

        // web3.ethLogFlowable(filter).subscribe((Log log) -> {
        web3.ethLogFlowable(filter).subscribe((log) -> {
            String from = "0x" + log.getTopics().get(1).substring(26);
            String to = "0x" + log.getTopics().get(2).substring(26);
            String txId = log.getTransactionHash();

            System.out.println("USDC Transfer Detected!");
            System.out.println("From: " + from);
            System.out.println("To: " + to);
            System.out.println("Tx Hash: " + txId);

            Map<String, String> event = new HashMap<>();
            event.put("from", log.getTopics().get(1));
            event.put("to", log.getTopics().get(2));
            event.put("txHash", log.getTransactionHash());
            synchronized (BankService.liveFeed) {
                BankService.liveFeed.add(0, event);
                if (BankService.liveFeed.size() > 20)
                    BankService.liveFeed.remove(BankService.liveFeed.size() - 1);
            }
        });
    }

    public static void load() {
        get("/api/live-feed", (req, res) -> {
            res.type("application/json");
            synchronized (BankService.liveFeed) {
                return new Gson().toJson(BankService.liveFeed);
            }
        });
    }

}
