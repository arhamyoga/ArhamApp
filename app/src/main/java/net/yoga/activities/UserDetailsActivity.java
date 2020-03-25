package net.yoga.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

import net.yoga.R;
import net.yoga.model.Campaigns;
import net.yoga.model.User;
import net.yoga.sharedpref.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDetailsActivity extends AppCompatActivity {

    ImageView profileImage;
    TextView userName, myReferralCode, referralsJoined, arhamSessionsDone, campaignSessionsCompleted;
    EditText referralCodeInput;
    ImageButton editButton, saveButton;
    LinearLayout campaignLayout,referralLayout;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    SessionManager session;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        initialize();
        String currentMobile = mAuth.getCurrentUser().getPhoneNumber();
        Log.d("mobile",currentMobile);
        DocumentReference docRef = db.collection("users1").document(currentMobile);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            List<Campaigns> campaignsList = user.getCampaigns();
            if(campaignsList!=null && campaignsList.size()!=0) {
                Campaigns campaigns = campaignsList.get(campaignsList.size()-1);
                if (campaigns.getEnrolled()) {
                    campaignLayout.setVisibility(View.VISIBLE);
                    campaignSessionsCompleted.setText(campaigns.getSessionsCount() + "");
                } else {
                    campaignLayout.setVisibility(View.GONE);
                }
            }
            String myJoinedCode = user.getMyJoinedCode();
            Log.d("joined",myJoinedCode);
            if(myJoinedCode.length()==0) {
                referralLayout.setVisibility(View.VISIBLE);
                saveButton.setOnClickListener(view -> {
                    String referralInput = referralCodeInput.getText().toString();
                    if(referralInput.length()==0) {
                        referralCodeInput.setError("Enter valid code");
                    } else {
                        progress.setMessage("Saving...");
                        progress.show();
                        progress.setCancelable(false);
                        Query query1 = db.collection("users1")
                                .whereEqualTo("myReferralCode",referralInput);
                        query1.get().addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                String id = queryDocumentSnapshots.getDocuments().get(0).getId();
                                DocumentReference docRef1 = db.collection("users1").document(id);
                                docRef1.get().addOnSuccessListener(documentSnapshot1 -> {
                                    if (documentSnapshot1.exists()) {
                                        User otherUser = documentSnapshot.toObject(User.class);
                                        List<String> referrals = otherUser.getJoinedReferralCode();
                                        if (referrals == null) {
                                            referrals = new ArrayList<>();
                                        }
                                        Log.d("joned", referrals + "");
                                        referrals.add(currentMobile);
                                        docRef1.update("joinedReferralCode", referrals).addOnSuccessListener(aVoid -> {
                                            referralLayout.setVisibility(View.GONE);
                                            docRef.update("myJoinedCode", referralInput);
                                            progress.dismiss();
                                        });
                                    } else {
                                        referralCodeInput.setError("Invalid Code");
                                        progress.dismiss();
                                    }
                                });
                            } else {
                                Log.d("Other referrals", "no code exists");
                                referralCodeInput.setError("Invalid Code");
                                progress.dismiss();
                            }
                        });
                    }
                });
            }
        });
        userName.setText(session.getUsername());
        myReferralCode.setText(session.getReferralCode());
        arhamSessionsDone.setText(session.getArhamSessions()+"");
    }

    private void initialize() {
        progress = new ProgressDialog(this);
        session = new SessionManager(getApplicationContext());
        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.user_name);
        myReferralCode = findViewById(R.id.my_referral_code_text);
        referralsJoined = findViewById(R.id.referrals_joined);
        arhamSessionsDone = findViewById(R.id.arham_sessions);
        campaignSessionsCompleted = findViewById(R.id.campaign_sessions);
        campaignLayout = findViewById(R.id.campaign_layout);
        referralLayout = findViewById(R.id.referral_layout);
        referralCodeInput = findViewById(R.id.referral_code_input);
        editButton = findViewById(R.id.edit_button);
        saveButton = findViewById(R.id.save_button);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }
}
