package com.crypto.business;

import static spark.Spark.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import com.google.gson.Gson;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import com.crypto.business.MockUSDC;

public class QrPaymentSimulator {

    // Replace with actual deployed values
    private static final String INFURA_URL = "https://sepolia.infura.io/v3/26f29e34a90e4ab1ba3b18fe5c326fde";
    private static final String USDC_CONTRACT_ADDRESS = "0xd98bf8653e9c0d337e23ed1cb868a4a76d1a2c81";
    private static final String RECEIVER_WALLET = "0xB428b0eef777a19dD27f8D5e640Ba0e1D865bca1";

    public static void main(String[] args) {
        port(4570);
        staticFiles.location("/public"); // loads dashboard.html
        Gson gson = new Gson();

        post("/simulate-payment", (req, res) -> {
            Map<String, String> body = gson.fromJson(req.body(), Map.class);
            String invoiceId = body.get("invoiceId");
            String amountStr = body.get("amount");
            String fromWalletPrivateKey = body.get("fromWallet"); // expecting private key here

            try {
                Web3j web3 = Web3j.build(new HttpService(INFURA_URL));
                Credentials credentials = Credentials.create(fromWalletPrivateKey);
                MockUSDC usdc = MockUSDC.load(USDC_CONTRACT_ADDRESS, web3, credentials, new DefaultGasProvider());

                BigDecimal amount = new BigDecimal(amountStr);
                BigInteger scaledAmount = amount.multiply(BigDecimal.TEN.pow(18)).toBigInteger();

                usdc.transfer(RECEIVER_WALLET, scaledAmount).send();

                res.type("application/json");
                return gson.toJson(Map.of(
                        "status", "confirmed",
                        "invoiceId", invoiceId,
                        "tx", "transfer complete"));

            } catch (Exception e) {
                res.status(500);
                return gson.toJson(Map.of(
                        "status", "error",
                        "message", e.getMessage()));
            }
        });
    }
}
