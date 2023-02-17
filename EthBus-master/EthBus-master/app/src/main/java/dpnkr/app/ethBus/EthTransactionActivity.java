package dpnkr.app.ethBus;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.paperdb.Paper;


public class EthTransactionActivity extends Activity {
    String ethAddr, password, transactionType, timestamp, locationHash;
    Web3j web3j;
    Credentials credentials;
    ContractGasProvider gasProvider = new DefaultGasProvider();
    TextView walletAddress;
    TextView typeOfTransaction;
    TextView response;
    BigDecimal balance;
    BigInteger weiValue;
    String username, phoneNumber, fiscalCode;
    Button goBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        walletAddress = (TextView) findViewById(R.id.wallet_address);
        typeOfTransaction = (TextView) findViewById(R.id.transaction_type);
        response = (TextView) findViewById(R.id.result);
        goBack = (Button) findViewById(R.id.go_back);

        Wallet mWallet = new Wallet();
        password = "aniabhi";
        try {
            credentials = mWallet.loadCredentials(password);
            ethAddr = credentials.getAddress();
            walletAddress.setText("Ethereum Wallet Address: " + ethAddr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            loadData();
            try {
                switch (transactionType) {
                    case "createPassengerContract":
                        new LongOperation().execute("createPassengerContract");
                        break;

                    case "payTicketFee":
                        new LongOperation().execute("payTicketFee");
                        break;

                    case "updateTravelStruct":
                        new LongOperation().execute("updateTravelStruct");
                        break;

                    case "depositSum":
                        new LongOperation().execute("depositSum");
                        break;

                    case "contractQuery":
                        new LongOperation().execute("contractQuery");
                        break;
                }
            } catch (Exception e) {
            }

            web3j = mWallet.constructWeb3("https://rinkeby.infura.io/v3/8ec6481cb37b48e2beb746659efd2bf3");
            loadBalance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadData() throws Exception {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        transactionType = extras.getString("TransactionType");
        typeOfTransaction.setText("Calling Function: \n~~" + transactionType + "~~\nOn passenger smart contract");
        if (transactionType.matches("contractQuery")) {
            return;
        }
        else if (transactionType.matches("createPassengerContract")) {
            username = extras.getString("Username");
            phoneNumber = extras.getString("PhoneNumber");
            fiscalCode = extras.getString("FiscalCode");
        }
        else if (transactionType.matches("depositSum")) {
            String ethVal = extras.getString("EthereumValue");
            BigDecimal tempVal = Convert.toWei(new BigDecimal(ethVal), Convert.Unit.ETHER);
            weiValue = tempVal.toBigInteger();
            return;
        }
        timestamp = extras.getString("Time");
        locationHash = extras.getString("Location");
    }

    public void loadBalance() throws Exception {
        EthGetBalance ethGetBalance = web3j.ethGetBalance(ethAddr, DefaultBlockParameterName.LATEST).sendAsync().get();
        //balance = ethGetBalance.getBalance();
        balance = Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER);
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            Wallet wallet = new Wallet();
            response.setText("Waiting for transaction to get confirmed...");
            try {
                switch (params[0]) {
                    case "createPassengerContract":
                        web3j = wallet.constructWeb3("https://rinkeby.infura.io/v3/8ec6481cb37b48e2beb746659efd2bf3");
                        credentials = wallet.loadCredentials(password);
                        Contract contract = new Contract(web3j, credentials, gasProvider);
                        String transactionHash = contract.generatePassengerContract(username, phoneNumber, fiscalCode);
                        return "Contract created: " + transactionHash;

                    case "updateTravelStruct":
                        web3j = wallet.constructWeb3("https://rinkeby.infura.io/v3/8ec6481cb37b48e2beb746659efd2bf3");
                        credentials = wallet.loadCredentials(password);
                        contract = new Contract(web3j, credentials, gasProvider);
                        String passConAddr = getPassConAddr();
                        transactionHash = contract.updateTravel(new BigInteger(timestamp), locationHash, passConAddr);
                        return "Travel finished and logged " + transactionHash;

                    case "payTicketFee":
                        web3j = wallet.constructWeb3("https://rinkeby.infura.io/v3/8ec6481cb37b48e2beb746659efd2bf3");
                        credentials = wallet.loadCredentials(password);
                        contract = new Contract(web3j, credentials, gasProvider);
                        passConAddr = getPassConAddr();
                        transactionHash = contract.payTicketFee(new BigInteger(timestamp), locationHash, passConAddr);
                        return "Ticket fee paid " + transactionHash;

                    case "depositSum":
                        web3j = wallet.constructWeb3("https://rinkeby.infura.io/v3/8ec6481cb37b48e2beb746659efd2bf3");
                        credentials = wallet.loadCredentials(password);
                        contract = new Contract(web3j, credentials, gasProvider);
                        passConAddr = getPassConAddr();
                        System.out.println(passConAddr);
                        transactionHash = contract.depositSum(weiValue, passConAddr);
                        return "Contract recharged successfully: " + transactionHash;

                    case "contractQuery":
                        web3j = wallet.constructWeb3("https://rinkeby.infura.io/v3/8ec6481cb37b48e2beb746659efd2bf3");
                        credentials = wallet.loadCredentials(password);
                        contract = new Contract(web3j, credentials, gasProvider);
                        passConAddr = getPassConAddr();
                        BigInteger result = contract.queryFrozenDeposit(passConAddr);
                        return result.toString();

                    default:
                        web3j = wallet.constructWeb3("https://rinkeby.infura.io/v3/8ec6481cb37b48e2beb746659efd2bf3");
                        loadBalance();
                        return "loadBalance";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                // Ether send trasnaction, update balance
                if (result == "loadBalance") {
                    response.setText("Wallet balance : " + balance.toString());
                }
                else {
                    // Contract trasnaction, update response view
                    response.setText("Transaction Result: \n" + result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }*/
    }

    @TargetApi(Build.VERSION_CODES.O)
    public String asciiToHex(String asciiValue) {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString() + "".join("", Collections.nCopies(32 - (hex.length() / 2), "00"));
    }

    public String getPassConAddr() {
        List<String> pAddressList = Paper.book().read("pAddressList", new ArrayList<String>());
        if (pAddressList.size() != 0) {
            return pAddressList.get(pAddressList.size()-1);
        } else {
            return "NULL";
        }
    }
}
