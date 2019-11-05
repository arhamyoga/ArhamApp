package net.yoga.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import net.yoga.R;
import net.yoga.utils.ArcProgress;
import net.yoga.utils.FBEventLogManager;


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
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Long count = documentSnapshot.getLong("noOfSessionsCompleted");
                            count++;
                            docRef.update("noOfSessionsCompleted",count).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Arham Session","Completed");
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);
        videoView = findViewById(R.id.videoView);
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("video", "setOnErrorListener ");
                return true;
            }
        });

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        mAuth = FirebaseAuth.getInstance();
        mobileUser = mAuth.getCurrentUser().getPhoneNumber();

        progressView = findViewById(R.id.circleView);
        playButton = findViewById(R.id.playView);

        progressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlaying = false;
                videoView.pause();
                progressView.setVisibility(View.INVISIBLE);
                playButton.setVisibility(View.VISIBLE);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlaying = true;
                playButton.setVisibility(View.GONE);
                progressView.setVisibility(View.VISIBLE);
                videoView.start();
                if (currentTime == 0) {
                    restartVideoPlay();
                }
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
}
