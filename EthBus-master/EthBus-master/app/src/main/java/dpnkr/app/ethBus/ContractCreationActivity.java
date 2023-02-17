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

public class ContractCreationActivity extends AppCompatActivity {
    Button createContract, cancelOperation;
    TextView displayText;
    EditText username, phoneNumber, fiscalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_creation);
        Toast.makeText(this, "Welcome to contract creation activity", Toast.LENGTH_LONG).show();
        RegisterView();
        checkPermissions();
        addEventListners();
    }

    private void RegisterView() {
        createContract = (Button) findViewById(R.id.button_to_create);
        cancelOperation = (Button) findViewById(R.id.button_to_cancel);
        displayText = (TextView) findViewById(R.id.display_text);
        username = (EditText) findViewById(R.id.username);
        phoneNumber = (EditText) findViewById(R.id.phone_no);
        fiscalCode = (EditText) findViewById(R.id.fiscal_code);
    }

    private void addEventListners() {
        createContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent tranIntent = new Intent(ContractCreationActivity.this,
                            EthTransactionActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("Username", username.getText().toString());
                    extras.putString("PhoneNumber", phoneNumber.getText().toString());
                    extras.putString("FiscalCode", fiscalCode.getText().toString());
                    extras.putString("TransactionType", "createPassengerContract");
                    tranIntent.putExtras(extras);
                    startActivity(tranIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cancelOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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