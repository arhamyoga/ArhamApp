package net.yoga.model;

public class User {

    public String userName;
    public String state;
    public String city;
    public Integer noOfSessionsCompleted;
    public String deviceId;
    public String fcmId;

    public User() {
    }

    public User(String userName, String state, String city, Integer noOfSessionsCompleted, String deviceId, String fcmId) {
        this.userName = userName;
        this.state = state;
        this.city = city;
        this.noOfSessionsCompleted = noOfSessionsCompleted;
        this.deviceId = deviceId;
        this.fcmId = fcmId;
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
