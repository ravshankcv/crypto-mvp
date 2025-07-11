package com.crypto.business;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
//import org.web3j.abi.datatypes.CustomError;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>
 * Auto generated code.
 * <p>
 * <strong>Do not modify!</strong>
 * <p>
 * Please use the <a href="https://docs.web3j.io/command_line.html">web3j
 * command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/LFDT-web3j/web3j/tree/main/codegen">codegen
 * module</a> to update.
 *
 * <p>
 * Generated with web3j version 1.7.0.
 */
@SuppressWarnings("rawtypes")
public class MockUSDC extends Contract {
    public static final String BINARY = "608060405234801562000010575f80fd5b506040518060400160405280600981526020017f4d6f636b205553444300000000000000000000000000000000000000000000008152506040518060400160405280600481526020017f555344430000000000000000000000000000000000000000000000000000000081525081600390816200008e919062000602565b508060049081620000a0919062000602565b505050620000e233620000b8620000e860201b60201c565b600a620000c691906200086f565b620f4240620000d69190620008bf565b620000f060201b60201c565b62000a08565b5f6012905090565b5f73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff160362000163575f6040517fec442f050000000000000000000000000000000000000000000000000000000081526004016200015a91906200094c565b60405180910390fd5b620001765f83836200017a60201b60201c565b5050565b5f73ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1603620001ce578060025f828254620001c1919062000967565b925050819055506200029f565b5f805f8573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f20549050818110156200025a578381836040517fe450d38c0000000000000000000000000000000000000000000000000000000081526004016200025193929190620009b2565b60405180910390fd5b8181035f808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f2081905550505b5f73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1603620002e8578060025f828254039250508190555062000332565b805f808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f82825401925050819055505b8173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef83604051620003919190620009ed565b60405180910390a3505050565b5f81519050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f60028204905060018216806200041a57607f821691505b60208210810362000430576200042f620003d5565b5b50919050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f60088302620004947fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8262000457565b620004a0868362000457565b95508019841693508086168417925050509392505050565b5f819050919050565b5f819050919050565b5f620004ea620004e4620004de84620004b8565b620004c1565b620004b8565b9050919050565b5f819050919050565b6200050583620004ca565b6200051d6200051482620004f1565b84845462000463565b825550505050565b5f90565b6200053362000525565b62000540818484620004fa565b505050565b5b8181101562000567576200055b5f8262000529565b60018101905062000546565b5050565b601f821115620005b657620005808162000436565b6200058b8462000448565b810160208510156200059b578190505b620005b3620005aa8562000448565b83018262000545565b50505b505050565b5f82821c905092915050565b5f620005d85f1984600802620005bb565b1980831691505092915050565b5f620005f28383620005c7565b9150826002028217905092915050565b6200060d826200039e565b67ffffffffffffffff811115620006295762000628620003a8565b5b62000635825462000402565b620006428282856200056b565b5f60209050601f83116001811462000678575f841562000663578287015190505b6200066f8582620005e5565b865550620006de565b601f198416620006888662000436565b5f5b82811015620006b1578489015182556001820191506020850194506020810190506200068a565b86831015620006d15784890151620006cd601f891682620005c7565b8355505b6001600288020188555050505b505050505050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f8160011c9050919050565b5f808291508390505b60018511156200077057808604811115620007485762000747620006e6565b5b6001851615620007585780820291505b8081029050620007688562000713565b945062000728565b94509492505050565b5f826200078a57600190506200085c565b8162000799575f90506200085c565b8160018114620007b25760028114620007bd57620007f3565b60019150506200085c565b60ff841115620007d257620007d1620006e6565b5b8360020a915084821115620007ec57620007eb620006e6565b5b506200085c565b5060208310610133831016604e8410600b84101617156200082d5782820a905083811115620008275762000826620006e6565b5b6200085c565b6200083c84848460016200071f565b92509050818404811115620008565762000855620006e6565b5b81810290505b9392505050565b5f60ff82169050919050565b5f6200087b82620004b8565b9150620008888362000863565b9250620008b77fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff848462000779565b905092915050565b5f620008cb82620004b8565b9150620008d883620004b8565b9250828202620008e881620004b8565b91508282048414831517620009025762000901620006e6565b5b5092915050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f620009348262000909565b9050919050565b620009468162000928565b82525050565b5f602082019050620009615f8301846200093b565b92915050565b5f6200097382620004b8565b91506200098083620004b8565b92508282019050808211156200099b576200099a620006e6565b5b92915050565b620009ac81620004b8565b82525050565b5f606082019050620009c75f8301866200093b565b620009d66020830185620009a1565b620009e56040830184620009a1565b949350505050565b5f60208201905062000a025f830184620009a1565b92915050565b610dfc8062000a165f395ff3fe608060405234801561000f575f80fd5b5060043610610091575f3560e01c8063313ce56711610064578063313ce5671461013157806370a082311461014f57806395d89b411461017f578063a9059cbb1461019d578063dd62ed3e146101cd57610091565b806306fdde0314610095578063095ea7b3146100b357806318160ddd146100e357806323b872dd14610101575b5f80fd5b61009d6101fd565b6040516100aa9190610a75565b60405180910390f35b6100cd60048036038101906100c89190610b26565b61028d565b6040516100da9190610b7e565b60405180910390f35b6100eb6102af565b6040516100f89190610ba6565b60405180910390f35b61011b60048036038101906101169190610bbf565b6102b8565b6040516101289190610b7e565b60405180910390f35b6101396102e6565b6040516101469190610c2a565b60405180910390f35b61016960048036038101906101649190610c43565b6102ee565b6040516101769190610ba6565b60405180910390f35b610187610333565b6040516101949190610a75565b60405180910390f35b6101b760048036038101906101b29190610b26565b6103c3565b6040516101c49190610b7e565b60405180910390f35b6101e760048036038101906101e29190610c6e565b6103e5565b6040516101f49190610ba6565b60405180910390f35b60606003805461020c90610cd9565b80601f016020809104026020016040519081016040528092919081815260200182805461023890610cd9565b80156102835780601f1061025a57610100808354040283529160200191610283565b820191905f5260205f20905b81548152906001019060200180831161026657829003601f168201915b5050505050905090565b5f80610297610467565b90506102a481858561046e565b600191505092915050565b5f600254905090565b5f806102c2610467565b90506102cf858285610480565b6102da858585610513565b60019150509392505050565b5f6012905090565b5f805f8373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f20549050919050565b60606004805461034290610cd9565b80601f016020809104026020016040519081016040528092919081815260200182805461036e90610cd9565b80156103b95780601f10610390576101008083540402835291602001916103b9565b820191905f5260205f20905b81548152906001019060200180831161039c57829003601f168201915b5050505050905090565b5f806103cd610467565b90506103da818585610513565b600191505092915050565b5f60015f8473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f8373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f2054905092915050565b5f33905090565b61047b8383836001610603565b505050565b5f61048b84846103e5565b90507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff81101561050d57818110156104fe578281836040517ffb8f41b20000000000000000000000000000000000000000000000000000000081526004016104f593929190610d18565b60405180910390fd5b61050c84848484035f610603565b5b50505050565b5f73ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1603610583575f6040517f96c6fd1e00000000000000000000000000000000000000000000000000000000815260040161057a9190610d4d565b60405180910390fd5b5f73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16036105f3575f6040517fec442f050000000000000000000000000000000000000000000000000000000081526004016105ea9190610d4d565b60405180910390fd5b6105fe8383836107d2565b505050565b5f73ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff1603610673575f6040517fe602df0500000000000000000000000000000000000000000000000000000000815260040161066a9190610d4d565b60405180910390fd5b5f73ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff16036106e3575f6040517f94280d620000000000000000000000000000000000000000000000000000000081526004016106da9190610d4d565b60405180910390fd5b8160015f8673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f8573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f208190555080156107cc578273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925846040516107c39190610ba6565b60405180910390a35b50505050565b5f73ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1603610822578060025f8282546108169190610d93565b925050819055506108f0565b5f805f8573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f20549050818110156108ab578381836040517fe450d38c0000000000000000000000000000000000000000000000000000000081526004016108a293929190610d18565b60405180910390fd5b8181035f808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f2081905550505b5f73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1603610937578060025f8282540392505081905550610981565b805f808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f82825401925050819055505b8173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040516109de9190610ba6565b60405180910390a3505050565b5f81519050919050565b5f82825260208201905092915050565b5f5b83811015610a22578082015181840152602081019050610a07565b5f8484015250505050565b5f601f19601f8301169050919050565b5f610a47826109eb565b610a5181856109f5565b9350610a61818560208601610a05565b610a6a81610a2d565b840191505092915050565b5f6020820190508181035f830152610a8d8184610a3d565b905092915050565b5f80fd5b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f610ac282610a99565b9050919050565b610ad281610ab8565b8114610adc575f80fd5b50565b5f81359050610aed81610ac9565b92915050565b5f819050919050565b610b0581610af3565b8114610b0f575f80fd5b50565b5f81359050610b2081610afc565b92915050565b5f8060408385031215610b3c57610b3b610a95565b5b5f610b4985828601610adf565b9250506020610b5a85828601610b12565b9150509250929050565b5f8115159050919050565b610b7881610b64565b82525050565b5f602082019050610b915f830184610b6f565b92915050565b610ba081610af3565b82525050565b5f602082019050610bb95f830184610b97565b92915050565b5f805f60608486031215610bd657610bd5610a95565b5b5f610be386828701610adf565b9350506020610bf486828701610adf565b9250506040610c0586828701610b12565b9150509250925092565b5f60ff82169050919050565b610c2481610c0f565b82525050565b5f602082019050610c3d5f830184610c1b565b92915050565b5f60208284031215610c5857610c57610a95565b5b5f610c6584828501610adf565b91505092915050565b5f8060408385031215610c8457610c83610a95565b5b5f610c9185828601610adf565b9250506020610ca285828601610adf565b9150509250929050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f6002820490506001821680610cf057607f821691505b602082108103610d0357610d02610cac565b5b50919050565b610d1281610ab8565b82525050565b5f606082019050610d2b5f830186610d09565b610d386020830185610b97565b610d456040830184610b97565b949350505050565b5f602082019050610d605f830184610d09565b92915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f610d9d82610af3565b9150610da883610af3565b9250828201905080821115610dc057610dbf610d66565b5b9291505056fea2646970667358221220f478c9fc69d1df85b6f2d5038978cd7a4575fdd9e3e85a7aa077c2a61fa30bf164736f6c63430008140033\n";

