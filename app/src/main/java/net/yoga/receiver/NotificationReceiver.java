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
    /* renamed from: a */
    Context f3389a;
    /* renamed from: b */
    String[] f3390b;
    /* renamed from: c */
    String[] f3391c;
    /* renamed from: d */
    String f3392d;
    /* renamed from: e */
    String f3393e;

    private void createNotificationChannel(Context context) {
        if (VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("reminder_notification", "Reminder Notification", 3);
            notificationChannel.setDescription("Include all the notifications");
            ((NotificationManager) context.getSystemService("notification")).createNotificationChannel(notificationChannel);
        }
    }

    public void onReceive(Context context, Intent intent) {
        this.f3389a = context;
//        this.f3390b = new String[]{this.f3389a.getString(R.string.local1), this.f3389a.getString(R.string.local2), this.f3389a.getString(R.string.local3)};
//        this.f3391c = new String[]{this.f3389a.getString(R.string.local4), this.f3389a.getString(R.string.local5), this.f3389a.getString(R.string.local6)};
//        this.f3392d = this.f3390b[new Random().nextInt(this.f3390b.length)];
//        this.f3393e = this.f3391c[new Random().nextInt(this.f3391c.length)];
        createNotificationChannel(context);
        Log.d("TAG", "Notification in receiver came");
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), R.drawable.yoga_new);
        ((NotificationManager) context.getSystemService("notification")).
                notify(100, new Builder(context, "reminder_notification").
                        setContentIntent(PendingIntent.getActivity(context, 100, new Intent(context, MainActivity.class),
                134217728)).setSmallIcon(R.drawable.icon_notification).setContentTitle("Yoga time")
                        .setContentText(this.f3392d)
                        .setStyle(new BigPictureStyle()
                                .bigPicture(decodeResource).setBigContentTitle("Let's start yoga")
                                .setSummaryText(this.f3393e)).setAutoCancel(true).build());
    }
}
