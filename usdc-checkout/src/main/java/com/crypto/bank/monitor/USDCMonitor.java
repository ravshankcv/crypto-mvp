package com.crypto.bank.monitor;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import com.crypto.bank.scoring.WalletRiskScorer;

import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.TypeReference;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.Log;

import java.util.Arrays;
import java.util.function.Consumer;

public class USDCMonitor {

    private final String usdcContractAddress = "0xd98bf8653e9c0d337e23ed1cb868a4a76d1a2c81";

    public void start(Consumer<String> onWalletActivity) {
        Web3j web3 = Web3j.build(new HttpService("https://sepolia.infura.io/v3/26f29e34a90e4ab1ba3b18fe5c326fde"));

        Event transferEvent = new Event("Transfer",
                Arrays.asList(
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {
                        }));

        EthFilter filter = new EthFilter(
                DefaultBlockParameterName.LATEST,
                DefaultBlockParameterName.LATEST,
                usdcContractAddress);

        filter.addSingleTopic(EventEncoder.encode(transferEvent));

        web3.ethLogFlowable(filter).subscribe((Log log) -> {
            String from = "0x" + log.getTopics().get(1).substring(26);
            String to = "0x" + log.getTopics().get(2).substring(26);

            System.out.println("Transfer from " + from + " to " + to);
            onWalletActivity.accept(from);
            onWalletActivity.accept(to);
            // Inside transfer event callback:
            WalletRiskScorer.registerTransfer(from);
            WalletRiskScorer.registerTransfer(to);

        });
    }
}
