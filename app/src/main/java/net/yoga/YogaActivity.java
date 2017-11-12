package net.yoga;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;

import net.yoga.lib.ArcProgress;


/**
 * Created by pkumar on 11/7/2017.
 */

public class YogaActivity extends AppCompatActivity {

    private VideoView videoView;
    private ImageButton playButton;

    private int currentTime = 0;
    private Handler mHandler = new Handler();
    private ArcProgress progressView;
    private int video_time = 65;

    private boolean isPlaying;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);
        videoView = (VideoView) findViewById(R.id.videoView);
        progressView = (ArcProgress) findViewById(R.id.circleView);
        playButton = (ImageButton) findViewById(R.id.playView);

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
    }

    public void updateProgressBar() {
        if (mHandler != null) {
            mHandler.post(UpdateProgress);
        }
    }

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
            }
        }
    };
}
