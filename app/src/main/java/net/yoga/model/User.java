package net.yoga.model;

import java.util.List;

public class User {

    public String userName;
    public String state;
    public String city;
    public Integer noOfSessionsCompleted;
    public String deviceId;
    public String fcmId;
    public String myReferralCode;
    public List<Long> timestamps;
    public List<String> joinedReferralCode;
    public List<SpecialSessionVideo> specialSessionVideos;

    public User() {
    }


    public User(String userName, String state, String city, Integer noOfSessionsCompleted, String deviceId, String fcmId,
                String myReferralCode, List<Long> timestamps, List<String> joinedReferralCode, List<SpecialSessionVideo> specialSessionVideos) {
        this.userName = userName;
        this.state = state;
        this.city = city;
        this.noOfSessionsCompleted = noOfSessionsCompleted;
        this.deviceId = deviceId;
        this.fcmId = fcmId;
        this.myReferralCode = myReferralCode;
        this.timestamps = timestamps;
        this.joinedReferralCode = joinedReferralCode;
        this.specialSessionVideos = specialSessionVideos;
    }

    public String getMyReferralCode() {
        return myReferralCode;
    }

    public void setMyReferralCode(String myReferralCode) {
        this.myReferralCode = myReferralCode;
    }

    public List<String> getJoinedReferralCode() {
        return joinedReferralCode;
    }

    public void setJoinedReferralCode(List<String> joinedReferralCode) {
        this.joinedReferralCode = joinedReferralCode;
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

    public List<SpecialSessionVideo> getSpecialSessionVideos() {
        return specialSessionVideos;
    }

    public void setSpecialSessionVideos(List<SpecialSessionVideo> specialSessionVideos) {
        this.specialSessionVideos = specialSessionVideos;
    }

    public List<Long> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(List<Long> timestamps) {
        this.timestamps = timestamps;
    }
}
