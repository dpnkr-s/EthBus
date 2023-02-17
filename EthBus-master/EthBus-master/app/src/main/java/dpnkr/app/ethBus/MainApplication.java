package dpnkr.app.ethBus;

import android.app.Application;

import io.paperdb.Paper;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
    }


}
