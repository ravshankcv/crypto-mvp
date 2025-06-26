package com.crypto.business;

import static spark.Spark.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.TypeReference;
import org.web3j.protocol.core.methods.request.EthFilter;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class InvoiceMatcherService {

    private static final List<Invoice> matchedInvoices = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        Web3j web3 = Web3j.build(new HttpService("https://sepolia.infura.io/v3/26f29e34a90e4ab1ba3b18fe5c326fde"));
        String contractAddress = "0xd98bf8653e9c0d337e23ed1cb868a4a76d1a2c81";

        Event event = new Event("Transfer",
                Arrays.asList(
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Uint256>() {
                        }));

        EthFilter filter = new EthFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                contractAddress);
        filter.addSingleTopic(EventEncoder.encode(event));

        List<Invoice> pending = InvoiceDatabase.getPendingInvoices();

        web3.ethLogFlowable(filter).subscribe((Log log) -> {
            String toAddress = "0x" + log.getTopics().get(2).substring(26);
            String amount = new java.math.BigInteger(log.getData().substring(2), 16).toString();

            for (Invoice invoice : pending) {
                if (invoice.walletAddress.equalsIgnoreCase(toAddress) && invoice.amount.equals(amount)) {
                    System.out.println("✅ Invoice " + invoice.invoiceId + " matched.");
                    matchedInvoices.add(invoice);
                }
            }
        });

        // Serve matched invoices to frontend
        port(4567);
        staticFiles.location("/public");
        get("/matched-invoices", (req, res) -> {
            res.type("application/json");
            return new ObjectMapper().writeValueAsString(matchedInvoices);
        });
    }

    static List<Invoice> fetchMatchedInvoices() {
        // This method can be used to retrieve matched invoices
        // or perform further processing if needed.
        Web3j web3 = Web3j.build(new HttpService("https://sepolia.infura.io/v3/26f29e34a90e4ab1ba3b18fe5c326fde"));
        String contractAddress = "0xd98bf8653e9c0d337e23ed1cb868a4a76d1a2c81";

        Event event = new Event("Transfer",
                Arrays.asList(
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Uint256>() {
                        }));

        EthFilter filter = new EthFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                contractAddress);
        filter.addSingleTopic(EventEncoder.encode(event));

        List<Invoice> pending = InvoiceDatabase.getPendingInvoices();

        web3.ethLogFlowable(filter).subscribe((Log log) -> {
            String toAddress = "0x" + log.getTopics().get(2).substring(26);
            String amount = new java.math.BigInteger(log.getData().substring(2), 16).toString();

            for (Invoice invoice : pending) {
                if (invoice.walletAddress.equalsIgnoreCase(toAddress) && invoice.amount.equals(amount)) {
                    System.out.println("✅ Invoice " + invoice.invoiceId + " matched.");
                    matchedInvoices.add(invoice);
                }
            }
        });
        return matchedInvoices;
    }
}
// This service listens for USDC transfer events and matches them against
// pending invoices.
// When a match is found, it adds the invoice to a list of matched invoices.