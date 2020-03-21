package net.yoga.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.iid.FirebaseInstanceId;

import net.yoga.R;
import net.yoga.model.User;

import java.security.SecureRandom;

public class SignUpActivity extends AppCompatActivity {

    String mobNo = "",token="";
    EditText viewName,viewReferralCode,viewCity,viewState,viewRefferalCode;
    Button proceed;
    FirebaseFirestore db;
    ProgressDialog dialog;
    String myReferralCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Bundle bundle = getIntent().getExtras();
        mobNo = bundle.getString("mobile");

        proceed = findViewById(R.id.registerBtn);
        viewCity = findViewById(R.id.city);
        viewReferralCode = findViewById(R.id.referenceCode);
        viewState = findViewById(R.id.state);
        viewName = findViewById(R.id.userName);
        dialog = new ProgressDialog(this);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w( "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    token = task.getResult().getToken();
                    Log.d("FCM token",token);
                });

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        myReferralCode = generateReferralCode();

        Query query = db.collection("users")
                        .whereEqualTo("myReferralCode",myReferralCode);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(!queryDocumentSnapshots.isEmpty()){
                myReferralCode = generateReferralCode();
            }
        });


        proceed.setOnClickListener(v -> {
            final User user = new User();
            String city = viewCity.getText().toString();
            String state = viewState.getText().toString();
            String name = viewName.getText().toString();
            String referralCode = viewReferralCode.getText().toString();
            if(referralCode.length()!=0){
//                user.setJoinedReferralCode(referralCode);
            }
            if(city.length()!=0&&name.length()!=0&&state.length()!=0) {
                user.setCity(city);
                user.setState(state);
                user.setUserName(name);
                user.setDeviceId(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID));
                user.setNoOfSessionsCompleted(0);
                user.setFcmId(token);
                user.setMyReferralCode(myReferralCode);
                dialog.setMessage("Saving...");
                dialog.show();
                dialog.setCancelable(false);
                db.collection("users").document(mobNo).set(user)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            dialog.dismiss();
                            finish();
                        })
                        .addOnFailureListener(e -> {

                        });
            } else {
                if(TextUtils.isEmpty(city)){
                    viewCity.setError("Please fill this");
                    viewCity.requestFocus();
                }
                if(TextUtils.isEmpty(state)){
                    viewState.setError("Please fill this");
                    viewState.requestFocus();
                }
                if(TextUtils.isEmpty(name)){
                    viewName.setError("Please fill this");
                    viewName.requestFocus();
                }
            }
        });
    }

    private String generateReferralCode(){
        char[] corpus = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        int generated = 0;
        int desired=6;
        char[] result= new char[desired];

        while(generated<desired){
            byte[] ran = SecureRandom.getSeed(desired);
            for(byte b: ran){
                if(b>=0&&b<corpus.length){
                    result[generated] = corpus[b];
                    generated+=1;
                    if(generated==desired) break;
                }
            }
        }
        return new String(result);
    }
}
