package net.yoga.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.hbb20.CountryCodePicker;

import net.yoga.R;

import static net.yoga.utils.Utils.isOnline;

public class LoginActivity extends AppCompatActivity {

    EditText mobileNumber;
    Button proceed;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    int check;
    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:net.yoga"));
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(getApplicationContext()));
        progressDialog = new ProgressDialog(this);
        mobileNumber = findViewById(R.id.field_phone_number);
        ccp = findViewById(R.id.ccp_picker);
        ccp.registerCarrierNumberEditText(mobileNumber);
        proceed = findViewById(R.id.button_start_verification);

        //firestore initialization
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        //on click listeners
        proceed.setOnClickListener(v -> {
            if(!ccp.isValidFullNumber()){
                mobileNumber.setError("Enter a valid mobile");
                mobileNumber.requestFocus();
                return;
            } else {
                if(isOnline(getApplicationContext())) {
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    DocumentReference docRef = db.collection("users").document(ccp.getFullNumberWithPlus()+"");
                    docRef.get().addOnSuccessListener(documentSnapshot -> {
                        Bundle bundle = new Bundle();
                        Log.d("LoginDoc", documentSnapshot.exists() + "");
                        if (documentSnapshot.exists()) {
                            bundle.putString("status", "login");
                            bundle.putString("mobile", ccp.getFullNumberWithPlus()+"");
                        } else {
                            bundle.putString("mobile", ccp.getFullNumberWithPlus()+"");
                            bundle.putString("status", "register");
                        }
                        Log.d("bundle", bundle.toString());
                        Intent i = new Intent(getApplicationContext(), OTPActivity.class);
                        i.putExtras(bundle);
                        progressDialog.dismiss();
                        startActivity(i);
                    }).addOnFailureListener(e -> check = 0);
                } else {
                    Snackbar.make(findViewById(android.R.id.content),"Please check your internet...",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /*@Override
    public void onStart() {
        super.onStart();
        progressDialog.show();
        progressDialog.setMessage("Collecting Data...");
        progressDialog.setCancelable(false);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            final String mobileNumber1 = currentUser.getPhoneNumber();
            DocumentReference docRef = db.collection("users").document(mobileNumber1);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                Log.d("Document",documentSnapshot.exists()+"");
                if(documentSnapshot.exists()){
//                        User user = documentSnapshot.toObject(User.class);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    progressDialog.dismiss();
                    finish();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("mobile",mobileNumber1);
                    Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                    i.putExtras(bundle);
                    progressDialog.dismiss();
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
            progressDialog.dismiss();
        }
    }*/
}
