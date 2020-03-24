//package net.yoga.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.google.android.youtube.player.YouTubeBaseActivity;
//import com.google.android.youtube.player.YouTubeInitializationResult;
//import com.google.android.youtube.player.YouTubePlayer;
//import com.google.android.youtube.player.YouTubePlayerView;
//
//import net.yoga.R;
//
//import static net.yoga.utils.Constants.getYoutubeApiKey;
//
//public class VideoPlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
//
//    private static final int RECOVERY_REQUEST = 1;
//    private YouTubePlayerView youTubeView;
//    String url;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_player);
//        Bundle bundle = getIntent().getExtras();
//        url = bundle.getString("videoUrl");
//        youTubeView = findViewById(R.id.youtube_view);
//        youTubeView.initialize(getYoutubeApiKey(), this);
//    }
//
//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//        if (!b) {
//            youTubePlayer.loadVideo(url); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
//        }
//    }
//
//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
//        if (errorReason.isUserRecoverableError()) {
//            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
//        } else {
//            String error = "Error initializing YouTube player: "+ errorReason.toString();
//            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RECOVERY_REQUEST) {
//            // Retry initialization if user performed a recovery action
//            getYouTubePlayerProvider().initialize(getYoutubeApiKey(), this);
//        }
//    }
//
//    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
//        return youTubeView;
//    }
//}
package net.yoga.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.WindowManager;

import com.google.android.exoplayer2.ui.PlayerView;

import net.yoga.R;
import net.yoga.utils.ExoPlayerManager;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class VideoPlayerActivity extends AppCompatActivity {

    String url;
    String videoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("videoId");
        videoTitle = bundle.getString("videoTitle");
        Log.d("videourl to play",url);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        extractYoutubeUrl();
    }

    private void extractYoutubeUrl() {
        @SuppressLint("StaticFieldLeak") YouTubeExtractor mExtractor = new YouTubeExtractor(this) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> sparseArray, VideoMeta videoMeta) {
                Log.d("videoMeta",videoMeta.toString());
                if (sparseArray != null) {
                    Log.e("err",sparseArray.toString());
                    playVideo(sparseArray.get(18).getUrl());
                }
            }
        };
        mExtractor.extract(url, true, true);
    }

    private void playVideo(String downloadUrl) {
        Log.d("download url",downloadUrl);
        PlayerView mPlayerView = findViewById(R.id.mPlayerView);
        mPlayerView.setPlayer(ExoPlayerManager.getSharedInstance(VideoPlayerActivity.this).getPlayerView().getPlayer());
        ExoPlayerManager.getSharedInstance(VideoPlayerActivity.this).playStream(downloadUrl,url,videoTitle);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        boolean isPlaying = ExoPlayerManager.getSharedInstance(VideoPlayerActivity.this).isPlayerPlaying();
        Log.e("isPlaying",isPlaying+"");
        ExoPlayerManager.getSharedInstance(VideoPlayerActivity.this).destroyPlayer();
        isPlaying = ExoPlayerManager.getSharedInstance(VideoPlayerActivity.this).isPlayerPlaying();
        Log.e("isPlaying",isPlaying+"");
    }
}
