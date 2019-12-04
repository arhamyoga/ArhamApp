package net.yoga.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.yoga.R;
import net.yoga.activities.MainActivity;
import net.yoga.model.Notification;
import net.yoga.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "ArhamYoga";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor prefsEditor;
    List<Notification> notificationList;
    Gson gson;

    private void createNotificationChannel(Context context) {
        if (VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("fcm_notification", "FCM Notification", 3);
            notificationChannel.setDescription("Include all the notifications");
            ((NotificationManager) context.getSystemService("notification")).createNotificationChannel(notificationChannel);
        }
    }

    private void sendNotification(String str, String str2, String str3, String str4) {
        createNotificationChannel(getApplicationContext());
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.APP_PACKAGE_NAME, str);
        intent.putExtra(Constants.APP_BANNER_URL, str2);
        intent.addFlags(67108864);
        Log.e("Notification",str+str2+str3+str4);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 1073741824);
        String string = "fcm_default_channel";
        Notification notification = new Notification();
        notification.setNotificationTitle(str4);
        notification.setNotificationContent(str3);
        Log.e("Notification list",notification.getNotificationContent());
        notificationList.add(notification);
        String toJson = gson.toJson(notificationList);
        prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString("Notifications_customObjectList",toJson);
        prefsEditor.apply();
        Builder contentIntent = new Builder(this, string).setSmallIcon(R.drawable.icon_notification).setContentTitle(str4).setContentText(str3).setAutoCancel(true).setSound(RingtoneManager.getDefaultUri(2)).setContentIntent(activity);
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        if (VERSION.SDK_INT >= 26) {
            notificationManager.createNotificationChannel(new NotificationChannel(string, "Channel human readable title", 3));
        }
        notificationManager.notify(0, contentIntent.build());
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("From: ");
        stringBuilder.append(remoteMessage.getFrom());
        Log.d(str, stringBuilder.toString());
        String str2 = null;
        Log.d("FCM",remoteMessage.getData()+"");
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        gson = new Gson();
        notificationList = gson.fromJson(mSharedPreferences.getString("Notifications_customObjectList",null), new CustomType(this).getType());
        if(notificationList == null || notificationList.size()<=0){
            notificationList = new ArrayList<>();
        }
        if (remoteMessage.getData().size() > 0) {
            str = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Message data payload: ");
            stringBuilder2.append(remoteMessage.getData());
            Log.d(str, stringBuilder2.toString());
            Map data = remoteMessage.getData();
            if (data.containsKey(Constants.APP_PACKAGE_NAME)) {
                str2 = (String) data.get(Constants.APP_PACKAGE_NAME);
                str = (String) data.get(Constants.APP_BANNER_URL);
                if (remoteMessage.getNotification() != null) {
                    String str3 = TAG;
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Message Notification Body: ");
                    stringBuilder3.append(remoteMessage.getNotification().getBody());
                    Log.d(str3, stringBuilder3.toString());
                }
                sendNotification(str2, str, remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
            }
        }
        str = null;
        if (remoteMessage.getNotification() != null) {
            String str32 = TAG;
            StringBuilder stringBuilder32 = new StringBuilder();
            stringBuilder32.append("Message Notification Body: ");
            stringBuilder32.append(remoteMessage.getNotification().getBody());
            Log.d(str32, stringBuilder32.toString());
        }
        sendNotification(str2, str, remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
    }
    class CustomType extends TypeToken<List<Notification>>{
        final MyFirebaseMessagingService myFirebaseMessagingService;

        CustomType(MyFirebaseMessagingService myFirebaseMessagingService) {
            this.myFirebaseMessagingService = myFirebaseMessagingService;
        }
    }
}
