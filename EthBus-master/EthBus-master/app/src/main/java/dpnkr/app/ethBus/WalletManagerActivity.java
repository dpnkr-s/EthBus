package dpnkr.app.ethBus;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

public class WalletManagerActivity extends AppCompatActivity {
    Wallet mWallet = new Wallet();
    Button getBalance, create;
    TextView result;
    EditText mPassword;
    TextView mAddress;
    Web3j web3j;
    Credentials credentials;
    BigDecimal balance;
    String ethAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web3j);
        Toast.makeText(this, "Welcome to Ethereum-mobile-wallet", Toast.LENGTH_LONG).show();
        RegisterView();
        checkPermissions();
        addEventListners();
    }

    private void RegisterView() {
        getBalance = (Button) findViewById(R.id.get_balance);
        create = (Button) findViewById(R.id.create);
        result = (TextView) findViewById(R.id.result);
        mPassword = (EditText) findViewById(R.id.password);
        mAddress = (TextView) findViewById(R.id.m_address);
    }

    private void addEventListners() {
        getBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String passwordText = mPassword.getText().toString();
                    Credentials credentials = mWallet.loadCredentials(passwordText);
                    //result.setText(credentials.getAddress() + " Loaded successfully");
                    ethAddr = credentials.getAddress();
                    new LoadBalance().execute("loadBalance");
                } catch (Exception e) {
                    result.setText(e.toString());
                }
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String passwordText = mPassword.getText().toString();
                    String fileName = mWallet.createWallet(passwordText);
                    mAddress.setText(fileName);
                } catch (Exception e) {
                    result.setText(e.toString());
                }
            }
        });
    }

    private class LoadBalance extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Wallet wallet = new Wallet();

            try {
                web3j = wallet.constructWeb3("https://rinkeby.infura.io/v3/8ec6481cb37b48e2beb746659efd2bf3");
                EthGetBalance ethGetBalance = web3j.ethGetBalance(ethAddr, DefaultBlockParameterName.LATEST).sendAsync().get();
                //balance = ethGetBalance.getBalance();
                balance = Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER);
                return "balanceLoaded";
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                // Ether send trasnaction, update balance
                if (response == "balanceLoaded")
                    result.setText(balance.toString());
                else {
                    // Print error
                    result.setText(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void loadDefaultWallet() {
        mAddress.setText("0x833e56c5df2a654372a252658006af4d3158e9f3");
    }

    private void checkPermissions() {
        String TAG = "Permisiion";
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted1");
            } else {
                Log.v(TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted1");
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted2");
            } else {
                Log.v(TAG, "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted2");
        }
    }
}