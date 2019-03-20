package real.estate.gokulam.chat.chatUtils;


import android.app.Application;

import com.sendbird.android.SendBird;

public class BaseApplication extends Application {

//    public static final String APP_ID = "6E2CB85B-E565-4CD8-9C40-8CA2DA87E677"; // US-1 Demo
    public static final String APP_ID = "2BD7BE55-AEFE-46A3-81EA-FF186CBE291F"; // US-1 Demo
    public static final String VERSION = "3.0.40";

    @Override
    public void onCreate() {
        super.onCreate();
        //PreferenceUtils.init(getBaseContext());

//        SendBird.init(getString(R.string.send_bird_app_id), getApplicationContext());
        SendBird.init(APP_ID, getApplicationContext());
    }
}
