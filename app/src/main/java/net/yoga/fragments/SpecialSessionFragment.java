package net.yoga.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import net.yoga.R;
import net.yoga.activities.MainActivity;
import net.yoga.adapter.SpecialSessionAdapter;
import net.yoga.model.SpecialSessionVideo;
import net.yoga.utils.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static net.yoga.utils.Constants.getYoutubeApiKey;
import static net.yoga.utils.Utils.isOnline;

public class SpecialSessionFragment extends Fragment implements YouTubeThumbnailView.OnInitializedListener,
        YouTubeThumbnailLoader.OnThumbnailLoadedListener {

    YouTubeThumbnailView thumbnailView;
    YouTubeThumbnailLoader thumbnailLoader;
    RecyclerView VideoList;
    SpecialSessionAdapter adapter;
    List<SpecialSessionVideo> specialSessionVideos;
    String title = "";
    SpecialSessionVideo specialSessionVideo;
    ProgressBar progressBar;
    ArrayList<String> videoIds = new ArrayList<>();
    private static final String TAG = "SpecialSessionFragment";
    String activityType;
    TextView toolbarText;

    public SpecialSessionFragment(String activityType) {
        this.activityType = activityType;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.special_session_fragment, container, false);
        // Inflate the layout for this fragment
        view.setTag(TAG);
        Log.d(TAG,TAG);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().mutate().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        toolbarText = toolbar.findViewById(R.id.toolbarText);
        if(activityType.equals("boostup")) {
            toolbarText.setText("Boost Up in Lockdown");
        } else if(activityType.equals("specialsession")) {
            toolbarText.setText("Special Sessions");
        } else {
            toolbarText.setText("Self Discovery");
        }
        setHasOptionsMenu(true);
        VideoList = view.findViewById(R.id.videos_list);
        specialSessionVideos = new ArrayList<>();
        progressBar = view.findViewById(R.id.no_videos);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        VideoList.setLayoutManager(layoutManager);
        adapter=new SpecialSessionAdapter(view.getContext(), specialSessionVideos,activityType);
        VideoList.setAdapter(adapter);
        if(isOnline(view.getContext())) {
            thumbnailView = new YouTubeThumbnailView(getContext());
            thumbnailView.initialize(getYoutubeApiKey(), this);
            VideoList.setVisibility(View.VISIBLE);
        } else {
            TextView noInternet = view.findViewById(R.id.nointernet);
            noInternet.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
        if(!videoIds.contains(s)) {
            specialSessionVideo = new SpecialSessionVideo();
            specialSessionVideo.setImageDrawable(youTubeThumbnailView.getDrawable());
            specialSessionVideo.setVideoId(s);
            videoIds.add(s);
            Log.d("Videoid", s);
            new GetData().execute(s);
        }
        if (thumbnailLoader.hasNext())
            thumbnailLoader.next();
//        new Handler().postDelayed(this::add,15000);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

    }

    @Override
    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
        thumbnailLoader = youTubeThumbnailLoader;
        youTubeThumbnailLoader.setOnThumbnailLoadedListener(SpecialSessionFragment.this);
        if(activityType.equals("specialsession")) {
            thumbnailLoader.setPlaylist("PLP528-nAynDr8t_UAtDyAm6FG9QkyiYIz");//PLDRGddUJfTCDRGewIAwYFLc1PhnaWM8O9
        } else if(activityType.equals("boostup")) {
            thumbnailLoader.setPlaylist("PLP528-nAynDp2JmaBmBdbPFj7GFC8f7CT");//PLDRGddUJfTCDRGewIAwYFLc1PhnaWM8O9
        } else {
            thumbnailLoader.setPlaylist("PLP528-nAynDqX3EYOuSmcFW5eJnlgLSxq");
        }
    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

    }

    private class GetData extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            RequestHandler requestHandler=new RequestHandler();
            return requestHandler.sendGetRequest(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                progressBar.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(s);
                title = jsonObject.getString("title");
                specialSessionVideo.setTitle(title);
                specialSessionVideo.setImageUrl(jsonObject.getString("thumbnail_url"));
                specialSessionVideos.add(specialSessionVideo);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
