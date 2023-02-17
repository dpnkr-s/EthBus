package dpnkr.app.ethBus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class JSONRequestToGTT extends AppCompatActivity {

    String url = "https://gpa.madbob.org/query.php?stop=1940"; //hardcoded for fermata spalato

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moveTaskToBack(true);

        //Button btnHit = findViewById(R.id.btnHit);
        //final TextView mTextView = (TextView) findViewById(R.id.text);
        //final EditText inpTxt = findViewById(R.id.inpTxt);
        //final TextView labelTxt = findViewById(R.id.labelTxt);

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        //String stop = inpTxt.getText().toString();
        //String url = gttApi + stop;

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(JSONRequestToGTT.this, "Response" + response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(JSONRequestToGTT.this, "Oops! That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }
}
