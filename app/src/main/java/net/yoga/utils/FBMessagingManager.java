package net.yoga.utils;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by nayan on 12/10/17.
 */

public class FBMessagingManager extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(Utils.TAG, "From: " + remoteMessage.getFrom());
    }
}
