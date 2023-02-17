package dpnkr.app.ethBus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;

import java.util.ArrayList;

import io.paperdb.Paper;

public class TransitionBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (ActivityTransitionResult.hasResult(intent)) {
            ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);
            if (result != null) {
                for (ActivityTransitionEvent event : result.getTransitionEvents()) {
                    int activityType = event.getActivityType();
                    long activityTimestamp = event.getElapsedRealTimeNanos();
                    int transitionType = event.getTransitionType();
                    long activityTimeSeconds = activityTimestamp/1000000000;

                    if (activityType == 3 && transitionType == 0) {
                        //get location and time at the start of journey
                        //Intent intentNew = new Intent(context, PaymentRequestPrompt.class);
                        //intentNew.putExtra("ACTION","START");
                        //context.startActivity(intentNew);
                        System.out.println("Activity Timestamp (milli): " + activityTimestamp/1000000);

                    } else if (activityType == 3 && transitionType == 1) {
                        //get location time at the end of journey
                        //Intent intentNew = new Intent(context, PaymentRequestPrompt.class);
                        //intentNew.putExtra("ACTION","STOP");
                        //context.startActivity(intentNew);
                    }
                    saveActivity(new ActivityTransitionEventWrapper(event));
                    //Toast.makeText(context, strFinal, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void saveActivity(final ActivityTransitionEventWrapper event) {
        ArrayList<ActivityTransitionEventWrapper> events = Paper.book().read("activities", new ArrayList<ActivityTransitionEventWrapper>());
        events.add(event);
        Paper.book().write("activities", events);
    }
}

