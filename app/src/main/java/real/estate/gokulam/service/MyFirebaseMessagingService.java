package real.estate.gokulam.service;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import real.estate.gokulam.R;
import real.estate.gokulam.chat.chatUtils.BaseApplication;
import real.estate.gokulam.localdatabase.MyDBHandler;
import real.estate.gokulam.localdatabase.pojo.Notificationpojo;
import real.estate.gokulam.utils.SessionManager;
import real.estate.gokulam.views.liveChat.chats1810034.ConnectionManager;
import real.estate.gokulam.views.liveChat.chats1810034.GroupChannelActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "MsgFirebaseServ";
    NotificationManager notificationManager;
    int bundleNotificationId = 100;
    String bundle_notification_id;
    int singleNotificationId = 100;
    Intent resultIntent;
    PendingIntent resultPendingIntent;
    NotificationCompat.Builder summaryNotificationBuilder;
    MyDBHandler myDBHandler;
    static Dialog dialog;
    int i=0;
    String GROUP_ID = "SAMPLE_ID";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            ++i;
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Log.d("sddsds", "" + remoteMessage.getData());
          /*  if (remoteMessage.getData().size() > 0) {
                String title = remoteMessage.getData().get("title");
                String body = remoteMessage.getData().get("body");
                Log.d(TAG, "" + " Log. " + title + " body " + body);
                setBundleNotificationId(title, body);
            }

            if (remoteMessage.getNotification() != null) {
                String body = remoteMessage.getNotification().getBody();
                String title = remoteMessage.getNotification().getTitle();
                Log.d(TAG, "Message Notification Body: " + body + "  " + title);
                //Toast.makeText(this, "Notification  " + body, Toast.LENGTH_SHORT).show();
                //sendNotification(title, body);
                setBundleNotificationId(title, body);
            }*/


            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }

            String channelUrl = null;
            String channelname = null;
            String senderId = null;
            String sendername = null;
            String message = null;
            try {
                JSONObject sendBird = new JSONObject(remoteMessage.getData().get("sendbird"));
                JSONObject channel = (JSONObject) sendBird.get("channel");
                JSONObject sender = (JSONObject) sendBird.get("sender");
                senderId = sender.getString("id");
                sendername = sender.getString("name");

                Log.d("sdsds_sendBird", "" + sendBird);
                Log.d("sdsds+channel", "" + channel);
                channelUrl = (String) channel.get("channel_url");
                channelname = (String) channel.get("name");
                message = sendBird.getString("message");
                if (i == 1) {
                    myDBHandler = new MyDBHandler(getApplicationContext());
                    Notificationpojo notificationpojo = new Notificationpojo(senderId, sendername, channelUrl, message, channelname);
                    if (myDBHandler.addUser(notificationpojo)) {
                        if (!senderId.equals(SessionManager.getMobile(getApplicationContext()))) {
                            getMessagefromdataLocaldatabase();
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getMessagefromdataLocaldatabase() {
        try {
//            connectsendbird();
            ArrayList<Notificationpojo> list = myDBHandler.getAllUsers();
            if (list.size() == 0) {
                setBundleNotificationId(list.get(0).getSendername(), list.get(0).getMessage(), list.get(0).getChannel_url(), list.get(0).getId());
            } else {
                ArrayList<Notificationpojo> notificationpojoArrayList;
                notificationpojoArrayList = myDBHandler.getAllUserschannels();
                Log.d("Table ", "" + notificationpojoArrayList.size());
                setBundleNotificationId(notificationpojoArrayList.get(0).getSendername(), notificationpojoArrayList.get(0).getMessage(), notificationpojoArrayList.get(0).getChannel_url(), notificationpojoArrayList.get(0).getId());
                if (notificationpojoArrayList.size() == 1) {
                    sendsiglenotification(notificationpojoArrayList.get(0).getChannel_url(),0);

                } else {
                    for (int j = 0; j < notificationpojoArrayList.size(); j++) {
                        Log.d("Table ", " position " + j + " " + notificationpojoArrayList.get(j).getMessage());
                        sendsiglenotification(notificationpojoArrayList.get(j).getChannel_url(),j);
                    }

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void setBundleNotificationId(String title, String body, String channelUrl, int id) {
        bundleNotificationId = bundleNotificationId + 12;
        GROUP_ID = GROUP_ID + "" + bundle_notification_id;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannels().size() < 2) {
                NotificationChannel groupChannel = new NotificationChannel("bundle_channel_id", "bundle_channel_name", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(groupChannel);
                NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

        }
        Intent resultIntent = new Intent(this, GroupChannelActivity.class);
        resultIntent.putExtra("groupChannelname", "group");
        resultIntent.putExtra("groupChannelUrl", "group");
        resultIntent.putExtra("groupChannelID", bundleNotificationId);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, bundleNotificationId, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder summaryNotificationBuilder = new NotificationCompat.Builder(this, "bundle_channel_id")
                .setGroup(GROUP_ID)
                .setGroupSummary(true)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.parseColor("#008577"))  // small icon background color
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.private_chats))
//              .setOngoing(true)//not swipe
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
//                .setOngoing(true)//not swipe
//                .setContent(new RemoteViews(getPackageName(),R.layout.dialog_confirmation_permission))
                .setDeleteIntent(resultPendingIntent);


        notificationManager.notify(bundleNotificationId, summaryNotificationBuilder.build());

//        notificationManager.cancel(bundleNotificationId);

    }

    public void sendsiglenotification(String sendername, int i) {
        ArrayList<Notificationpojo> arrayList = myDBHandler.getdata(sendername);
        Intent resultIntent = new Intent(this, GroupChannelActivity.class);
        resultIntent.putExtra("groupChannelUrl", sendername);
        resultIntent.putExtra("groupChannelname", arrayList.get(0).getChannelname());
        resultIntent.putExtra("groupChannelID", i);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, i, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "channel_id")
                .setGroup(GROUP_ID)
                .setContentTitle(arrayList.get(0).getChannelname())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setGroupSummary(false)
                .setColor(Color.parseColor("#008577"))  // small icon background color
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.private_chats))
//                .setOngoing(true)//not swipe
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
//                .setDeleteIntent(createOnDismissedIntent(getApplicationContext(),singleNotificationId));
                .setContentIntent(resultPendingIntent);

        if (arrayList.size() != 0) {
            NotificationCompat.InboxStyle inboxStyle =
                    new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle(arrayList.get(0).getChannelname()+": "+arrayList.get(0).getSendername() +" ("+arrayList.size()+")");
            for (Notificationpojo notificationpojo : arrayList) {
                Log.d("Table ", " position +" + notificationpojo.getMessage());
                inboxStyle.addLine(notificationpojo.getMessage());
            }
            notification.setStyle(inboxStyle);
        }
        notificationManager.notify(i, notification.build());
//        notificationManager.notify(bundleNotificationId, summaryNotificationBuilder.build());


    }


}
