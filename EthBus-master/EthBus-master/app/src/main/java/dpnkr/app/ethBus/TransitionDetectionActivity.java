package dpnkr.app.ethBus;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.paperdb.Paper;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class TransitionDetectionActivity extends AppCompatActivity {

    Context context = TransitionDetectionActivity.this;
/*
    public TransitionDetectionActivity(Context context) {
        this.context = context.getApplicationContext();
    }
*/

    String ethAddr, password;
    TextView homeView, homeViewPrev, homeViewPPrev;
    TextView startLoc, endLoc;
    CountDownTimer countDownTimer;
    long prevTimeStamp = 0;
    private FusedLocationProviderClient mLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    public String startLocation, endLocation;
    boolean statusFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting UI views
        setContentView(R.layout.transition_activity);
        homeView = findViewById(R.id.home_view);
        homeViewPrev = findViewById(R.id.home_view_prev);
        homeViewPPrev = findViewById(R.id.home_view_Pprev);
        startLoc = findViewById((R.id.start_loc));
        endLoc = findViewById((R.id.end_loc));

        ethAddr = getIntent().getExtras().getString("mAddress");
        password = getIntent().getExtras().getString("mPassword");

        ActivityCompat.requestPermissions(TransitionDetectionActivity.this, new String[]{ACCESS_FINE_LOCATION}, 1);

        Intent recIntent = new Intent(context, TransitionBroadcastReceiver.class);

        PendingIntent pendingIntentBroadcast = PendingIntent.getBroadcast(context, 0, recIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        List<ActivityTransition> transitions = getTransitionActivityList();
        ActivityTransitionRequest request = new ActivityTransitionRequest(transitions);

        startGetBroadcast(pendingIntentBroadcast, request, "pendingIntentBroadcast");
        refresh();
    }

    public void refresh() {
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            // This is called after every 1 sec interval.
            public void onTick(long millisUntilFinished) {
                ArrayList<ActivityTransitionEventWrapper> events = Paper.book().read("activities", new ArrayList<ActivityTransitionEventWrapper>());
                if (events.size() > 2) {
                    //ActivityTransitionEventWrapper currentEvent = Iterables.getLast(events);
                    String dispText1 = "Latest Event data:= \nActivity Type: " + events.get(events.size() - 1).getTypeOfActivity() + "\nTransition Type: " + events.get(events.size() - 1).getTypeOfTransition() + "\nTimestamp: " + events.get(events.size() - 1).getEventTime().toString();
                    String dispText2 = "Previous Event data:= \nActivity Type: " + events.get(events.size() - 2).getTypeOfActivity() + "\nTransition Type: " + events.get(events.size() - 2).getTypeOfTransition() + "\nTimestamp: " + events.get(events.size() - 2).getEventTime().toString();
                    //String dispText3 = "Event data : " + events.get(events.size() - 3).getTypeOfActivity() + "\n" + events.get(events.size() - 3).getTypeOfTransition() + "\n" + events.get(events.size() - 3).getEventTime().toString();
                    String ActType = events.get(events.size() - 1).getTypeOfActivity();
                    String TranType = events.get(events.size() - 1).getTypeOfTransition();
                    String LastActType = events.get(events.size() - 2).getTypeOfActivity();
                    String LastTranType = events.get(events.size() - 2).getTypeOfTransition();

                    if (events.get(events.size() - 1).getEventTime() > (prevTimeStamp + 5)) {
                        //Toast.makeText(context, "WE ARE HERE", Toast.LENGTH_SHORT).show();
                        if (ActType.equals("IN_VEHICLE") && TranType.equals("TRANSITION_ENTER")) {
                            startLocationUpdate();
                            if (statusFlag == false) {
                                statusFlag = true;
                                Intent intentPay = new Intent(context, PaymentRequestPrompt.class);
                                Bundle extras = new Bundle();
                                List<String> locations = Paper.book().read("locationHashList", new ArrayList<String>());
                                if (locations.size() != 0) {
                                    //System.out.println(locations.get(locations.size()-1));
                                    extras.putString("Location", locations.get(locations.size() - 1));
                                } else {
                                    extras.putString("Location", "NULL");
                                }
                                //extras.putString("Location", "35F00BAEDBCD7B46A5A4A071698788B12E57029FEB89F5FEB1F6DFAFE10FA1DF");
                                extras.putString("Time", events.get(events.size() - 1).getEventTime().toString());
                                intentPay.putExtras(extras);
                                startActivity(intentPay);
                            }
                        }
                        if (LastActType.equals("IN_VEHICLE") && LastTranType.equals("TRANSITION_EXIT")) {
                            endLocationUpdate();
                            if (statusFlag == true) {
                                Intent intentTransac = new Intent(context, EthTransactionActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("TransactionType", "updateTravelStruct");
                                List<String> locations = Paper.book().read("locationHashList", new ArrayList<String>());
                                if (locations.size() != 0) {
                                    extras.putString("Location", locations.get(locations.size()-1));
                                } else {
                                    extras.putString("Location", "NULL");
                                }
                                //extras.putString("Location", "A072BE18EB512CBF2F9E8EA27113B555C5A0FD58FF7C0757FC68D810C85490DD");
                                extras.putString("Time", events.get(events.size() - 1).getEventTime().toString());
                                intentTransac.putExtras(extras);
                                statusFlag = false;
                                startActivity(intentTransac);
                            }
                        }
                        prevTimeStamp = events.get(events.size() - 1).getEventTime();
                    }

                    homeView.setText(dispText1);
                    homeViewPrev.setText(dispText2);
                }
                homeViewPPrev.setText("Previous Event Timestamp: " + Long.toString(prevTimeStamp));
            }

            public void onFinish() {
                start();
            }
        }.start();
    }

    private void startGetBroadcast(PendingIntent pendingIntent, ActivityTransitionRequest request, final String type) {
        // myPendingIntent is the instance of PendingIntent where the app receives callbacks.
        Task<Void> task = ActivityRecognition.getClient(context).requestActivityTransitionUpdates(request, pendingIntent);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(context, "Waiting for Activity Transitions...", Toast.LENGTH_LONG).show();
            }
        });
        task.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Toast.makeText(context, "onComplete " + type, Toast.LENGTH_SHORT).show();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(context, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    int[] detectedActivity = new int[]{
            DetectedActivity.IN_VEHICLE,
            DetectedActivity.ON_BICYCLE,
            // DetectedActivity.ON_FOOT,
            DetectedActivity.RUNNING,
            DetectedActivity.STILL,
            // DetectedActivity.TILTING,
            // DetectedActivity.UNKNOWN,
            DetectedActivity.WALKING};

    private List<ActivityTransition> getTransitionActivityList() {
        List<ActivityTransition> transitions = new ArrayList<>();
        for (int activity : detectedActivity) {
            transitions.add(
                    new ActivityTransition.Builder()
                            .setActivityType(activity)
                            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                            .build());
            transitions.add(
                    new ActivityTransition.Builder()
                            .setActivityType(activity)
                            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                            .build());
        }
        return transitions;
    }

    private void askForPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askForPermission();
        }
        //Starting Location Service
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(100); // 100 for high accuracy, 102 for balanced block-level accuracy
        mLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                Location location = locationResult.getLastLocation();
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                long timeLoc = location.getTime()/1000;
                String Loc = "StartLoc:= Latitude: " + Double.toString(lat) + ", Longitude: " + Double.toString(lon);
                startLoc.setText(Loc);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    startLocation = Hashing.sha256()
                            .hashString(Loc + ", EthereumAddr: " + ethAddr, StandardCharsets.UTF_8)
                            .toString();
                }
                System.out.println(Loc + ", EthereumAddr: " + ethAddr);
                System.out.println(startLocation);
                saveData(startLocation);
            }
        };
        //start listening to location updates with given priority settings
        mLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback, null);
    }

    private void endLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askForPermission();
        }
        //Starting Location Service
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(100); // 100 for high accuracy, 102 for balanced block-level accuracy
        mLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                Location location = locationResult.getLastLocation();
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                long timeLoc = location.getTime()/1000; //divided by 1000 to obtain timestamp in seconds
                String Loc = "EndLoc:= Latitude: " + Double.toString(lat) + ", Longitude: " + Double.toString(lon);
                endLoc.setText(Loc);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    endLocation = Hashing.sha256()
                            .hashString(Loc + ", EthereumAddr: " + ethAddr, StandardCharsets.UTF_8)
                            .toString();
                }
                saveData(endLocation);
            }
        };
        //start listening to location updates with given priority settings
        mLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback, null);
    }

    private void stopLocationUpdates() {
        mLocationClient.removeLocationUpdates(mLocationCallback);
    }

    public void saveData(String hashedLoc) {
        List<String> locations = Paper.book().read("locationHashList", new ArrayList<String>());
        locations.add(hashedLoc);
        Paper.book().write("locationHashList", locations);
    }
}
