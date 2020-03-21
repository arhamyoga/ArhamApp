package net.yoga.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat.BigPictureStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;


import net.yoga.R;
import net.yoga.activities.MainActivity;

import java.util.Random;

public class NotificationReceiver extends BroadcastReceiver {
    private final String CHANNEL_ID = "reminder_notification";

    Context f3389a;

    private void createNotificationChannel(Context context) {
        if (VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("reminder_notification", "Reminder Notification", 3);
            notificationChannel.setDescription("Include all the notifications");
            ((NotificationManager) context.getSystemService("notification")).createNotificationChannel(notificationChannel);
        }
    }

    public void onReceive(Context context, Intent intent) {
        this.f3389a = context;
        createNotificationChannel(context);
        Log.d("TAG", "Notification in receiver came");
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), R.drawable.yoga_new);
        ((NotificationManager) context.getSystemService("notification")).
                notify(100, new Builder(context, "reminder_notification").
                        setContentIntent(PendingIntent.getActivity(context, 100, new Intent(context, MainActivity.class),
                134217728)).setSmallIcon(R.drawable.icon_notification).setContentTitle("Arham time")
                        .setContentText("Arham Dhyan")
                        .setStyle(new BigPictureStyle()
                                .bigPicture(decodeResource).setBigContentTitle("Let's start Arham")
                                .setSummaryText("Arham Brings mental peace")).setAutoCancel(true).build());
    }
}
