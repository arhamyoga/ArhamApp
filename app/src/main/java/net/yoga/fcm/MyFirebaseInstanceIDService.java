package net.yoga.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

public class MyFirebaseInstanceIDService /*extends FirebaseInstanceIdService*/ {
    private static final String TAG = "MyFirebaseIIDService";

//    @Override
    public void onTokenRefresh() {
//        super.onTokenRefresh();

        //now we will have the token
        String token = null;
        try {
            token = FirebaseInstanceId.getInstance().getToken(FirebaseInstanceId.getInstance().getId(),"FCM");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //for now we are displaying the token in the log
        //copy it as this method is called only when the new token is generated
        //and usually new token is only generated when the app is reinstalled or the data is cleared
        Log.d(TAG,"MyRefreshedToken"+token);
    }
}
