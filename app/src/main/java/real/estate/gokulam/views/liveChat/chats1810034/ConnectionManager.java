package real.estate.gokulam.views.liveChat.chats1810034;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import real.estate.gokulam.chat.chatUtils.BaseApplication;
import real.estate.gokulam.utils.SessionManager;

import static android.provider.UserDictionary.Words.APP_ID;


public class ConnectionManager {
    static String TAG = "ConnectSENDBIrd";
    boolean isConnected;
     Context activity;
    ConnectionManager(Context activity ){
        this.activity =activity;
    }

    public static void inti(FragmentActivity activity) {
        SendBird.init(APP_ID, activity);
    }

    public static void login(String userId, final SendBird.ConnectHandler handler) {

        SendBird.connect(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (handler != null) {
                    handler.onConnected(user, e);
                }
            }
        });
    }

    public static void logout(final SendBird.DisconnectHandler handler) {
        SendBird.disconnect(new SendBird.DisconnectHandler() {
            @Override
            public void onDisconnected() {
                if (handler != null) {
                    handler.onDisconnected();
                }
            }
        });
    }

    public static void addConnectionManagementHandler(String handlerId, final ConnectionManagementHandler handler) {

        SendBird.addConnectionHandler(handlerId, new SendBird.ConnectionHandler() {
            @Override
            public void onReconnectStarted() {

            }

            @Override
            public void onReconnectSucceeded() {
                if (handler != null) {
                    handler.onConnected(true);
                }
            }

            @Override
            public void onReconnectFailed() {

            }
        });

        if (SendBird.getConnectionState() == SendBird.ConnectionState.OPEN) {
            if (handler != null) {
                handler.onConnected(false);
            }
        } else if (SendBird.getConnectionState() == SendBird.ConnectionState.CLOSED) { // push notification or system kill
        /*    String userId = PreferenceUtils.getUserId();
            SendBird.connect(userId, new SendBird.ConnectHandler() {
                @Override
                public void onConnected(User user, SendBirdException e) {
                    if (e != null) {
                        return;
                    }

                    if (handler != null) {
                        handler.onConnected(false);
                    }
                }
            });*/
        }
    }

    public static void removeConnectionManagementHandler(String handlerId) {
        SendBird.removeConnectionHandler(handlerId);
    }

    public interface ConnectionManagementHandler {
        /**
         * A callback for when connected or reconnected to refresh.
         *
         * @param reconnect Set false if connected, true if reconnected.
         */
        void onConnected(boolean reconnect);
    }

    public boolean connectsendbird() {

        try {
            SendBird.init(BaseApplication.APP_ID, activity);
            ConnectionManager.login(SessionManager.getMobile(activity), new SendBird.ConnectHandler() {
                @Override
                public void onConnected(User user, SendBirdException e) {
                    if (e != null) {
                        Log.d("Connect", "" + e);
                        isConnected = false;
                    } else {
                        isConnected = true;
                    }
                }
            });
        } catch (Exception cv) {
            cv.printStackTrace();
        }
        return isConnected;
    }
}
