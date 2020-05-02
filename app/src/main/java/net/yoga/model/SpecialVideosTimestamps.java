package net.yoga.model;

public class SpecialVideosTimestamps {

    private Long startTime;
    private Long endTime;

    public SpecialVideosTimestamps() {
    }

    public SpecialVideosTimestamps(Long startTime, Long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
