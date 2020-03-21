package net.yoga.model;

import android.graphics.drawable.Drawable;

public class SpecialSessionVideo {
    private String title;
    private Long id;
    private String videoId;
    private String imageUrl;
    private Drawable imageDrawable;
    private boolean isWatched;
    private Integer count;

    public SpecialSessionVideo() {
    }

    public SpecialSessionVideo(String title, Long id, String videoId, String imageUrl, Drawable imageDrawable, boolean isWatched, Integer count) {
        this.title = title;
        this.id = id;
        this.videoId = videoId;
        this.imageUrl = imageUrl;
        this.imageDrawable = imageDrawable;
        this.isWatched = isWatched;
        this.count = count;
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
}
