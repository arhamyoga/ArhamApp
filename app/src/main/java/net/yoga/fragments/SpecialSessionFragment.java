package net.yoga.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import net.yoga.R;
import net.yoga.activities.MainActivity;
import net.yoga.adapter.VideoListAdapter;
import net.yoga.model.YoutubeVideo;
import net.yoga.utils.Constants;
import net.yoga.utils.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static net.yoga.utils.Utils.isOnline;

public class SpecialSessionFragment extends Fragment implements YouTubeThumbnailView.OnInitializedListener,
        YouTubeThumbnailLoader.OnThumbnailLoadedListener {

    YouTubeThumbnailView thumbnailView;
    YouTubeThumbnailLoader thumbnailLoader;
    RecyclerView VideoList;
    VideoListAdapter adapter;
    List<YoutubeVideo> youtubeVideos;
    String title = "";
    YoutubeVideo youtubeVideo;
    ProgressBar progressBar;
    private static final String TAG = "SpecialSessionFragment";

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
        setHasOptionsMenu(true);
        VideoList = view.findViewById(R.id.videos_list);
        youtubeVideos = new ArrayList<>();
        progressBar = view.findViewById(R.id.no_videos);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        VideoList.setLayoutManager(layoutManager);
        adapter=new VideoListAdapter(view.getContext(),youtubeVideos);
        VideoList.setAdapter(adapter);
        if(isOnline(view.getContext())) {
            thumbnailView = new YouTubeThumbnailView(getContext());
            thumbnailView.initialize(Constants.YOUTUBE_API_KEY, this);
            VideoList.setVisibility(View.VISIBLE);
        } else {
            Snackbar.make(view.findViewById(android.R.id.content),"Please connect to internet...",Snackbar.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
        youtubeVideo = new YoutubeVideo();
        new GetData().execute(s);
        youtubeVideo.setImageDrawable(youTubeThumbnailView.getDrawable());
        youtubeVideo.setVideoId(s);
        Log.d("Videoid",s);
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
        thumbnailLoader.setPlaylist("PLP528-nAynDr8t_UAtDyAm6FG9QkyiYIz");//PLDRGddUJfTCDRGewIAwYFLc1PhnaWM8O9
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
                youtubeVideo.setTitle(title);
                youtubeVideo.setImageUrl(jsonObject.getString("thumbnail_url"));
                youtubeVideos.add(youtubeVideo);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
