package net.yoga.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.iid.FirebaseInstanceId;

import net.yoga.R;
import net.yoga.model.User;

public class SignUpActivity extends AppCompatActivity {

    String mobNo = "",token="";
    EditText viewName,viewMobile,viewCity,viewState;
    Button proceed;
    FirebaseFirestore db;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Bundle bundle = getIntent().getExtras();
        mobNo = bundle.getString("mobile");

        proceed = findViewById(R.id.registerBtn);
        viewCity = findViewById(R.id.city);
        viewMobile = findViewById(R.id.mobileNumber);
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

        viewMobile.setText(mobNo);
        viewMobile.setEnabled(false);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user = new User();
                String city = viewCity.getText().toString();
                String state = viewState.getText().toString();
                String name = viewName.getText().toString();
                if(city.length()!=0&&name.length()!=0&&state.length()!=0) {
                    user.setCity(city);
                    user.setState(state);
                    user.setUserName(name);
                    user.setDeviceId(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID));
                    user.setNoOfSessionsCompleted(0);
                    user.setFcmId(token);
                    dialog.setMessage("Saving...");
                    dialog.show();
                    dialog.setCancelable(false);
                    if(!mobNo.startsWith("+91"))
                        mobNo = "+91"+mobNo;
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
            }
        });
    }
}
