package net.yoga.receiver;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.internal.view.SupportMenu;
import android.util.Log;

import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.yoga.R;
import net.yoga.activities.MainActivity;
import net.yoga.model.Reminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NotificationPublisher extends BroadcastReceiver {
    private static final String PREFERENCE_LAST_NOTIF_ID = "PREFERENCE_LAST_NOTIF_ID";
    static final boolean f3360a = true;
    private String TAG = "NotificationPublisher";
    private AlarmHelper alarmHelper;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SimpleDateFormat startTimeFormat;


    class C15111 extends TypeToken<List<Reminder>> {

        final NotificationPublisher f5150d;

        C15111(NotificationPublisher notificationPublisher) {
            this.f5150d = notificationPublisher;
        }
    }

    private int getNextNotifId() {
        int i = this.sharedPreferences.getInt(PREFERENCE_LAST_NOTIF_ID, 0) + 1;
        if (i == Integer.MAX_VALUE) {
            i = 0;
        }
        this.sharedPreferences.edit().putInt(PREFERENCE_LAST_NOTIF_ID, i).apply();
        return i;
    }

    private void startNotification(Context context) {
        PendingIntent existAlarm = this.alarmHelper.existAlarm(this.sharedPreferences.getInt(PREFERENCE_LAST_NOTIF_ID, 0));
        if (existAlarm != null) {
            existAlarm.cancel();
        }
        if (VERSION.SDK_INT < 26) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            Intent intent = new Intent(context, MainActivity.class);
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addFlags(ErrorDialogData.BINDER_CRASH);
            Notification build = new Builder(context).setContentIntent(PendingIntent.getActivity(context, getNextNotifId(), intent, 0))
                    .setAutoCancel(true).setWhen(System.currentTimeMillis()).setSmallIcon(R.drawable.icon_notification)
                    .setContentTitle("Hey! it's Workout time").setContentText("Let's lose some weight today.").setDefaults(1).build();
            if (f3360a || notificationManager != null) {
                notificationManager.notify(getNextNotifId(), build);
                return;
            }
            throw new AssertionError();
        }
        Intent intent2 = new Intent(context, MainActivity.class);
        intent2.setAction("android.intent.action.MAIN");
        intent2.addCategory("android.intent.category.LAUNCHER");
        intent2.addFlags(ErrorDialogData.BINDER_CRASH);
        NotificationManager notificationManager2 = (NotificationManager) context.getSystemService("notification");
        String str = "my_channel_id_01";
        NotificationChannel notificationChannel = new NotificationChannel(str, "My Notifications", 4);
        notificationChannel.setDescription("Channel description");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
        notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
        notificationChannel.enableVibration(true);
        if (f3360a || notificationManager2 != null) {
            notificationManager2.createNotificationChannel(notificationChannel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, str);
            builder.setAutoCancel(true).setContentIntent(PendingIntent.getActivity(context, getNextNotifId(), intent2, 0))
                    .setDefaults(-1).setAutoCancel(true).setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.icon_notification).setContentTitle("Hey! it's Workout time")
                    .setContentText("Let's lose some weight today.").setDefaults(1);
            notificationManager2.notify(getNextNotifId(), builder.build());
            return;
        }
        throw new AssertionError();
    }

    void m2551a(AlarmHelper alarmHelper, Calendar calendar) {
        int parseInt;
        int parseInt2;
        int i;
        String str = this.TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setTimeHrAndMin   ");
        stringBuilder.append(startTimeFormat().format(calendar.getTime()));
        Log.d(str, stringBuilder.toString());
        if (startTimeFormat().format(calendar.getTime()).equalsIgnoreCase("AM")) {
            parseInt = Integer.parseInt(getHourFormat().format(calendar.getTime()));
            parseInt2 = Integer.parseInt(getMinuteFormat().format(calendar.getTime()));
            i = 0;
        } else if (startTimeFormat().format(calendar.getTime()).equalsIgnoreCase("PM")) {
            parseInt = Integer.parseInt(getHourFormat().format(calendar.getTime()));
            parseInt2 = Integer.parseInt(getMinuteFormat().format(calendar.getTime()));
            i = 1;
        } else {
            return;
        }
        alarmHelper.schedulePendingIntent(parseInt, parseInt2, i, 86400000);
    }

    public SimpleDateFormat getHourFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh", Locale.ENGLISH);
        this.startTimeFormat = simpleDateFormat;
        return simpleDateFormat;
    }

    public SimpleDateFormat getMinuteFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm", Locale.ENGLISH);
        this.startTimeFormat = simpleDateFormat;
        return simpleDateFormat;
    }

    public void onReceive(Context context, Intent intent) {
        String str = this.TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onReceive1 ==========");
        stringBuilder.append(intent.getAction());
        Log.d(str, stringBuilder.toString());
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        List list = (List) new Gson().fromJson(this.sharedPreferences.getString("Reminder_customObjectList", null), new C15111(this).getType());
        Calendar instance = Calendar.getInstance();
        instance.get(Calendar.HOUR_OF_DAY);
        instance.get(Calendar.MINUTE);
        int i = instance.get(Calendar.DAY_OF_WEEK);
        if (list != null && list.size() > 0) {
            this.alarmHelper = new AlarmHelper(context);
            int i2 = 0;
            while (i2 < list.size()) {
                if (((Reminder) list.get(i2)).getTime().equals(startTimeFormat().format(instance.getTime())) && ((Reminder) list.get(i2)).getOnTime()) {
                    if (!(((Reminder) list.get(i2)).getSunday() && i == 1)) {
                        if (!((Reminder) list.get(i2)).getMonday() || i != 2) {
                            if (!((Reminder) list.get(i2)).getTuesday() || i != 3) {
                                if (!((Reminder) list.get(i2)).getWednesday() || i != 4) {
                                    if (!((Reminder) list.get(i2)).getThurday() || i != 5) {
                                        if (!((Reminder) list.get(i2)).getFriday() || i != 6) {
                                            if (((Reminder) list.get(i2)).getSaturday() && i == 7) {
                                            }
                                            m2551a(this.alarmHelper, instance);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    startNotification(context);
                    m2551a(this.alarmHelper, instance);
                }
                i2++;
            }
            String action = intent.getAction();
            action.getClass();
            if (action.equals("android.intent.action.TIME_SET")) {
                int i3 = 0;
                while (i3 < list.size()) {
                    String str2 = this.TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("onReceive: +++++++++++++");
                    stringBuilder.append(((Reminder) list.get(i3)).getTime());
                    Log.d(str2, stringBuilder.toString());
                    if (!(((Reminder) list.get(i3)).getTime().toUpperCase().endsWith("AM") || ((Reminder) list.get(i3)).getTime().toLowerCase().endsWith("am") || ((Reminder) list.get(i3)).getTime().toUpperCase().endsWith("a.m."))) {
                        if (!((Reminder) list.get(i3)).getTime().toLowerCase().endsWith("a.m.")) {
                            if (((Reminder) list.get(i3)).getTime().toUpperCase().endsWith("PM") || ((Reminder) list.get(i3)).getTime().toUpperCase().endsWith("pm") || ((Reminder) list.get(i3)).getTime().toUpperCase().endsWith("p.m.") || ((Reminder) list.get(i3)).getTime().toLowerCase().endsWith("p.m.")) {
                                this.alarmHelper.schedulePendingIntent(Integer.parseInt(((Reminder) list.get(i3)).getTime().substring(0, 2)), Integer.parseInt(((Reminder) list.get(i3)).getTime().substring(3, 5)), 1);
                                i3++;
                            } else {
                                i3++;
                            }
                        }
                    }
                    this.alarmHelper.schedulePendingIntent(Integer.parseInt(((Reminder) list.get(i3)).getTime().substring(0, 2)), Integer.parseInt(((Reminder) list.get(i3)).getTime().substring(3, 5)), 0);
                    i3++;
                }
            }
        }
    }

    public SimpleDateFormat startTimeFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        this.startTimeFormat = simpleDateFormat;
        return simpleDateFormat;
    }
}
