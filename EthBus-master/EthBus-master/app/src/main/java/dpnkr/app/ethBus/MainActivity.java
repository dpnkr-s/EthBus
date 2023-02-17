package dpnkr.app.ethBus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class MainActivity extends AppCompatActivity {
    Wallet mWallet = new Wallet();
    Button startDetection, goToWallet, createPassengerContract, depositSum;
    TextView displayText, result;
    EditText ethValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Welcome!", Toast.LENGTH_LONG).show();
        RegisterView();
        checkPermissions();
        addEventListners();
    }

    private void RegisterView() {
        startDetection= (Button) findViewById(R.id.start_detection);
        goToWallet = (Button) findViewById(R.id.access_wallet);
        displayText = (TextView) findViewById(R.id.textView);
        result = (TextView) findViewById(R.id.result);
        createPassengerContract = (Button) findViewById(R.id.create_passenger_contract);
        depositSum = (Button) findViewById(R.id.deposit_sum);
        ethValue = (EditText) findViewById(R.id.eth_val);
    }

    private void addEventListners() {
        startDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String passwordText = "aniabhi"; //load default wallet
                    Credentials credentials = mWallet.loadCredentials(passwordText);
                    //Skip geofence to start transition detection directly

                    //Intent fenceIntent = new Intent(MainActivity.this,
                    //        GeofencingActivity.class);
                    //startActivity(fenceIntent);
                    Intent tranIntent = new Intent(MainActivity.this,
                            TransitionDetectionActivity.class);
                    tranIntent.putExtra("mAddress", credentials.getAddress());
                    tranIntent.putExtra("mPassword", passwordText);
                    startActivity(tranIntent);
                } catch (Exception e) {
                    result.setText(e.toString());
                }
            }
        });

        goToWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent walletIntent = new Intent(MainActivity.this,
                            WalletManagerActivity.class);
                    startActivity(walletIntent);
                } catch (Exception e) {
                    result.setText(e.toString());
                }
            }
        });

        createPassengerContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent tranIntent = new Intent(MainActivity.this,
                        ContractCreationActivity.class);
                    startActivity(tranIntent);
                } catch (Exception e) {
                    result.setText(e.toString());
                }
            }
        });

        depositSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String passwordText = "aniabhi"; //load default wallet
                    Credentials credentials = mWallet.loadCredentials(passwordText);
                    String ethVal = ethValue.getText().toString();
                    Intent tranIntent = new Intent(MainActivity.this,
                            EthTransactionActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("TransactionType", "depositSum");
                    extras.putString("EthereumValue", ethVal);
                    tranIntent.putExtras(extras);
                    startActivity(tranIntent);
                } catch (Exception e) {
                    result.setText(e.toString());
                }
            }
        });
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
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
