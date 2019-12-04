package net.yoga.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import net.yoga.R;
import net.yoga.utils.FBEventLogManager;

/**
 * Created by mvnpavan on 08/11/17.
 */

public class Splash extends AppCompatActivity {

    private static final String TAG = "ARM-S";

    private Context context;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(getApplicationContext()));
        FBEventLogManager.initialize(this);
        FBEventLogManager.logArhamAppStarted();

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            final String mobileNumber1 = currentUser.getPhoneNumber();
            DocumentReference docRef = db.collection("users").document(mobileNumber1);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                Log.d("Document",documentSnapshot.exists()+"");
                if(documentSnapshot.exists()){
//                        User user = documentSnapshot.toObject(User.class);
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("mobile",mobileNumber1);
                    Intent i = new Intent(Splash.this, SignUpActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                }
            }).addOnFailureListener(e -> {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("mobile",mobileNumber1);
//                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
//                    progressDialog.dismiss();
//                    finish();
                Log.e("LoginActivity","Problem connecting to db");
            });
            Log.d("mobileNumberUser", mobileNumber1 + "");
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
