package net.yoga.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class DailyBrodcast extends BroadcastReceiver {
    private Context context;

    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")) {
            setAlarm();
        }
    }

//    public void setAlarm() {
//        Calendar instance = Calendar.getInstance();
//        instance.set(11, 7);
//        instance.set(12, 30);
//        instance.set(13, 0);
//        ((AlarmManager) this.context.getSystemService(NotificationCompat.CATEGORY_ALARM))
//                .setRepeating(0, instance.getTimeInMillis(), 86400000,
//                        PendingIntent.getBroadcast(this.context, 100, new Intent(this.context, NotificationReceiver.class), 134217728));
//    }
public void setAlarm()
{
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.set(Calendar.HOUR_OF_DAY, 7);
    localCalendar.set(Calendar.MINUTE, 30);
    localCalendar.set(Calendar.SECOND, 0);
    Object localObject = new Intent(context, NotificationReceiver.class);
    localObject = PendingIntent.getBroadcast(context, 100, (Intent)localObject, 134217728);
    ((AlarmManager)context.getSystemService("alarm")).setRepeating(0, localCalendar.getTimeInMillis(), 86400000L, (PendingIntent)localObject);
}
}

