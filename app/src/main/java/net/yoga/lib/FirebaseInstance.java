package net.yoga.lib;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by nayan on 8/12/17.
 */

public class FirebaseInstance {

    private static FirebaseAnalytics fbInstance;
    private static String androidDeviceId;

    public synchronized static void initialize(Context context) {
        if (fbInstance == null) {
            fbInstance = FirebaseAnalytics.getInstance(context);
            androidDeviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            // TODO log duplicate initialize called
        }
    }

    public static void logArhamAppStarted() {
        logEvent("Arham.AppStarted", androidDeviceId);
    }

    public static void logArhamSessionStarted() {
        logEvent("Arham.SessionStarted", androidDeviceId);
    }

    public static void logArhamSessionCompleted() {
        logEvent("Arham.SessionCompleted", androidDeviceId);
    }

    private static void logEvent(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(id, name);
        bundle.putString("deviceId", androidDeviceId);
        fbInstance.logEvent("Arham", bundle);
    }
}
