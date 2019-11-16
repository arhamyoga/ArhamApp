package net.yoga.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.yoga.R;
import net.yoga.activities.VideoPlayerActivity;
import net.yoga.model.YoutubeVideo;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyView> {

    private Context context;
    private List<YoutubeVideo> youtubeVideos;

    public VideoListAdapter(Context context, List<YoutubeVideo> youtubeVideos) {
        this.context = context;
        this.youtubeVideos = youtubeVideos;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(this.context).inflate(R.layout.video_row, parent, false);
        return new VideoListAdapter.MyView(itemView);
    }

    @Override
    public void onBindViewHolder(VideoListAdapter.MyView holder, int position) {

        YoutubeVideo youtubeVideo = youtubeVideos.get(position);
        holder.imageView.setImageDrawable(youtubeVideo.getImageDrawable());
        holder.titleView.setText(youtubeVideo.getTitle());
        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("videoUrl",youtubeVideo.getVideoId());
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return youtubeVideos.size();
    }

    public class MyView extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView titleView;
        public MyView(View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.thumbnailView);
            titleView = itemView.findViewById(R.id.videoTitle);
        }

    }
}