    private static String librariesLinkedBinary;

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    // CustomError constants removed because CustomError does not exist in web3j

    public static final Event APPROVAL_EVENT = new Event("Approval",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>() {
            }));;

    public static final Event TRANSFER_EVENT = new Event("Transfer",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>() {
            }));;

    @Deprecated
    protected MockUSDC(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected MockUSDC(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected MockUSDC(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected MockUSDC(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String spender, BigInteger value) {
        final Function function = new Function(
                FUNC_APPROVE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, spender),
                        new org.web3j.abi.datatypes.generated.Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static List<ApprovalEventResponse> getApprovalEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(APPROVAL_EVENT,
                transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ApprovalEventResponse getApprovalEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(APPROVAL_EVENT, log);
        ApprovalEventResponse typedResponse = new ApprovalEventResponse();
        typedResponse.log = log;
        typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getApprovalEventFromLog(log));
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> transfer(String to, BigInteger value) {
        final Function function = new Function(
                FUNC_TRANSFER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to),
                        new org.web3j.abi.datatypes.generated.Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static List<TransferEventResponse> getTransferEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TRANSFER_EVENT,
                transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TransferEventResponse getTransferEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TRANSFER_EVENT, log);
        TransferEventResponse typedResponse = new TransferEventResponse();
        typedResponse.log = log;
        typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTransferEventFromLog(log));
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String from, String to,
            BigInteger value) {
        final Function function = new Function(
                FUNC_TRANSFERFROM,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from),
                        new org.web3j.abi.datatypes.Address(160, to),
                        new org.web3j.abi.datatypes.generated.Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> allowance(String owner, String spender) {
        final Function function = new Function(FUNC_ALLOWANCE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner),
                        new org.web3j.abi.datatypes.Address(160, spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String account) {
        final Function function = new Function(FUNC_BALANCEOF,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> name() {
        final Function function = new Function(FUNC_NAME,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static MockUSDC load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new MockUSDC(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static MockUSDC load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MockUSDC(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static MockUSDC load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new MockUSDC(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static MockUSDC load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new MockUSDC(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<MockUSDC> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MockUSDC.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<MockUSDC> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MockUSDC.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(),
                "");
    }

    @Deprecated
    public static RemoteCall<MockUSDC> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MockUSDC.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<MockUSDC> deploy(Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MockUSDC.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(),
                "");
    }

    // The linkLibraries method is removed because Contract.LinkReference does not
    // exist in web3j.
    // If you need to link libraries, implement this method with the correct type or
    // remove it if not needed.

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String spender;

        public BigInteger value;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger value;
    }
}
