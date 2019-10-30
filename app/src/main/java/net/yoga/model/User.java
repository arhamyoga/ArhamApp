package net.yoga.model;

public class User {

    private String userName;
    private String mobileNumber;
    private String email;
    private String city;

    public User() {
    }

    public User(String userName, String mobileNumber, String city) {
        this.userName = userName;
        this.mobileNumber = mobileNumber;
        this.city = city;
    }

    public User(String userName, String mobileNumber, String email, String city) {
        this.userName = userName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.city = city;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
