package net.yoga.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.yoga.model.Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BootReceiver extends BroadcastReceiver {

    private static final String PREFERENCE_LAST_REQUEST_CODE = "PREFERENCE_LAST_REQUEST_CODE";
    private static final String TAG = "BootReceiver";
    private AlarmHelper alarmHelper;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SimpleDateFormat startTimeFormat;

    public BootReceiver() {}

    private int getNextRequestCode() {
        int j = sharedPreferences.getInt("PREFERENCE_LAST_REQUEST_CODE", 0) + 1;
        int i = j;
        if (j == Integer.MAX_VALUE) {
            i = 0;
        }
        sharedPreferences.edit().putInt("PREFERENCE_LAST_REQUEST_CODE", i).apply();
        return i;
    }

    private PendingIntent getPendingIntent() {
        Intent localIntent = new Intent("com.android.workout.receiver.NOTIFY_ACTION");
        localIntent.setClass(context, NotificationPublisher.class);
        localIntent.setFlags(ErrorDialogData.BINDER_CRASH);
        return PendingIntent.getBroadcast(context, getNextRequestCode(), localIntent, 134217728);
    }

    private void setBootReceiverEnabled(int paramInt) {
        ComponentName localComponentName = new ComponentName(context, BootReceiver.class);
        context.getPackageManager().setComponentEnabledSetting(localComponentName, paramInt, 1);
    }

    public SimpleDateFormat getHourFormat() {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("hh");
        startTimeFormat = localSimpleDateFormat;
        return localSimpleDateFormat;
    }

    public SimpleDateFormat getMinuteFormat() {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("mm");
        startTimeFormat = localSimpleDateFormat;
        return localSimpleDateFormat;
    }

    void getTime(AlarmHelper paramAlarmHelper, Calendar paramCalendar) {
        int i=0;
        int j=0;
        int k=0;
        if (start_TimeFormat().format(paramCalendar.getTime()).endsWith("AM")) {
            i = Integer.parseInt(getHourFormat().format(paramCalendar.getTime()));
            j = Integer.parseInt(getMinuteFormat().format(paramCalendar.getTime()));
            k=0;
        }
        else {
            i = Integer.parseInt(getHourFormat().format(paramCalendar.getTime()));
            j = Integer.parseInt(getMinuteFormat().format(paramCalendar.getTime()));
            k=1;
        }
        init(i, j, k);
    }

    void init(int paramInt1, int paramInt2, int paramInt3) {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.set(Calendar.HOUR_OF_DAY, paramInt1);
        localCalendar.set(Calendar.MINUTE, paramInt2);
        localCalendar.set(Calendar.SECOND, 0);
        localCalendar.set(Calendar.MILLISECOND, 0);
        localCalendar.set(Calendar.AM_PM, paramInt3);
        setAlarm(localCalendar.getTimeInMillis(), getPendingIntent());
    }

    public void onReceive(Context paramContext, Intent paramIntent) {
        String str = TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("onReceive - Intent Action: ");
        localStringBuilder.append(paramIntent.getAction());
        Log.d(str, localStringBuilder.toString());
        context = paramContext;
        try
        {
            setAlarm(paramContext);
            return;
        }
        catch (Exception paramContex1t)
        {
            paramContex1t.printStackTrace();
        }
    }

    void setAlarm(long paramLong, PendingIntent paramPendingIntent) {
        Object localObject = TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("scheduling_PendingIntent: ");
        localStringBuilder.append(paramLong);
        localStringBuilder.append("/");
        localStringBuilder.append(paramPendingIntent);
        Log.d((String)localObject, localStringBuilder.toString());
        localObject = (AlarmManager)context.getSystemService("alarm");
        if (Build.VERSION.SDK_INT >= 23) {
            ((AlarmManager)localObject).setExactAndAllowWhileIdle(0, paramLong, paramPendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            ((AlarmManager)localObject).setExact(0, paramLong, paramPendingIntent);
        } else {
            ((AlarmManager)localObject).set(0, paramLong, paramPendingIntent);
        }
        setBootReceiverEnabled(1);
    }

    public void setAlarm(Context context) throws ParseException {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        List list = (List) new Gson().fromJson(this.sharedPreferences.getString("Reminder_customObjectList", null), new TypeToken() {}.getType());
        if (list != null && list.size() > 0) {
            this.alarmHelper = new AlarmHelper(context);
            Calendar instance = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
            for (int i = 0; i < list.size(); i++) {
                instance.setTime(simpleDateFormat.parse(((Reminder) list.get(i)).getTime().substring(0, 5)));
                getTime(this.alarmHelper, instance);
            }
        }
    }

    public SimpleDateFormat start_TimeFormat()
    {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("hh:mm a");
        startTimeFormat = localSimpleDateFormat;
        return localSimpleDateFormat;
    }
}
