package net.yoga.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.yoga.R;
import net.yoga.activities.VideoPlayerActivity;
import net.yoga.model.SpecialSessionVideo;

import java.util.List;

public class SpecialSessionAdapter extends RecyclerView.Adapter<SpecialSessionAdapter.MyView> {

    private Context context;
    private List<SpecialSessionVideo> specialSessionVideos;

    public SpecialSessionAdapter(Context context, List<SpecialSessionVideo> specialSessionVideos) {
        this.context = context;
        this.specialSessionVideos = specialSessionVideos;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(this.context).inflate(R.layout.video_row, parent, false);
        return new SpecialSessionAdapter.MyView(itemView);
    }

    @Override
    public void onBindViewHolder(SpecialSessionAdapter.MyView holder, int position) {

        SpecialSessionVideo specialSessionVideo = specialSessionVideos.get(position);
        holder.imageView.setImageDrawable(specialSessionVideo.getImageDrawable());
        holder.titleView.setText(specialSessionVideo.getTitle());
        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("videoId", specialSessionVideo.getVideoId());
            bundle.putString("videoTitle",specialSessionVideo.getTitle());
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return specialSessionVideos.size();
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
