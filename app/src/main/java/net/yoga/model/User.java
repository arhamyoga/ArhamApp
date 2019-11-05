package net.yoga.model;

public class User {

    private String userName;
    private String state;
    private String city;
    private Integer noOfSessionsCompleted;
    private String deviceId;
    private String fcmId;

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getNoOfSessionsCompleted() {
        return noOfSessionsCompleted;
    }

    public void setNoOfSessionsCompleted(Integer noOfSessionsCompleted) {
        this.noOfSessionsCompleted = noOfSessionsCompleted;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }
}
