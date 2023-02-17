package dpnkr.app.ethBus;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.1.1.
 */
public class PassengerContract extends Contract {
    private static final String BINARY = "60806040526000600a556000601055600060115534801561001f57600080fd5b5060405160c080610d51833981018060405260c081101561003f57600080fd5b508051602082015160408301516060840151608085015160a09095015160079490945560048054600160a060020a0394851662010000026201000060b060020a03199091161790556005805493909216600160a060020a0319909316929092179055600b55600e91909155600f55600060085562278d00600955610c89806100c86000396000f3fe608060405260043610610147576000357c01000000000000000000000000000000000000000000000000000000009004806363738555116100c8578063aac6466c1161008c578063aac6466c1461044c578063b83bebec14610506578063c71daccb1461051b578063d2f2f95a14610530578063e94b2f4414610538578063fb5e05181461054d57610147565b80636373855514610227578063699f548f1461035357806369aa2b601461040d5780639f908a2014610422578063a0994c471461043757610147565b806322922a901161010f57806322922a90146101c75780634e53385a146101f857806358f29334146102025780635e47e037146102175780636039af521461021f57610147565b8063010ab8441461014c57806308c9c186146101735780630b26b3d31461018857806318a680281461019d5780631df4ccfc146101b2575b600080fd5b34801561015857600080fd5b50610161610562565b60408051918252519081900360200190f35b34801561017f57600080fd5b50610161610568565b34801561019457600080fd5b5061016161056e565b3480156101a957600080fd5b50610161610574565b3480156101be57600080fd5b5061016161057a565b3480156101d357600080fd5b506101dc610580565b60408051600160a060020a039092168252519081900360200190f35b610200610595565b005b34801561020e57600080fd5b5061016161059b565b6102006105a1565b61020061065b565b34801561023357600080fd5b506102516004803603602081101561024a57600080fd5b50356106ce565b6040518080602001878152602001806020018681526020018515151515815260200184151515158152602001838103835289818151815260200191508051906020019080838360005b838110156102b257818101518382015260200161029a565b50505050905090810190601f1680156102df5780820380516001836020036101000a031916815260200191505b50838103825287518152875160209182019189019080838360005b838110156103125781810151838201526020016102fa565b50505050905090810190601f16801561033f5780820380516001836020036101000a031916815260200191505b509850505050505050505060405180910390f35b34801561035f57600080fd5b506102006004803603604081101561037657600080fd5b8135919081019060408101602082013564010000000081111561039857600080fd5b8201836020820111156103aa57600080fd5b803590602001918460018302840111640100000000831117156103cc57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061083c945050505050565b34801561041957600080fd5b5061016161090e565b34801561042e57600080fd5b50610161610914565b34801561044357600080fd5b5061016161091a565b34801561045857600080fd5b506102006004803603604081101561046f57600080fd5b8135919081019060408101602082013564010000000081111561049157600080fd5b8201836020820111156104a357600080fd5b803590602001918460018302840111640100000000831117156104c557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610920945050505050565b34801561051257600080fd5b50610161610ad0565b34801561052757600080fd5b50610161610ad6565b610200610adc565b34801561054457600080fd5b50610161610bb9565b34801561055957600080fd5b50610161610bbf565b60095481565b60105481565b600b5481565b600d5481565b600c5481565b600454620100009004600160a060020a031681565b34600655565b600a5481565b600454620100009004600160a060020a031633146105be57600080fd5b600d543031111561061057600554600d54604051600160a060020a039092169181156108fc0291906000818181858888f19350505050158015610605573d6000803e3d6000fd5b506000600d55610659565b600d8054308031909103909155600554604051600160a060020a0391909116913180156108fc02916000818181858888f19350505050158015610657573d6000803e3d6000fd5b505b565b600454620100009004600160a060020a0316331461067857600080fd5b600b5430311161068757600080fd5b600554600b54604051600160a060020a039092169181156108fc0291906000818181858888f193505050501580156106c3573d6000803e3d6000fd5b506009544201600855565b60018054829081106106dc57fe5b60009182526020918290206005919091020180546040805160026001841615610100026000190190931692909204601f8101859004850283018501909152808252919350918391908301828280156107755780601f1061074a57610100808354040283529160200191610775565b820191906000526020600020905b81548152906001019060200180831161075857829003601f168201915b505050505090806001015490806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108195780601f106107ee57610100808354040283529160200191610819565b820191906000526020600020905b8154815290600101906020018083116107fc57829003601f168201915b50505050600383015460049093015491929160ff80821692506101009091041686565b600454620100009004600160a060020a0316331461085957600080fd5b6008544211610898576003829055805161087a906002906020840190610bc5565b506004805461ff001960ff199091166001171661010017905561090a565b600754600a5430310311156108e557600754600a80549091019055600382905580516108cb906002906020840190610bc5565b506004805461ff001960ff1990911660011716905561090a565b600382905580516108fd906002906020840190610bc5565b506004805461ffff191690555b5050565b60115481565b600e5481565b60075481565b600454620100009004600160a060020a0316331461093d57600080fd5b604080516002805460206001808316156101000260001901909216839004601f8101829004909102840160e090810190955260c084018181529194849391908401828280156109cd5780601f106109a2576101008083540402835291602001916109cd565b820191906000526020600020905b8154815290600101906020018083116109b057829003601f168201915b5050509183525050600354602080830191909152604082018590526060820186905260045460ff8082161515608085015261010090910416151560a09092019190915282546001810180855560009485529382902083518051600590930290910192610a3e92849290910190610bc5565b50602082810151600183015560408301518051610a619260028501920190610bc5565b50606082015160038083019190915560808301516004928301805460a09095015115156101000261ff001992151560ff1990961695909517919091169390931790925590549054908403915060ff1615610ac2576010805482019055610acb565b60118054820190555b505050565b60085481565b30315b90565b600554600160a060020a03163314610af357600080fd5b600e5460108054909102600c819055600f5460118054909102600d5560009283905591909155600a5410610b6857600554600c54604051600160a060020a039092169181156108fc0291906000818181858888f19350505050158015610b5d573d6000803e3d6000fd5b506000600a55610659565b600554600a54604051600160a060020a039092169181156108fc0291906000818181858888f19350505050158015610ba4573d6000803e3d6000fd5b50600a8054600c5403600202600d5560009055565b60065481565b600f5481565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610c0657805160ff1916838001178555610c33565b82800160010185558215610c33579182015b82811115610c33578251825591602001919060010190610c18565b50610c3f929150610c43565b5090565b610ad991905b80821115610c3f5760008155600101610c4956fea165627a7a723058209dad223d5371bad30146174096c924872e5bba6f1afbfaba9227fced8906c8780029";

