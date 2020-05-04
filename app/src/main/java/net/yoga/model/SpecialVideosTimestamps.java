package net.yoga.model;

public class SpecialVideosTimestamps {

    private Long startTime;
    private Long watchedTime;

    public SpecialVideosTimestamps() {
    }

    public SpecialVideosTimestamps(Long startTime, Long watchedTime) {
        this.startTime = startTime;
        this.watchedTime = watchedTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getWatchedTime() {
        return watchedTime;
    }

    public void setWatchedTime(Long watchedTime) {
        this.watchedTime = watchedTime;
    }
}
