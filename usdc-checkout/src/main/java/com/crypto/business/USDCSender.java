package com.crypto.business;

import static spark.Spark.*;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

public class USDCSender {
    public static void main(String[] args) throws Exception {

        port(8081);
        staticFiles.location("/public"); // loads dashboard.html

        // Setup Web3j and credentials
        Web3j web3j = Web3j.build(new HttpService("https://sepolia.infura.io/v3/26f29e34a90e4ab1ba3b18fe5c326fde"));
        // Contract address of deployed MockUSDC on Sepolia
        String contractAddress = "0xd98bf8653e9c0d337e23ed1cb868a4a76d1a2c81";

        /*
         * Credentials credentials = Credentials
         * .create("fc3cbd9420dc50bcfe793be2479d8b000d7fd6c009f54badb63bd92d6adf6dc1");
         * // sender's private key
         * 
         * // Load the deployed contract
         * ContractGasProvider gasProvider = new DefaultGasProvider();
         * MockUSDC usdc = MockUSDC.load(contractAddress, web3j, credentials,
         * gasProvider);
         * 
         * // Recipient and amount
         * String sender = credentials.getAddress();
         * System.out.println("Sender address: " + sender);
         * 
         * String recipient = "0xB428b0eef777a19dD27f8D5e640Ba0e1D865bca1";
         * BigInteger amount = new BigInteger("1000000000000000000"); // if USDC has 6
         * decimals, this is 1 USDC
         * 
         * // Send USDC
         * var txReceipt = usdc.transfer(recipient, amount).send();
         * System.out.println("✅ USDC Transfer Complete. Tx Hash: " +
         * txReceipt.getTransactionHash());
         * 
         * BigInteger balance_sender = usdc.balanceOf(sender).send();
         * BigInteger balance_recipient = usdc.balanceOf(recipient).send();
         * System.out.println("Sender USDC balance: " + balance_sender);
         * System.out.println("Recipient USDC balance: " + balance_recipient);
         */

        post("/transfer-usdc", (req, res) -> {
            ObjectMapper mapper = new ObjectMapper();
            TransferRequest input = mapper.readValue(req.body(), TransferRequest.class);
            try {
                Credentials senderCredentials = Credentials.create(input.from);

                // Load the deployed contract
                ContractGasProvider gasProvider = new DefaultGasProvider();
                MockUSDC usdc = MockUSDC.load(contractAddress, web3j, senderCredentials, gasProvider);
                String sender = senderCredentials.getAddress();
                String recipient = input.to;
                BigInteger amount = new BigInteger(input.amount); // Amount in smallest unit (e.g

                // if USDC has 6 decimals, this is 1 USDC = 1000000 in smallest unit)

                System.out.println("Sender address: " + sender);
                System.out.println("Recipient address: " + recipient);
                System.out.println("Transfer amount: " + amount);

                // Send USDC
                var txReceipt = usdc.transfer(recipient, amount).send();
                System.out.println("✅ USDC Transfer Complete. Tx Hash: " + txReceipt.getTransactionHash());

                BigInteger balance_sender = usdc.balanceOf(sender).send();
                BigInteger balance_recipient = usdc.balanceOf(recipient).send();
                System.out.println("Sender USDC balance: " + balance_sender);
                System.out.println("Recipient USDC balance: " + balance_recipient);
                return "Success! Tx Hash: " + txReceipt.getTransactionHash();

            } catch (Exception e) {
                res.status(500);
                return "Transfer failed: " + e.getMessage();
            }
        });

    }

    static class TransferRequest {
        public String from;
        public String to;
        public String amount;
    }
}