    public static final String FUNC_TRANSITPASSVALIDITYPERIOD = "transitPassValidityPeriod";

    public static final String FUNC_TOTALTRAVELTIME = "totalTravelTime";

    public static final String FUNC_TRANSITPASSFEE = "transitPassFee";

    public static final String FUNC_TOTALFEEUNPAID = "totalFeeUnpaid";

    public static final String FUNC_TOTALFEE = "totalFee";

    public static final String FUNC_PASSENGERADDR = "passengerAddr";

    public static final String FUNC_DEPOSITSUM = "depositSum";

    public static final String FUNC_FREEZEDEPOSIT = "freezeDeposit";

    public static final String FUNC_PAYREMAININGFEE = "payRemainingFee";

    public static final String FUNC_RENEWTRANSITPASS = "renewTransitPass";

    public static final String FUNC_TRAVELS = "travels";

    public static final String FUNC_PAYTICKETFEE = "payTicketFee";

    public static final String FUNC_TOTALTRAVELTIMEUNPAID = "totalTravelTimeUnpaid";

    public static final String FUNC_TARIFF = "tariff";

    public static final String FUNC_TICKETFEE = "ticketFee";

    public static final String FUNC_UPDATETRAVELSTRUCT = "updateTravelStruct";

    public static final String FUNC_TRANSITPASSEXPIRYTIME = "transitPassExpiryTime";

    public static final String FUNC_CHECKBALANCE = "checkBalance";

    public static final String FUNC_RETRIEVEDEPOSITSUM = "retrieveDepositSum";

    public static final String FUNC_CURRENTDEPOSIT = "currentDeposit";

    public static final String FUNC_TARIFFHIGH = "tariffHigh";

