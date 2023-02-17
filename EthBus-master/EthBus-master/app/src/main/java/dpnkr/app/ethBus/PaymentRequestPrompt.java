package dpnkr.app.ethBus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PaymentRequestPrompt extends Activity {

    Context context = PaymentRequestPrompt.this;
    TextView tvLocation;
    Button payTicket, noPayTicket;
    String locationHash;
    String timestamp;

    Wallet mWallet = new Wallet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        tvLocation = findViewById(R.id.tvlocation);
        payTicket = findViewById(R.id.pay_button);
        noPayTicket = findViewById(R.id.no_pay_button);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        //locationHash = extras.getString("Location");
        locationHash = extras.getString("Location");
        timestamp = extras.getString("Time");
        tvLocation.setText("Current location hash value: \n" + locationHash);

        payTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String passwordText = "aniabhi";
                //Credentials credentials = null;
                //try {
                //    credentials = mWallet.loadCredentials(passwordText);
                //    tvLocation.setText("Ethereum Wallet Address: " + credentials.getAddress());
                //} catch (Exception e) {
                //    e.printStackTrace();
                //}

                Intent intentTransac = new Intent(context, EthTransactionActivity.class);
                Bundle extras = new Bundle();
                //extras.putString("mAddress", credentials.getAddress());
                //extras.putString("mPassword", passwordText);
                extras.putString("TransactionType", "payTicketFee");
                extras.putString("Location", locationHash);
                extras.putString("Time", timestamp);
                intentTransac.putExtras(extras);
                startActivity(intentTransac);
            }
        });

        noPayTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Finish this activity
                finish();
            }
        });

    }
}