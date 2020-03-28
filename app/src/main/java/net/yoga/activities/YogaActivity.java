package net.yoga.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import net.yoga.R;
import net.yoga.model.User;
import net.yoga.sharedpref.SessionManager;
import net.yoga.utils.ArcProgress;
import net.yoga.utils.FBEventLogManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pkumar on 11/7/2017.
 */

public class YogaActivity extends AppCompatActivity {

    private VideoView videoView;
    private ImageButton playButton;

    private int currentTime = 0;
    private Handler mHandler = new Handler();
    private ArcProgress progressView;
    private int video_time = 12*60+4;
    String mobileUser="";
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    SessionManager session;

    private boolean isPlaying;
    private Runnable UpdateProgress = new Runnable() {
        public void run() {
            if (isPlaying) {
                currentTime = currentTime - 1;
                progressView.setProgress(currentTime);
            }
            if (currentTime > 0) {
                mHandler.postDelayed(this, 1000);
            } else if (currentTime == 0) {
                isPlaying = false;
                progressView.setVisibility(View.INVISIBLE);
                playButton.setVisibility(View.VISIBLE);

                FBEventLogManager.logArhamSessionCompleted();
                final DocumentReference docRef = db.collection("users").document(mobileUser);

                Log.e("Session count",""+session.getArhamSessions());
                int countSessions = session.getArhamSessions()+1;
                session.putNoOfSessions(countSessions);
                Log.e("Session count",""+session.getArhamSessions());

                docRef.get().addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
//                        Long count = documentSnapshot.getLong("noOfSessionsCompleted");
//                        count++;
                        Log.e("Session Updated",""+session.getArhamSessions());
                        User user = documentSnapshot.toObject(User.class);
                        List<Long> timestamps = user.getTimestamps();
                        if(timestamps==null){
                            timestamps = new ArrayList<>();
                        }
                        timestamps.add(System.currentTimeMillis());
                        docRef.update("timestamps",timestamps);
                        docRef.update("noOfSessionsCompleted",countSessions).addOnSuccessListener(aVoid -> Log.d("Arham Session","Completed"));
                    }
                }).addOnFailureListener(e -> {
                    Log.e("Session Shared",""+session.getArhamSessions());
                    session.putNoOfSessions(countSessions);
                });
                Toast.makeText(YogaActivity.this,"Thank you for taking the session...",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);
        videoView = findViewById(R.id.videoView);
        videoView.setOnErrorListener((mp, what, extra) -> {
            Log.d("video", "setOnErrorListener ");
            return true;
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        session = new SessionManager(getApplicationContext());
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        mAuth = FirebaseAuth.getInstance();
        mobileUser = mAuth.getCurrentUser().getPhoneNumber();

        progressView = findViewById(R.id.circleView);
        playButton = findViewById(R.id.playView);

        progressView.setOnClickListener(view -> {
            isPlaying = false;
            videoView.pause();
            progressView.setVisibility(View.INVISIBLE);
            playButton.setVisibility(View.VISIBLE);
        });

        playButton.setOnClickListener(view -> {
            isPlaying = true;
            playButton.setVisibility(View.GONE);
            progressView.setVisibility(View.VISIBLE);
            videoView.start();
            if (currentTime == 0) {
                restartVideoPlay();
            }
        });

        restartVideoPlay();
    }

    private void restartVideoPlay() {
        String path = "android.resource://" + getPackageName() + "/" + R.raw.yoga;
        videoView.setVideoURI(Uri.parse(path));
        videoView.start();
        isPlaying = true;
        progressView.setMax(video_time);
        currentTime = video_time;
        updateProgressBar();

        FBEventLogManager.logArhamSessionStarted();
    }

    public void updateProgressBar() {
        if (mHandler != null) {
            mHandler.post(UpdateProgress);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}