    @Deprecated
    protected PassengerContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PassengerContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected PassengerContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected PassengerContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<BigInteger> transitPassValidityPeriod() {
        final Function function = new Function(FUNC_TRANSITPASSVALIDITYPERIOD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> totalTravelTime() {
        final Function function = new Function(FUNC_TOTALTRAVELTIME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> transitPassFee() {
        final Function function = new Function(FUNC_TRANSITPASSFEE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> totalFeeUnpaid() {
        final Function function = new Function(FUNC_TOTALFEEUNPAID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> totalFee() {
        final Function function = new Function(FUNC_TOTALFEE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> passengerAddr() {
        final Function function = new Function(FUNC_PASSENGERADDR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> depositSum(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_DEPOSITSUM, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<BigInteger> freezeDeposit() {
        final Function function = new Function(FUNC_FREEZEDEPOSIT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> payRemainingFee(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_PAYREMAININGFEE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> renewTransitPass(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_RENEWTRANSITPASS, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<Tuple6<String, BigInteger, String, BigInteger, Boolean, Boolean>> travels(BigInteger param0) {
        final Function function = new Function(FUNC_TRAVELS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}));
        return new RemoteCall<Tuple6<String, BigInteger, String, BigInteger, Boolean, Boolean>>(
                new Callable<Tuple6<String, BigInteger, String, BigInteger, Boolean, Boolean>>() {
                    @Override
                    public Tuple6<String, BigInteger, String, BigInteger, Boolean, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, BigInteger, String, BigInteger, Boolean, Boolean>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (Boolean) results.get(4).getValue(), 
                                (Boolean) results.get(5).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> payTicketFee(BigInteger _startTime, String _startLoc) {
        final Function function = new Function(
                FUNC_PAYTICKETFEE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_startTime), 
                new org.web3j.abi.datatypes.Utf8String(_startLoc)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalTravelTimeUnpaid() {
        final Function function = new Function(FUNC_TOTALTRAVELTIMEUNPAID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> tariff() {
        final Function function = new Function(FUNC_TARIFF, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> ticketFee() {
        final Function function = new Function(FUNC_TICKETFEE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> updateTravelStruct(BigInteger _endTime, String _endLoc) {
        final Function function = new Function(
                FUNC_UPDATETRAVELSTRUCT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_endTime), 
                new org.web3j.abi.datatypes.Utf8String(_endLoc)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> transitPassExpiryTime() {
        final Function function = new Function(FUNC_TRANSITPASSEXPIRYTIME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> checkBalance() {
        final Function function = new Function(FUNC_CHECKBALANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> retrieveDepositSum(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_RETRIEVEDEPOSITSUM, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<BigInteger> currentDeposit() {
        final Function function = new Function(FUNC_CURRENTDEPOSIT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> tariffHigh() {
        final Function function = new Function(FUNC_TARIFFHIGH, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static PassengerContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PassengerContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static PassengerContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PassengerContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static PassengerContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new PassengerContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static PassengerContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PassengerContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<PassengerContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger _ticketFee, String _passengerAddr, String _operatorAddr, BigInteger _transitPassFee, BigInteger _tariff, BigInteger _tariffHigh) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_ticketFee), 
                new org.web3j.abi.datatypes.Address(_passengerAddr), 
                new org.web3j.abi.datatypes.Address(_operatorAddr), 
                new org.web3j.abi.datatypes.generated.Uint256(_transitPassFee), 
                new org.web3j.abi.datatypes.generated.Uint256(_tariff), 
                new org.web3j.abi.datatypes.generated.Uint256(_tariffHigh)));
        return deployRemoteCall(PassengerContract.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<PassengerContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _ticketFee, String _passengerAddr, String _operatorAddr, BigInteger _transitPassFee, BigInteger _tariff, BigInteger _tariffHigh) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_ticketFee), 
                new org.web3j.abi.datatypes.Address(_passengerAddr), 
                new org.web3j.abi.datatypes.Address(_operatorAddr), 
                new org.web3j.abi.datatypes.generated.Uint256(_transitPassFee), 
                new org.web3j.abi.datatypes.generated.Uint256(_tariff), 
                new org.web3j.abi.datatypes.generated.Uint256(_tariffHigh)));
        return deployRemoteCall(PassengerContract.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<PassengerContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _ticketFee, String _passengerAddr, String _operatorAddr, BigInteger _transitPassFee, BigInteger _tariff, BigInteger _tariffHigh) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_ticketFee), 
                new org.web3j.abi.datatypes.Address(_passengerAddr), 
                new org.web3j.abi.datatypes.Address(_operatorAddr), 
                new org.web3j.abi.datatypes.generated.Uint256(_transitPassFee), 
                new org.web3j.abi.datatypes.generated.Uint256(_tariff), 
                new org.web3j.abi.datatypes.generated.Uint256(_tariffHigh)));
        return deployRemoteCall(PassengerContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<PassengerContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _ticketFee, String _passengerAddr, String _operatorAddr, BigInteger _transitPassFee, BigInteger _tariff, BigInteger _tariffHigh) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_ticketFee), 
                new org.web3j.abi.datatypes.Address(_passengerAddr), 
                new org.web3j.abi.datatypes.Address(_operatorAddr), 
                new org.web3j.abi.datatypes.generated.Uint256(_transitPassFee), 
                new org.web3j.abi.datatypes.generated.Uint256(_tariff), 
                new org.web3j.abi.datatypes.generated.Uint256(_tariffHigh)));
        return deployRemoteCall(PassengerContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
