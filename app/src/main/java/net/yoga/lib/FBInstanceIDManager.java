package net.yoga.lib;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by nayan on 12/10/17.
 */

public class FBInstanceIDManager extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        // String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}
