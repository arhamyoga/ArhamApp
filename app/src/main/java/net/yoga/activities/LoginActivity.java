package net.yoga.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.yoga.R;

public class LoginActivity extends AppCompatActivity {

    EditText mobileNumber;
    Button proceed;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(getApplicationContext()));
        progressDialog = new ProgressDialog(this);
        mobileNumber = findViewById(R.id.field_phone_number);
        proceed = findViewById(R.id.button_start_verification);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputMobile = mobileNumber.getText().toString();
                if(inputMobile.length()!=10){
                    mobileNumber.setError("Enter a valid mobile");
                    mobileNumber.requestFocus();
                    return;
                } else {
                    Intent i = new Intent(getApplicationContext(), OTPActivity.class);
                    i.putExtra("mobile",inputMobile);
                    startActivity(i);
                }

            }
        });
    }

    void launchSignUp(View v) {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
        this.finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog.show();
        progressDialog.setMessage("Collecting Data...");
        progressDialog.setCancelable(false);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String mobileNumber1 = currentUser.getPhoneNumber();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            progressDialog.dismiss();
            finish();
            Log.d("mobileNumberUser", mobileNumber1 + "");
        } else {
            progressDialog.dismiss();
        }
    }
}
