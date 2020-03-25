package net.yoga.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.iid.FirebaseInstanceId;

import net.yoga.R;
import net.yoga.model.User;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import static net.yoga.utils.Utils.isOnline;

public class OTPActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText otpEditText;
    private String mVerificationId;
    Button verifyOTP,resendOTP;
    String status = "",mobNo="",token="";
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    TextView waitOTP;
    String myReferralCode="";
    boolean flagUniqueCode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        firebaseAuth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(getApplicationContext()));
        otpEditText = findViewById(R.id.field_verification_code);
        verifyOTP = findViewById(R.id.button_verify_phone);
        resendOTP = findViewById(R.id.button_resend);
        waitOTP = findViewById(R.id.textResendMsg);
        progressDialog = new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        //getting mobile number from previous activity
        Bundle bundle = getIntent().getExtras();
        mobNo = bundle.getString("mobile");
        status = bundle.getString("status");
        //requesting for the OTP
        sendVerificationCode(mobNo);

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

        verifyOTP.setOnClickListener(v -> {
            String code = otpEditText.getText().toString();
            if (code.isEmpty() || code.length() < 6) {
                otpEditText.setError("Enter valid code");
                otpEditText.requestFocus();
                resendOTP.setVisibility(View.VISIBLE);
                return;
            }

            //verifying the code entered manually
            if(isOnline(getApplicationContext())) {
                try {
                    verifyVerificationCode(code);
                } catch (Exception e){
                    Snackbar.make(findViewById(android.R.id.content),"Something is wrong...", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(findViewById(android.R.id.content),"There is no internet connection...",Snackbar.LENGTH_SHORT).show();
            }
        });

        new Handler().postDelayed(() -> {
            resendOTP.setVisibility(View.VISIBLE);
            waitOTP.setVisibility(View.GONE);
        },60000);

        resendOTP.setOnClickListener(v -> {
            if(isOnline(getApplicationContext())) {
                sendVerificationCode(mobNo);
            } else {
                Snackbar.make(findViewById(android.R.id.content),"There is no internet connection...",Snackbar.LENGTH_SHORT).show();
            }
        });

    }
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "+91" + mobile,
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otpEditText.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };
    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPActivity.this, task -> {
                    if (task.isSuccessful()) {
                        //verification successful we will start the profile activity
                        if(status.equals("login")) {
                            String mobileUser = firebaseAuth.getCurrentUser().getPhoneNumber();
                            final DocumentReference docRef = db.collection("users1").document(mobileUser);
                            docRef.get().addOnSuccessListener(documentSnapshot -> {
                                if(documentSnapshot.exists()){
                                    User user = documentSnapshot.toObject(User.class);
                                    if(user.getMyReferralCode()==null){
                                        myReferralCode = generateReferralCode();
                                        while(!flagUniqueCode) {
                                            Query query = db.collection("users1")
                                                    .whereEqualTo("myReferralCode", myReferralCode);
                                            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                    myReferralCode = generateReferralCode();
                                                } else {
                                                    flagUniqueCode = true;
                                                }
                                            });
                                        }
                                        docRef.update("myReferralCode",myReferralCode);
                                    }
                                    docRef.update("fcmId",token).addOnSuccessListener(aVoid -> {
                                        Log.d("Arham Session","Completed");
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(OTPActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    });
                                }
                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Intent intent = new Intent(OTPActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            });
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("mobile",mobNo);
                            Intent intent = new Intent(OTPActivity.this,SignUpActivity.class);
                            intent.putExtras(bundle);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            progressDialog.dismiss();
                            startActivity(intent);
                        }

                    } else {

                        //verification unsuccessful.. display an error message
                        progressDialog.dismiss();
                        String message = "Somthing is wrong, we will fix it soon...";

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered...";
                        }

                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
                        snackbar.setAction("Dismiss", v -> {

                        });
                        snackbar.show();
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
