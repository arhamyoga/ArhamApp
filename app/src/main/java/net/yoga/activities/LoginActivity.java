package net.yoga.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import net.yoga.R;
import net.yoga.model.User;

public class LoginActivity extends AppCompatActivity {

    EditText mobileNumber;
    Button proceed;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(getApplicationContext()));
        progressDialog = new ProgressDialog(this);
        mobileNumber = findViewById(R.id.field_phone_number);
        proceed = findViewById(R.id.button_start_verification);

        //firestore initialization
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        //on click listeners
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputMobile = mobileNumber.getText().toString();
                if(inputMobile.length()!=10){
                    mobileNumber.setError("Enter a valid mobile");
                    mobileNumber.requestFocus();
                    return;
                } else {
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    DocumentReference docRef = db.collection("users").document("+91"+inputMobile);
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Bundle bundle = new Bundle();
                            Log.d("LoginDoc",documentSnapshot.exists()+"");
                            if(documentSnapshot.exists()){
                                bundle.putString("status","login");
                                bundle.putString("mobile",inputMobile);
                            } else {
                                bundle.putString("mobile",inputMobile);
                                bundle.putString("status","register");
                            }
                            Log.d("bundle",bundle.toString());
                            Intent i = new Intent(getApplicationContext(), OTPActivity.class);
                            i.putExtras(bundle);
                            progressDialog.dismiss();
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            check=0;
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog.show();
        progressDialog.setMessage("Collecting Data...");
        progressDialog.setCancelable(false);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            final String mobileNumber1 = currentUser.getPhoneNumber();
            DocumentReference docRef = db.collection("users").document(mobileNumber1);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
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
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("mobile",mobileNumber1);
//                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
//                    progressDialog.dismiss();
//                    finish();
                    Log.e("LoginActivity","Problem connecting to db");
                }
            });
            Log.d("mobileNumberUser", mobileNumber1 + "");
        } else {
            progressDialog.dismiss();
        }
    }
}
