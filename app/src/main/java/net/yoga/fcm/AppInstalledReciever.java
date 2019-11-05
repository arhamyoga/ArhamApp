package net.yoga.fcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics.Event;
import com.google.firebase.analytics.FirebaseAnalytics.Param;

import net.yoga.utils.Constants;

public class AppInstalledReciever extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String encodedSchemeSpecificPart = intent.getData().getEncodedSchemeSpecificPart();
        String string = context.getSharedPreferences(Constants.FCM_CROSS_PROMO_PREF, 0).getString("appPackageNameFromFCM", "");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("packageName outside if: ");
        stringBuilder.append(encodedSchemeSpecificPart);
        Log.i("onReceive ", stringBuilder.toString());
        try {
            StringBuilder stringBuilder2;
            if (encodedSchemeSpecificPart.equalsIgnoreCase(string)) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("packageName inside if: ");
                stringBuilder2.append(encodedSchemeSpecificPart);
                Log.i("onReceive ", stringBuilder2.toString());
                Bundle bundle = new Bundle();
                bundle.putString(Param.ITEM_ID, encodedSchemeSpecificPart);
                String str = Param.ITEM_NAME;
                stringBuilder = new StringBuilder();
                stringBuilder.append("installed-");
                stringBuilder.append(encodedSchemeSpecificPart);
                bundle.putString(str, stringBuilder.toString());
                bundle.putString(Param.CONTENT_TYPE, "image");
                FirebaseAnalytics.getInstance(context).logEvent(Event.SELECT_CONTENT, bundle);
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("packageName inside try: ");
            stringBuilder2.append(encodedSchemeSpecificPart);
            Log.i("onReceive ", stringBuilder2.toString());
            context.unregisterReceiver(this);
        } catch (Throwable e) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("packageName inside catch: ");
            stringBuilder3.append(e.getMessage());
            Log.i("onReceive ", stringBuilder3.toString());
            e.printStackTrace();
        }
    }
}
