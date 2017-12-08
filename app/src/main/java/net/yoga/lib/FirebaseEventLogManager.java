package net.yoga.lib;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by nayan on 8/12/17.
 */

public class FirebaseEventLogManager {

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
        logEvent("arham_app_started");
    }

    public static void logArhamSessionStarted() {
        logEvent("arham_session_started");
    }

    public static void logArhamSessionCompleted() {
        logEvent("arham_session_completed");
    }

    private static void logEvent(String eventName) {
        Bundle bundle = new Bundle();
        bundle.putString("deviceId", androidDeviceId);
        fbInstance.logEvent(eventName, bundle);
    }
}
