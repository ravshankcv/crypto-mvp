package com.crypto.business;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import static spark.Spark.*;

import com.google.gson.Gson;

import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.request.EthFilter;

import java.math.BigInteger;
import java.util.Arrays;

public class USDCMonitor {
        public static void main(String[] args) throws Exception {

                port(8080);
                staticFiles.location("/public"); // loads business-ui.html

                Web3j web3 = Web3j.build(
                                new HttpService("https://sepolia.infura.io/v3/26f29e34a90e4ab1ba3b18fe5c326fde"));
                String contractAddress = "0xa742C2401f654Aa6cf3cF4D05825ce521F014Ba6";

                Event transferEvent = new Event("Transfer",
                                Arrays.asList(
                                                new TypeReference<Address>(true) {
                                                }, // from
                                                new TypeReference<Address>(true) {
                                                }, // to
                                                new TypeReference<Uint256>() {
                                                } // value
                                ));

                EthFilter filter = new EthFilter(
                                DefaultBlockParameterName.EARLIEST,
                                DefaultBlockParameterName.LATEST,
                                contractAddress);
                filter.addSingleTopic(EventEncoder.encode(transferEvent));

                System.out.println("Listening for USDC Transfer events...");

                web3.ethLogFlowable(filter).subscribe((Log log) -> {
                        String from = "0x" + log.getTopics().get(1).substring(26);
                        String to = "0x" + log.getTopics().get(2).substring(26);
                        String txId = log.getTransactionHash();

                        // Decode value
                        String hexValue = log.getData();
                        BigInteger rawValue = new BigInteger(hexValue.substring(2), 16);
                        double value = rawValue.doubleValue() / 1e18; // Assuming 18 decimal places

                        System.out.println("USDC Transfer Detected!");
                        System.out.println("From: " + from);
                        System.out.println("To: " + to);
                        System.out.println("Amount: " + value);

                        if (AnomalyEngine.isAnomalous(value)) {
                                System.out.println("⚠️ Anomaly Detected for transaction: " + value);
                        } else {
                                System.out.println("✅ Normal Transaction.");
                        }

                        WebhookEvent event = new WebhookEvent(
                                        "USDC Transfer",
                                        from,
                                        to,
                                        value,
                                        txId,
                                        "confirmed",
                                        System.currentTimeMillis());

                        WebhookStatusStore.addEvent(event);
                        System.out.println("✅ New USDC Transfer: " + txId + " from " + from + " to " + to);
                });

                post("/webhook", (req, res) -> {
                        Gson gson = new Gson();
                        WebhookEvent event = gson.fromJson(req.body(), WebhookEvent.class);
                        WebhookStatusStore.addEvent(event);
                        return "Received";
                });

                get("/webhook-status", (req, res) -> {
                        res.type("application/json");
                        return new Gson().toJson(WebhookStatusStore.getEvents());
                });

                get("/matched-invoices", (req, res) -> {
                        res.type("application/json");
                        return new Gson().toJson(InvoiceMatcherService.fetchMatchedInvoices());
                });

                AnalyticsService.registerRoutes();
                QuickBooksSyncStatusService.registerRoutes();
                RefundService.registerRoutes();
        }
}
