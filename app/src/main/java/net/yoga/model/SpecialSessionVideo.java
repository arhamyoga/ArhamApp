package net.yoga.model;

import android.graphics.drawable.Drawable;

import java.util.List;

public class SpecialSessionVideo {

    public String title;
    public Long id;
    public String videoId;
    public String imageUrl;
    public Drawable imageDrawable;
    public boolean isWatched;
    public Integer count;
    public List<SpecialVideosTimestamps> specialVideosTimestampsList;
    public List<String> videoTypes;

    public SpecialSessionVideo() {
    }

    public SpecialSessionVideo(String title, Long id, String videoId, String imageUrl,
                               Drawable imageDrawable, boolean isWatched, Integer count, List<SpecialVideosTimestamps> specialVideosTimestampsList,
                               List<String> videoTypes) {
        this.title = title;
        this.id = id;
        this.videoId = videoId;
        this.imageUrl = imageUrl;
        this.imageDrawable = imageDrawable;
        this.isWatched = isWatched;
        this.count = count;
        this.specialVideosTimestampsList = specialVideosTimestampsList;
        this.videoTypes = videoTypes;
    }

    public Drawable getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(Drawable imageDrawable) {
        this.imageDrawable = imageDrawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<SpecialVideosTimestamps> getSpecialVideosTimestampsList() {
        return specialVideosTimestampsList;
    }

    public void setSpecialVideosTimestampsList(List<SpecialVideosTimestamps> specialVideosTimestampsList) {
        this.specialVideosTimestampsList = specialVideosTimestampsList;
    }

    public List<String> getVideoTypes() {
        return videoTypes;
    }

    public void setVideoTypes(List<String> videoTypes) {
        this.videoTypes = videoTypes;
    }
}
