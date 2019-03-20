package real.estate.gokulam.chat.chatUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentActivity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.sendbird.android.SendBird;

public class PushUtils {

    public static void registerPushTokenForCurrentUser(final Context context, SendBird.RegisterPushTokenWithStatusHandler handler) {
        SendBird.registerPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(), handler);
    }

    public static void unregisterPushTokenForCurrentUser(final Context context, SendBird.UnregisterPushTokenHandler handler) {
        SendBird.unregisterPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(), handler);
    }
    public static boolean isNetworkConnected(FragmentActivity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
