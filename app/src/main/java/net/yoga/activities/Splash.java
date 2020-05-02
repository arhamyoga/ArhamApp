package net.yoga.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import net.yoga.R;
import net.yoga.model.Campaigns;
import net.yoga.model.User;
import net.yoga.sharedpref.SessionManager;
import net.yoga.utils.FBEventLogManager;

import java.util.List;

public class Splash extends AppCompatActivity {

    private static final String TAG = "Arham";

    private Context context;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initialize();
    }

    private void initialize() {
        context = this;
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(getApplicationContext()));
        session = new SessionManager(this);
        FBEventLogManager.initialize(this);
        FBEventLogManager.logArhamAppStarted();

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            final String mobileNumber = currentUser.getPhoneNumber();
            DocumentReference docRef = db.collection("users").document(mobileNumber);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if(documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    String deviceId = user.getDeviceId();
                    if(deviceId.equals(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID))) {
                        Bundle bundle = new Bundle();
                        List<Campaigns> campaignsList = user.getCampaigns();
                        if(campaignsList == null || campaignsList.size()==0) {
                            bundle.putString("campaignenrolled","notanswered");
                        } else {
                            for(Campaigns campaigns: campaignsList) {
                                if(campaigns.getCampaignId().equalsIgnoreCase("Yoga Day")){
                                    if(campaigns.getEnrolled()) {
                                        bundle.putString("campaignenrolled","enrolled");
                                    } else {
                                        bundle.putString("campaignenrolled","notenrolled");
                                    }
                                    break;
                                } else {
                                    bundle.putString("campaignenrolled","notanswered");
                                }
                            }
                        }
                        Log.d(TAG,"SplashBundle : "+bundle);
                        Intent intent = new Intent(Splash.this,MainActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("mobile",mobileNumber);
                        Intent i = new Intent(Splash.this, SignUpActivity.class);
                        i.putExtras(bundle);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("mobile",mobileNumber);
                    Intent i = new Intent(Splash.this, SignUpActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                }
            }).addOnFailureListener(e -> {
                Log.e(TAG,"LoginActivity : "+"Problem connecting to db");
            });
            Log.d(TAG, "mobileNumberUser : "+ mobileNumber);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                finish();
            }, 1000);
        }
    }
}
