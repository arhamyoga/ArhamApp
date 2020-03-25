package net.yoga.model;

public class Campaigns {

    public String campaignId;
    private Integer sessionsCount;
    private String campaignName;
    private Boolean enrolled;
    private Long createdAt;

    public Campaigns() {
    }


    public Campaigns(String campaignId, Integer sessionsCount, String campaignName, Boolean enrolled, Long createdAt) {
        this.campaignId = campaignId;
        this.sessionsCount = sessionsCount;
        this.campaignName = campaignName;
        this.enrolled = enrolled;
        this.createdAt = createdAt;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getSessionsCount() {
        return sessionsCount;
    }

    public void setSessionsCount(Integer sessionsCount) {
        this.sessionsCount = sessionsCount;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Boolean getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(Boolean enrolled) {
        this.enrolled = enrolled;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
