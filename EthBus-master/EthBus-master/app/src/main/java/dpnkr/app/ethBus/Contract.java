package dpnkr.app.ethBus;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Contract {
    private Web3j web3j;
    private Credentials credentials;
    private ContractGasProvider gasProvider;
    private String contractAddress = "0x004d3e4c26ef9114f75b1dd938feb22f1f90ed7a";

    public Contract(Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        this.web3j = web3j;
        this.credentials = credentials;
        this.gasProvider = gasProvider;
    }

    /** Contract already deployed at Rinkeby testnet via REMIX
     *
    public String deploy() throws Exception {
        OperatorContract contract = OperatorContract.deploy(
                web3j, credentials,
                gasProvider,
                BigInteger.valueOf(1000000)).send();
        return contract.getContractAddress();
    }
    */

    public String generatePassengerContract(String passengerName, String phoneNumber, String fiscalCode) throws Exception {
        OperatorContract contract = OperatorContract.load(
                contractAddress, web3j, credentials, gasProvider);
        TransactionReceipt transactionReceipt = contract.generatePassengerContract(passengerName, phoneNumber, fiscalCode).send();
        if (transactionReceipt.isStatusOK()) {
            String passengerContractAddr = contract.getUniquePassengerContractAddress(fiscalCode).send();
            System.out.println(passengerContractAddr);
            saveData(passengerContractAddr);
            return transactionReceipt.getTransactionHash();
        } else {
            return transactionReceipt.getTransactionHash();
        }
    }

    public String depositSum (BigInteger weiVal, String passengerContractAddr) throws Exception {
        PassengerContract contract = PassengerContract.load(
                passengerContractAddr, web3j, credentials, gasProvider);
        TransactionReceipt transactionReceipt = contract.depositSum(weiVal).send();
        return transactionReceipt.getTransactionHash();
    }

    public String payTicketFee(BigInteger startTime, String startLocation, String passengerContractAddr) throws Exception {
        PassengerContract contract = PassengerContract.load(
                passengerContractAddr, web3j, credentials, gasProvider);
        TransactionReceipt transactionReceipt = contract.payTicketFee(startTime, startLocation).send();
        return transactionReceipt.toString();
    }

    public String updateTravel(BigInteger endTime, String endLocation, String passengerContractAddr) throws Exception {
        PassengerContract contract = PassengerContract.load(
                passengerContractAddr, web3j, credentials, gasProvider);
        TransactionReceipt transactionReceipt = contract.updateTravelStruct(endTime, endLocation).send();
        return transactionReceipt.getTransactionHash();
    }

    public BigInteger queryFrozenDeposit(String passengerContractAddr) throws Exception {
        PassengerContract contract = PassengerContract.load(
                passengerContractAddr, web3j, credentials, gasProvider);
        BigInteger result = contract.freezeDeposit().send();
        return result;
    }

    public void saveAddress(String newAddr) {
        List<String> pAddressList = Paper.book().read("pAddressList", new ArrayList<String>());
        pAddressList.add(newAddr);
        Paper.book().write("pAddressList", pAddressList);
    }

    public void saveData(String newAddr) {
        List<String> pAddressList = Paper.book().read("pAddressList", new ArrayList<String>());
        pAddressList.add(newAddr);
        Paper.book().write("pAddressList", pAddressList);
    }
}